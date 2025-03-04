# include <iostream>
# include <sstream>
# include <fstream>
# include <vector>
# include <map>
# include <filesystem>
# include <algorithm>
# include "DeleteMoviesCommand.h"
# include "Db.h"

using namespace std;
string DeleteMoviesCommand::execute(const string& args) {
    filesystem::path dir = "data";

    // Create the directory if it does not exist
    if (!filesystem::exists(dir)) {
        filesystem::create_directories(dir);
    }
    istringstream iss(args);
    if (args.size() < 2) {
        return "400 Bad Request\n";
    }
    vector<string> movies;
    string userid;
    iss >> userid;
    string movieid;
    
    //parse the movie ids
    while (iss >> movieid) {
        movies.push_back(movieid);
    }
   
    Db db;
    userMovies = db.loadFromFile(db.getFileName());
    //if the user does not exist, return 404 Not Found
    if (!isUserExist(userid)) {
        return "404 Not Found\n";
    }
    //if the user has not watched one of the movies, return 404 Not Found
    for (string movie : movies) {
        if (!didUserWatchMovie(userid, movie)) {
            return "404 Not Found\n";
        }
    }
    //delete the movies from the user-movie data
    for (string movie : movies) {
        deleteMovie(userid, movie);
        db.saveToFile(db.getFileName(), userMovies);
    }
    return "200 OK\n";
}

bool DeleteMoviesCommand::isUserExist(string userid) { // Check if a user exists
    return userMovies.find(userid) != userMovies.end();
}

bool DeleteMoviesCommand::didUserWatchMovie(string userid, string movieid) { // Check if a user has watched a movie
    if (userMovies.find(userid) != userMovies.end()) {
        const vector<string>& movies = userMovies[userid];
        return find(movies.begin(), movies.end(), movieid) != movies.end();
    }
    return false;
}

void DeleteMoviesCommand::deleteMovie(string userid, string movieid) {
    vector<string>& movies = userMovies[userid]; // Get the list of movies the userId has watched
    auto it = find(movies.begin(), movies.end(), movieid); // Find the movieid in the list
    if (it != movies.end()) { // Check if the movieid was found
        movies.erase(it); // Erase the movieid from the list if found
    }
}