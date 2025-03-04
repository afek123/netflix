#include "HelpCommand.h"
#include <sstream>
#include "App.h"

using namespace std;

// Constructor
HelpCommand::HelpCommand(const vector<string>& commands)
    : commands(commands) {}  // Store commands and app pointer

// Execute method to send the help message to the client
string HelpCommand::execute(const string& args) {
    // Build the response message
    stringstream response;
    response << "Commands:\n";
    for (const auto& command : commands) {
        response << "  " << command << "\n";
    }

    // Send the help message back to the client
    string responseStr = response.str();
    return (responseStr); // Use the app object to send the response over the socket
}
