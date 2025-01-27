#ifndef DB_H
#define DB_H

#include <string>
#include <map>
#include <vector>
#include <mutex>

using namespace std;

class Db {
private:
    const string fileName = "data/movies_data.txt"; // Default file name for movie data
    std::mutex dbMutex; // Mutex for thread safety

public:
    // Retrieves the file name
    string getFileName();

    // Saves user-movie data to the specified file
    void saveToFile(const string &fileName, map<string, vector<string>> userMovies);

    // Loads user-movie data from the specified file
    map<string, vector<string>> loadFromFile(const string &fileName);
};

#endif // DB_H
