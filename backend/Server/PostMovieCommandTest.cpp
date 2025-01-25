#include <gtest/gtest.h>
#include <fstream>
#include <vector>
#include <string>
#include <iostream>
#include "Db.h"
#include "PostCommand.h"

// Test fixture class for PostMoviesCommand tests
class PostMoviesCommandTest : public ::testing::Test {
protected:
    Db db; // Database instance
    PostCommand* postCommand; // Command instance

    // Setup method to initialize resources before each test
    void SetUp() override {
        postCommand = new PostCommand(); // Initialize the command
        clearFile(); // Ensure the file is clean before each test
    }

    // Teardown method to clean up resources after each test
    void TearDown() override {
        delete postCommand; // Delete the command instance
        clearFile(); // Clean up the file after each test
    }

    // Helper method to clear the content of the file
    void clearFile() {
        std::ofstream file(db.getFileName(), std::ios::trunc); // Clear file content
    }
};

// Test case for posting with a user without movies
TEST_F(PostMoviesCommandTest, PostWithUserWithoutMovies) {
    std::string response = postCommand->execute("1"); // Execute the command
    std::map<std::string, std::vector<std::string>> data = db.loadFromFile(db.getFileName()); // Load data from file
    EXPECT_EQ(response, "400 Bad Request\n"); // Check response

    EXPECT_TRUE(data.empty()); // Verify no data is stored

    // Print file content after executing command
    std::cout << "After executing command, file content is:\n";
    for (const auto& [userId, movies] : data) {
        std::cout << userId << ": ";
        for (const std::string& movie : movies) {
            std::cout << movie << " ";
        }
        std::cout << std::endl;
    }
}

// Test case for posting to a user that does not exist
TEST_F(PostMoviesCommandTest, PostToUserNotExist) {
    std::string response = postCommand->execute("1 200 201"); // Execute the command
    std::vector<std::string> expectedMovies = {"200", "201"}; // Expected movies
    std::map<std::string, std::vector<std::string>> data = db.loadFromFile(db.getFileName()); // Load data from file
    EXPECT_EQ(response, "201 Created\n"); // Check response
    EXPECT_EQ(data["1"].size(), expectedMovies.size()); // Check data size
    for (size_t i = 0; i < expectedMovies.size(); ++i) {
        EXPECT_EQ(data["1"][i], expectedMovies[i]); // Check movie data
    }

    // Print file content after executing command
    for (const auto& [userId, movies] : data) {
        std::cout << userId << ": ";
        for (const std::string& movie : movies) {
            std::cout << movie << " ";
        }
        std::cout << std::endl;
    }
}

// Test case for trying to post to an existing user
TEST_F(PostMoviesCommandTest, TryPostToUserExist) {
    postCommand->execute("1 100 101"); // Execute the initial command
    std::string response = postCommand->execute("1 102 103"); // Attempt to post to the same user
    std::vector<std::string> expectedMovies = {"100", "101"}; // Expected movies remain unchanged
    std::map<std::string, std::vector<std::string>> data = db.loadFromFile(db.getFileName()); // Load data from file
    EXPECT_EQ(response, "404 Not Found\n"); // Check response
    EXPECT_EQ(data["1"].size(), expectedMovies.size()); // Check data size
    for (size_t i = 0; i < expectedMovies.size(); ++i) {
        EXPECT_EQ(data["1"][i], expectedMovies[i]); // Check movie data
    }

    // Print file content after executing command
    for (const auto& [userId, movies] : data) {
        std::cout << userId << ": ";
        for (const std::string& movie : movies) {
            std::cout << movie << " ";
        }
        std::cout << std::endl;
    }
    clearFile();
}
