#ifndef APP_H
#define APP_H

#include <map>
#include <string>
#include "ICommand.h"
#include "HelpCommand.h"

class App {
private:
    std::map<std::string, ICommand*> commands; // A map to store command names and their corresponding ICommand objects
    int clientSocket;  // The socket to communicate with the client

public:
    // Constructor to initialize the App and load available commands
    App(int socket);

    // Function to register commands
    void registerCommands();

    // Function to send the response back to the client
    void sendResponse(const std::string& response);

    // Function to run the application's main loop
    void run();

    // Destructor to clean up dynamic memory for commands
    ~App();
};

#endif // APP_H
