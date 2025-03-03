#include "Db.h"
#include <iostream>
#include <fstream>
#include <sstream>

using namespace std;

string Db::getFileName() {
    std::lock_guard<std::mutex> lock(dbMutex); // Lock the mutex for thread safety
    return this->fileName;
}

void Db::saveToFile(const string &fileName, map<string, vector<string>> userMovies) {
    std::lock_guard<std::mutex> lock(dbMutex); // Lock the mutex for thread safety
    ofstream file(fileName, ios_base::trunc); // Open file in truncate mode to overwrite

    if (!file) {
        cerr << "Error opening file " << fileName << endl;
        return;
    }

    // Write each user and their list of movies to the file
    for (const auto &[userId, movies] : userMovies) {
        file << userId;
        for (string movie : movies) {
            file << " " << movie;
        }
        file << "\n";
    }

    file.close(); // Close the file
}

map<string, vector<string>> Db::loadFromFile(const string &fileName) {
    std::lock_guard<std::mutex> lock(dbMutex); // Lock the mutex for thread safety
    map<string, vector<string>> userMovies = {};
    ifstream file(fileName);

    // Check if the file exists; if not, create it and return an empty map
    if (!file.is_open()) {
        ofstream newFile(fileName); // Create the file if it doesn't exist
        return {}; // Return an empty map
    }

    userMovies.clear(); // Clear any existing data to prevent duplication
    string line;

    // Read each line of the file
    while (getline(file, line)) {
        istringstream iss(line);
        string userId, movieId;
        iss >> userId;

        // Extract movie IDs and associate them with the user ID
        while (iss >> movieId) {
            userMovies[userId].push_back(movieId);
        }
    }

    file.close(); // Close the file
    return userMovies; // Return the loaded user-movie data
}
