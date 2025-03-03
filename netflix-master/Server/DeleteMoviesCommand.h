#ifndef DELETEMOVIESCOMMAND_H
#define DELETEMOVIESCOMMAND_H

#include <vector>
#include <map>
#include "ICommand.h"
#include <iostream>
#include <sstream>
#include <fstream>
#include <string>

using namespace std;


/**
 * @class DeleteMoviesCommand
 * @brief Implements the logic for deleting movies from the user-movie data.
 */
class DeleteMoviesCommand : public ICommand {

private:
    map<string, vector<string>> userMovies;

    bool isUserExist(string userid);
    bool didUserWatchMovie(string userid, string movieid);
    void deleteMovie(string userid, string movieid);


public:
    /**
     * @brief Executes the delete movies command.
     * @param args The arguments for the command.
     * @return The result of the command.
     */
    string execute(const string& args) override;
};

#endif // DELETEMOVIESCOMMAND_H