#ifndef GETRECOMMENDMOVIESCOMMAND_H
#define GETRECOMMENDMOVIESCOMMAND_H

#include <vector>
#include <map>
#include "ICommand.h"
#include <string>

using namespace std;

/**
 * @class RecommendMoviesCommand
 * @brief Implements the recommendation logic for movies based on user similarity.
 */
class GetRecommendMoviesCommand : public ICommand {
private:
    map<string, vector<string>> userMovies;

    bool isUserExist(string userid);
    bool didUserWatchMovie(string userid, string movieid);
    int similarity(string user1, string user2);
    int calculateCardinality(string movieId, string userId);
    vector<string> getRelevantMovies(string movieid, string userid);
    map<string, string> calculateMovieRelevance(string movieid, string userid);
    map<string, string> calculateSimilarityScores(string userid);
    vector<pair<string, string>> sortMatching(const map<string, string>& matchingMap);
    vector<string> getTopTenMoviesList(const vector<pair<string, string>>& sortedMatching);
    vector<string> recommend(string userid, string movieid);
    vector<string> limitRecommendations(const vector<string>& recommendations, int limit);

public:
    /**
     * @brief Loads user-movie data from a file into memory.
     * @param filename The name of the file to load the data from.
     */
    string execute(const string& args) override;
};

#endif // GETRECOMMENDMOVIESCOMMAND_H