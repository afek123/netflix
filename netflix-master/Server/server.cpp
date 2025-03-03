#include <iostream>
#include <queue>
#include <vector>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <cstring>
#include <cstdlib> // for atoi
#include "App.h"

using namespace std;

// Global variables for thread pool
queue<int> clientQueue;                     // Queue to store client sockets
mutex queueMutex;                           // Mutex for thread synchronization
condition_variable queueCondition;          // Condition variable to signal threads
bool stopThreads = false;                   // Flag to stop the threads

// Function for worker threads
void workerThread() {
    while (true) {
        int client_socket;

        // Lock the queue and wait for a client socket
        {
            unique_lock<mutex> lock(queueMutex);
            queueCondition.wait(lock, [] { return !clientQueue.empty() || stopThreads; });

            if (stopThreads && clientQueue.empty()) {
                return; // Exit thread if stopping and no clients left
            }

            client_socket = clientQueue.front();
            clientQueue.pop();
        }

        // Notify the client they are being processed
        const char* connectedMessage = "You are now connected to the server.\n";
        //send(client_socket, connectedMessage, strlen(connectedMessage), 0);

        // Handle the client
        App app(client_socket);
        app.run();
        close(client_socket); // Close the client socket after processing
    }
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <port>" << endl;
        return EXIT_FAILURE;
    }

    int port = atoi(argv[1]);
    if (port <= 0 || port > 65535) {
        cerr << "Invalid port number. Please specify a valid port (1-65535)." << endl;
        return EXIT_FAILURE;
    }

    int server_fd, client_socket;
    struct sockaddr_in address;
    int addrlen = sizeof(address);

    // Create socket
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Socket creation failed");
        return EXIT_FAILURE;
    }

    // Define server address
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(port);

    // Bind the socket
    if (bind(server_fd, (struct sockaddr*)&address, sizeof(address)) < 0) {
        perror("Bind failed");
        return EXIT_FAILURE;
    }

    // Listen for connections
    if (listen(server_fd, 100) < 0) {
        perror("Listen failed");
        return EXIT_FAILURE;
    }

    cout << "Server is running on port " << port << "...\n";

    // Thread pool setup
    const int THREAD_POOL_SIZE = 4; // Adjust the pool size as needed
    vector<thread> threadPool;

    for (int i = 0; i < THREAD_POOL_SIZE; ++i) {
        threadPool.emplace_back(workerThread);
    }

    // Accept incoming client connections
    while (true) {
        if ((client_socket = accept(server_fd, (struct sockaddr*)&address, (socklen_t*)&addrlen)) < 0) {
            perror("Accept failed");
            continue; // Continue accepting other clients even if one failed
        }

        // Notify the client they are waiting
        const char* waitingMessage = "All workers are busy. You are in the queue. Please wait...\n";
        {
            lock_guard<mutex> lock(queueMutex);
            if (clientQueue.size() >= THREAD_POOL_SIZE) {
                //send(client_socket, waitingMessage, strlen(waitingMessage), 0);
            }

            clientQueue.push(client_socket);
        }
        queueCondition.notify_one(); // Notify one worker thread
    }

    // Cleanup
    {
        lock_guard<mutex> lock(queueMutex);
        stopThreads = true;
    }
    queueCondition.notify_all(); // Notify all threads to exit

    for (thread& t : threadPool) {
        t.join();
    }

    close(server_fd);
    return 0;
}
