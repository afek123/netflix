#ifndef POSTCOMMAND_H
#define POSTCOMMAND_H

#include "ICommand.h"  // Base interface for all commands
#include <map>         // For storing user-movie mappings
#include <vector>      // For storing lists of movie IDs

using namespace std;   // Allows usage of standard library classes without prefixing std::
/**
 * @class 
 * @brief Implements the ICommand interface to manage adding movies for users.
 * 
 * This class provides functionality to add movie IDs to a user's list and
 * persist the data by saving to and loading from a file.
 */
class PostCommand : public ICommand {
private:
    map<string, vector<string>> userMovies; // Maps user IDs to their associated movie IDs
public:

    /**
     * @brief Executes the command to add movies for a user.
     * @param args A string containing the user ID and movie IDs separated by spaces.
     */
    string execute(const string& args) override;

};

#endif // POSTCOMMAND_H