#include "GetRecommendMoviesCommand.h"
#include <iostream>
#include <vector>
#include <map>
#include <unordered_set>
#include <sstream>
#include <fstream>
#include <string>
#include <algorithm>
#include <functional>
#include "Db.h"

using namespace std;

// Check if a user exists
bool GetRecommendMoviesCommand::isUserExist(string userId) {
    return userMovies.count(userId) > 0;
}

// Check if a user has already watched a movie
bool GetRecommendMoviesCommand::didUserWatchMovie(string userId, string movieId) {
    if (userMovies.find(userId) != userMovies.end()) {
        const vector<string>& movies = userMovies[userId];
        return find(movies.begin(), movies.end(), movieId) != movies.end();
    }
    return false;
}

// Calculate similarity between two users
int GetRecommendMoviesCommand::similarity(string user1, string user2) {
    unordered_set<string> movies1(userMovies[user1].begin(), userMovies[user1].end());
    unordered_set<string> movies2(userMovies[user2].begin(), userMovies[user2].end());

    int commonMovies = 0;
    for (const string& movie : movies1) {
        if (movies2.find(movie) != movies2.end()) {
            commonMovies++;
        }
    }
    return commonMovies;
}

// Limit the number of recommended movies to 10
vector<string> GetRecommendMoviesCommand::limitRecommendations(const vector<string>& recommendations, int limit) {
    if (recommendations.size() <= limit) {
        return recommendations;
    }
    return vector<string>(recommendations.begin(), recommendations.begin() + limit);
}

// Calculate relevance for a movie
int GetRecommendMoviesCommand::calculateCardinality(string movieId, string userId) {
    int cardinality = 0;
    for (const auto& [otherUserId, _] : userMovies) {
        if (didUserWatchMovie(otherUserId, movieId)) {
            cardinality += similarity(userId, otherUserId);
        }
    }
    return cardinality;
}

// Recommend movies based on relevance
vector<string> GetRecommendMoviesCommand::recommend(string userId, string movieId) {
    map<string, int> movieRelevance;

    // Go through all users and calculate relevance
    for (const auto& [otherUserId, movies] : userMovies) {
        if (otherUserId == userId) continue;

        int sim = similarity(userId, otherUserId);
        if (sim == 0 || !didUserWatchMovie(otherUserId, movieId)) continue;

        for (const string& movie : movies) {
            if (!didUserWatchMovie(userId, movie) && movie != movieId) {
                movieRelevance[movie] += sim;
            }
        }
    }
    if (movieRelevance.empty()) {
        return {};
    }

    // Sort movies by relevance and movie ID
    vector<pair<string, int>> sortedMovies(movieRelevance.begin(), movieRelevance.end());
    sort(sortedMovies.begin(), sortedMovies.end(), [](const pair<string, int>& a, const pair<string, int>& b) {
        if (a.second == b.second) {
            return a.first < b.first; // If relevance is equal, sort by movie ID
        }
        return b.second < a.second; // Sort by relevance
    });

    // Take movie IDs for the result
    vector<string> result;
    for (const auto& [movieId, _] : sortedMovies) {
        result.push_back(movieId);
    }

    return result;
}

// Execute the recommendation logic
string GetRecommendMoviesCommand::execute(const string& args) {
    istringstream iss(args);
    string userId;
    string movieId;
    iss >> userId >> movieId;
    if (args[0] == ' ') {
        if (count(args.begin(), args.end(), ' ') != 2) {
            return "400 Bad Request\n";
        }
    } else if (count(args.begin(), args.end(), ' ') != 1) {
        return "400 Bad Request\n";
    }
    
    Db db;
    userMovies = db.loadFromFile(db.getFileName());
    if (!isUserExist(userId)) {
        return "404 Not Found\n";
    }
    
    vector<string> recommendedMovies = recommend(userId, movieId);
    if (recommendedMovies.empty()) {
        return "404 Not Found\n";
    }

    ostringstream oss;
    oss << "200 OK\n\n";
    recommendedMovies = limitRecommendations(recommendedMovies, 10);
    for (size_t i = 0; i < recommendedMovies.size(); i++) {
        if (i == recommendedMovies.size() - 1) {
            oss << recommendedMovies[i] << " \n";
            break;
        }
        oss << recommendedMovies[i] << " ";
    }
    return oss.str();
}
