#include <gtest/gtest.h>
#include <fstream>
#include <vector>
#include <string>
#include <iostream>
#include "PatchCommand.h"
#include "Db.h"
#include "PostCommand.h"

class PatchMoviesCommandTest : public ::testing::Test {
protected:
    Db db;
    PatchCommand* patchCommand;

    void SetUp() override {
        // Initialize the database and command
        patchCommand = new PatchCommand();
        clearFile(); // Ensure the file is clean before each test
    }

    void TearDown() override {
        delete patchCommand;
        clearFile(); // Clean up after each test
    }

    void clearFile() {
        std::ofstream file(db.getFileName(), std::ios::trunc); // Clear file content
    }
};

TEST_F(PatchMoviesCommandTest, PatchToUserExist) {
    // Test patching movies to an existing user
    PostCommand* post = new PostCommand();
    post->execute("1 100 101");
    std::string response = patchCommand->execute("1 200 201"); // Test with extra spaces
    std::vector<std::string> expectedMovies = {"100", "101", "200", "201"};
    std::map<std::string, std::vector<std::string>> data = db.loadFromFile(db.getFileName());
    EXPECT_EQ(response, "204 No Content\n");
    EXPECT_EQ(data["1"].size(), expectedMovies.size());
    for (size_t i = 0; i < expectedMovies.size(); ++i) {
        EXPECT_EQ(data["1"][i], expectedMovies[i]);
    }

    for (const auto& [userId, movies] : data) {
        std::cout << userId << ": ";
        for (const std::string& movie : movies) {
            std::cout << movie << " ";
        }
        std::cout << std::endl;
    }
}

TEST_F(PatchMoviesCommandTest, TryPatchToUserNotExist) {
    // Test patching movies to a non-existing user
    PostCommand* post = new PostCommand();
    post->execute("1 100 101");
    std::string response = patchCommand->execute("2 100 101 102"); // Add more movies to the same user
    std::vector<std::string> expectedMovies = {"100", "101"};
    std::map<std::string, std::vector<std::string>> data = db.loadFromFile(db.getFileName());
    EXPECT_EQ(response, "404 Not Found\n");
    EXPECT_EQ(data["1"].size(), expectedMovies.size());
    for (size_t i = 0; i < expectedMovies.size(); ++i) {
        EXPECT_EQ(data["1"][i], expectedMovies[i]);
    }

    for (const auto& [userId, movies] : data) {
        std::cout << userId << ": ";
        for (const std::string& movie : movies) {
            std::cout << movie << " ";
        }
        std::cout << std::endl;
    }
    clearFile();
}
