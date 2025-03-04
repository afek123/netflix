#include "PatchCommand.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <set>
#include <algorithm>
#include <filesystem>
#include "Db.h"
#include "App.h"
using namespace std;

string PatchCommand::execute(const string& args) {
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
    userMovies = d.loadFromFile(fileName);

    // Print user movies for debugging
    for (const auto& [userId, movies] : userMovies) {
        cout << userId << ": ";
        for (const string& movie : movies) {
            cout << movie << " ";
        }
        cout << endl;
    }

    istringstream iss(args);
    string userId;

    // Check if userId is valid
    if (!(iss >> userId)) {
        return "400 Bad Request\n";
    }

    set<string> moviesToAdd;
    string movieId;

    // Parse movie IDs from the input arguments
    while (iss >> movieId) {
        if (movieId.empty()) {
            return "400 Bad Request\n";
        }
        moviesToAdd.insert(movieId);
    }

    // Access the user's movie list
    vector<string>& userMoviesList = userMovies[userId];

    // Check if user exists
    if (userMoviesList.empty()) {
        return "404 Not Found\n";
    }

    // Add new movies to the user's list if they are not already present
    for (const string& movie : moviesToAdd) {
        if (find(userMoviesList.begin(), userMoviesList.end(), movie) == userMoviesList.end()) {
            userMoviesList.push_back(movie);
        }
    }

    // Save updated data back to the file
    d.saveToFile(fileName, userMovies);

    // Send success response
    return "204 No Content\n";
}
