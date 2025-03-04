#include <gtest/gtest.h>
#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <functional>
#include "DeleteMoviesCommand.h"
#include "Db.h" // Include the header file for the Db class
#include <filesystem>

class DeleteMoviesCommandTest : public ::testing::Test {
protected:
    DeleteMoviesCommand* deleteCommand;
    Db db = Db(); // Create an instance of the Db class
    // Helper method to clear the content of the file
    void clearFile() {
        std::ofstream file(db.getFileName(), std::ios::trunc); // Clear file content
    }
public:
    void SetUp() override {
        // Create the directory if it does not exist
        filesystem::path dir = "data";
        if (!std::filesystem::exists("data")) {
            std::filesystem::create_directories("data");
        }
        std::filesystem::create_directories(dir);
        db.loadFromFile(db.getFileName());
        db.saveToFile("data/movies_data.txt", {
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
        deleteCommand = new DeleteMoviesCommand();
    }
    void TearDown() override {
        delete deleteCommand;
    }
};

// Test case for 200 OK
TEST_F(DeleteMoviesCommandTest, needToDelete) {
    string result = deleteCommand->execute("1 101");
    EXPECT_EQ("200 OK\n", result);
    map<string, vector<string>> userMoviestester = db.loadFromFile(db.getFileName());
    vector<string> user1Movies = userMoviestester["1"];
    EXPECT_EQ(3, user1Movies.size());
    EXPECT_EQ("100", user1Movies[0]);
    EXPECT_EQ("102", user1Movies[1]);
    EXPECT_EQ("103", user1Movies[2]);
}

// Test case for 404 Not Found because the user does not exist
TEST_F(DeleteMoviesCommandTest, userNotExist) {
    string result = deleteCommand->execute("11 101");
    EXPECT_EQ("404 Not Found\n", result);
    map<string, vector<string>> userMoviestester = db.loadFromFile(db.getFileName());
    EXPECT_EQ(10, userMoviestester.size());
}

// Test case that the output db file is correct if we not delete anything
TEST_F(DeleteMoviesCommandTest, noNeedToDelete) {
    string result = deleteCommand->execute("1 104");
    EXPECT_EQ("404 Not Found\n", result);
    map<string, vector<string>> userMoviestester = db.loadFromFile(db.getFileName());
    vector<string> user1Movies = userMoviestester["1"];
    EXPECT_EQ(4, user1Movies.size());
    EXPECT_EQ("100", user1Movies[0]);
    EXPECT_EQ("101", user1Movies[1]);
    EXPECT_EQ("102", user1Movies[2]);
    EXPECT_EQ("103", user1Movies[3]);
}

// Test case for 400 Bad Request because the input is invalid
TEST_F(DeleteMoviesCommandTest, overFlowInput) {
    string result = deleteCommand->execute("1 101 102 105");
    EXPECT_EQ("404 Not Found\n", result);
    map<string, vector<string>> userMoviestester = db.loadFromFile(db.getFileName());
    vector<string> user1Movies = userMoviestester["1"];
    EXPECT_EQ(4, user1Movies.size());
    EXPECT_EQ("100", user1Movies[0]);
    EXPECT_EQ("101", user1Movies[1]);
    EXPECT_EQ("102", user1Movies[2]);
    EXPECT_EQ("103", user1Movies[3]);
}

// Test case for 400 Bad Request because the input is invalid
TEST_F(DeleteMoviesCommandTest, lessInput) {
    string result = deleteCommand->execute("1");
    EXPECT_EQ("400 Bad Request\n", result);
    map<string, vector<string>> userMoviestester = db.loadFromFile(db.getFileName());
    vector<string> user1Movies = userMoviestester["1"];
    EXPECT_EQ(4, user1Movies.size());
    EXPECT_EQ("100", user1Movies[0]);
    EXPECT_EQ("101", user1Movies[1]);
    EXPECT_EQ("102", user1Movies[2]);
    EXPECT_EQ("103", user1Movies[3]);
}

// Test case for 3 arguments
TEST_F(DeleteMoviesCommandTest, threeArguments) {
    string result = deleteCommand->execute("3 104 105");
    EXPECT_EQ("200 OK\n", result);
    map<string, vector<string>> userMoviestester = db.loadFromFile(db.getFileName());
    vector<string> user3Movies = userMoviestester["3"];
    EXPECT_EQ(3, user3Movies.size());
    EXPECT_EQ("100", user3Movies[0]);
    EXPECT_EQ("107", user3Movies[1]);
    EXPECT_EQ("108", user3Movies[2]);
    clearFile();
}
