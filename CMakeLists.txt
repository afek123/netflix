cmake_minimum_required(VERSION 3.27)
project(netflix)

set(CMAKE_CXX_STANDARD 14)

include_directories(backend/Server)

add_executable(netflix
        backend/Server/App.cpp
        backend/Server/App.h
        backend/Server/Db.cpp
        backend/Server/Db.h
        backend/Server/DeleteMoviesCommand.cpp
        backend/Server/DeleteMoviesCommand.h
        backend/Server/DeleteMoviesCommandTests.cpp
        backend/Server/GetRecommendMoviesCommand.cpp
        backend/Server/GetRecommendMoviesCommand.h
        backend/Server/GetRecommendMoviesCommandTests.cpp
        backend/Server/HelpCommand.cpp
        backend/Server/HelpCommand.h
        backend/Server/ICommand.h
        backend/Server/PatchCommand.cpp
        backend/Server/PatchCommand.h
        backend/Server/PatchMovieCommandTest.cpp
        backend/Server/PostCommand.cpp
        backend/Server/PostCommand.h
        backend/Server/PostMovieCommandTest.cpp
        backend/Server/server.cpp)
