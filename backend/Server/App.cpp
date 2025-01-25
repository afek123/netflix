#include "App.h"
#include <iostream>
#include "PostCommand.h"
#include "PatchCommand.h"
#include <sys/socket.h>
#include "DeleteMoviesCommand.h"
#include "GetRecommendMoviesCommand.h"
#include <vector>
#include <sstream>
#include <stdexcept>
#include <filesystem>
#include "Db.h"
#include <set>


using namespace std;

App::App(int socket) : clientSocket(socket) {
    registerCommands();
}

void App::registerCommands() {
    // Create and register the AddMoviesCommand with a reference to App for sending responses
    ICommand* POSTCommand = new PostCommand();
    commands["POST"] = POSTCommand;

    // Create and register the AddMoviesCommand with a reference to App for sending responses
    ICommand* PATCHCommand = new PatchCommand();
    commands["PATCH"] = PATCHCommand;
   vector<string> supportedCommands = {
    "DELETE, arguments: [userid] [movieid1] [movieid2] ...",
    "GET, arguments: [userid] [movieid]",
    "PATCH, arguments: [userid] [movieid1] [movieid2] ...",
    "POST, arguments: [userid] [movieid1] [movieid2] ...",
    "help"
    };


    // Create and register the HelpCommand with the list of supported commands
    ICommand* helpCommand = new HelpCommand(supportedCommands);
    commands["help"] = helpCommand;
    ICommand* DELETECommand = new DeleteMoviesCommand();
    commands["DELETE"] = DELETECommand;
    ICommand* GETcommand = new GetRecommendMoviesCommand();
    commands["GET"] = GETcommand;


}

void App::sendResponse(const string& response) {
    ssize_t bytesSent = send(clientSocket, response.c_str(), response.size(), 0);
    if (bytesSent == -1) {
        cerr << "Error sending response to client" << endl;
    }
}

void App::run() {
    while (true) {
        char buffer[1024] = {0};
        int bytesReceived = recv(clientSocket, buffer, sizeof(buffer) - 1, 0);
        if (bytesReceived <= 0) {
            cerr << "Client disconnected or error occurred" << endl;
            break; // Client disconnected or error
        }

        string inputLine(buffer); // Convert the received bytes to string

        if (inputLine.empty()) {
            continue; // Ignore empty lines
        }

        istringstream iss(inputLine);
        string commandName;
        iss >> commandName; // Extract the command name

        string args;
        getline(iss, args); // Get the remaining input as arguments
        string argscopy=args;
        cout << "Received command: " << commandName << " with arguments: " << args << endl;
        if (commands.find(commandName) != commands.end()) {
            try {
                if(commandName=="POST"){
                    // Define the directory path
                    filesystem::path dir = "data";
                    
                    // Create the directory if it does not exist
                    if (!filesystem::exists(dir)) {
                        filesystem::create_directories(dir);
                    }
                    
                    Db d;
                    string fileName = d.getFileName();
                    cout << fileName << endl;
                    
                    // Load existing data from file
                    map<string, vector<string>> userMovies = d.loadFromFile(fileName);
                
                    istringstream iss(args);
                    string userId;
                    set<string> moviesToAdd;
                    string movieId;
                     if (!(iss >> userId)) {
                        sendResponse("400 Bad Request\n") ;  // Invalid or missing userId
                    }
                    // Check if user exists
                    if (!(userMovies.find(userId) != userMovies.end())) {
                        // Call the execute method and get the response string
                        cout << "postttt" << endl;
                        cout << "Received input: " << inputLine << endl;
                        string response = commands["POST"]->execute(argscopy);
                        sendResponse(response); // Send the response to the client
                    }
                    else{
                        // Call the execute method and get the response string
                        cout << "Received input222: " << inputLine << endl;
                        string response = commands["PATCH"]->execute(argscopy);
                        sendResponse(response); // Send the response to the client
                    }
                }
                else{// Call the execute method and get the response string
                    cout << "Received input: " << inputLine << endl;
                    string response = commands[commandName]->execute(args);
                    sendResponse(response); // Send the response to the client
                }
            } catch (const exception& e) {
                cerr << "Error: " << e.what() << endl;
                sendResponse("Error: " + string(e.what()) + "\n");
            }
        } else {
            cerr << "400 Bad Request\n" << endl;
            sendResponse("400 Bad Request\n");
        }
    }
}


App::~App() {
    for (auto& command : commands) {
        delete command.second;  // Deleting dynamically allocated ICommand objects
    }
}
