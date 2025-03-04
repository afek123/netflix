#include "GetRecommendMoviesCommand.h"
#include <gtest/gtest.h>
#include <fstream>
#include <sstream>
#include <vector>
#include <set>
#include <string>
#include <algorithm>
#include <iostream>
#include <map>
#include <filesystem>
#include "Db.h"

using namespace std;

class GetRecommendMoviesCommandTests : public ::testing::Test {
protected:
    GetRecommendMoviesCommand* getRecommendMoviesCommand;
    Db db = Db();
    // Helper method to clear the content of the file
    void clearFile() {
        std::ofstream file(db.getFileName(), std::ios::trunc); // Clear file content
    }
public:
    std::filesystem::path dir = "data";

    void SetUp() override {
        // Create the directory if it does not exist
        if (!filesystem::exists(dir)) {
            filesystem::create_directories(dir);
        }
        db.loadFromFile(db.getFileName());
        db.saveToFile("data/movies_data.txt", std::map<string, std::vector<string>>{
            {"1", {"100", "101", "102", "103"}},
            {"2", {"101", "102", "104", "105", "106"}},
            {"3", {"100", "104", "105", "107", "108"}},
            {"4", {"101", "105", "106", "107", "109", "110"}},
            {"5", {"100", "102", "103", "105", "108", "111"}},
            {"6", {"100", "103", "104", "110", "111", "112", "113"}},
            {"7", {"102", "105", "106", "107", "108", "109", "110"}},
            {"8", {"101", "104", "105", "106", "109", "111", "114"}},
            {"9", {"100", "103", "105", "107", "112", "113", "115"}},
            {"10", {"100", "102", "105", "106", "107", "109", "110", "116"}}
        });
        getRecommendMoviesCommand = new GetRecommendMoviesCommand();
    }

    void TearDown() override {
        delete getRecommendMoviesCommand;
    }
};

// Test case for 200 OK
TEST_F(GetRecommendMoviesCommandTests, goodInput) {
    string result = getRecommendMoviesCommand->execute("1 104");
    EXPECT_EQ("200 OK\n\n105 106 111 110 112 113 107 108 109 114 \n", result);
}

// Test case for 404 Not Found because the user does not exist
TEST_F(GetRecommendMoviesCommandTests, userNotExist) {
    string result = getRecommendMoviesCommand->execute("11 101");
    EXPECT_EQ("404 Not Found\n", result);
}

// Test case for 404 Not Found because the movie does not exist
TEST_F(GetRecommendMoviesCommandTests, movieNotExist) {
    string result = getRecommendMoviesCommand->execute("1 117");
    EXPECT_EQ("404 Not Found\n", result);
}


// Test case for 400 Bad Request because the input is invalid
TEST_F(GetRecommendMoviesCommandTests, overFlowInput) {
    string result = getRecommendMoviesCommand->execute("1 101 102 103");
    EXPECT_EQ("400 Bad Request\n", result);
}

// Test case for 400 Bad Request because the input is invalid
TEST_F(GetRecommendMoviesCommandTests, lessInput) {
    string result = getRecommendMoviesCommand->execute("1");
    EXPECT_EQ("400 Bad Request\n", result);
}


// Test case that the recommendations are less than 10
TEST_F(GetRecommendMoviesCommandTests, lessThan10Recommendations) {
    map<string, vector<string>> userMovies = {
        {"1", {"100", "101", "102", "103"}},
        {"2", {"101", "102", "104", "105", "106"}},
        {"3", {"100", "104", "105", "107", "108"}},
        {"4", {"101", "105", "106", "107", "109", "110"}},
        {"5", {"100", "102", "103", "105", "108", "111"}},
        {"6", {"100", "103", "104", "110", "111", "112", "113"}},
        {"7", {"102", "105", "106", "107", "108", "109", "110"}},
        {"8", {"101", "104", "105", "106", "109", "111", "114"}},
        {"9", {"100", "103", "105", "107", "112", "113", "115"}},
        {"10", {"100", "102", "105", "106", "107", "109", "110", "116"}},
        {"11", {"100", "104", "115"}}
    };
    db.saveToFile("data/movies_data.txt", userMovies);
    string result = getRecommendMoviesCommand->execute("1 104");
    EXPECT_EQ("200 OK\n\n105 106 111 110 112 113 107 108 109 114 \n", result); // 10 recommendations without 115 because it is the 11th movie in the list
    clearFile();
}
