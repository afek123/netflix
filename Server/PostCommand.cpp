#include "PostCommand.h"
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

string PostCommand::execute(const string& args) {
    cout << "Input Args: " << args << endl;

    // Ensure the "data" directory exists
    filesystem::path dir = "data";
    if (!filesystem::exists(dir)) {
        filesystem::create_directories(dir);
    }

    Db d;
    string fileName = d.getFileName();

    // Load existing data from file
    userMovies = d.loadFromFile(fileName);

    // Print current user-movie data for debugging
    for (const auto& [userId, movies] : userMovies) {
        cout << "User " << userId << ": ";
        for (const string& movie : movies) {
            cout << movie << " ";
        }
        cout << endl;
    }

    // Parse the user ID and movie IDs from input arguments
    istringstream iss(args);
    string userId;

    if (!(iss >> userId)) {
        return "400 Bad Request\n"; // Invalid or missing user ID
    }

    set<string> moviesToAdd;
    string movieId;

    // Parse movie IDs from the input arguments
    while (iss >> movieId) {
        if (movieId.empty()) {
            return "400 Bad Request\n"; // Invalid movie ID
        }
        moviesToAdd.insert(movieId); // Insert only unique movie IDs
    }

    if (moviesToAdd.empty()) {
        return "400 Bad Request\n"; // No valid movie IDs provided
    }

    // Access the user's movie list
    vector<string>& userMoviesList = userMovies[userId];

    // Check if the user already has movies
    if (!userMoviesList.empty()) {
        return "404 Not Found\n"; // User already has movies in their list
    }

    // Add movies to the user's list
    for (const string& movie : moviesToAdd) {
        userMoviesList.push_back(movie);
    }

    // Save updated data back to the file
    d.saveToFile(fileName, userMovies);

    return "201 Created\n"; // Success
}
