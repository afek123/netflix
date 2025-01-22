const User = require('../models/users'); // Assuming you have a User model defined
const Movie = require('../models/movie');

// Create a new user
const createUser = async (userData) => {
    const user = new User(userData);
    await user.save();
    return user;
};

// Get user by ID
const getUserById = async (userId) => {
    return await User.findById(userId).select('-password');
};

// Authenticate user
const authenticateUser = async (credentials) => {
    const user = await User.findOne({ username: credentials.username }).select('+password');;
    if (user && user.password === credentials.password) { // Simplified for example purposes
        user.password = undefined;
        return user;
    }
    return null;
};

// Add movie to watched list
const addMovieToWatched = async (userId, movieId) => {
    const user = await User.findById(userId);
    if (!user) {
        throw new Error('User not found');
    }
    const movie = await Movie.findById(movieId);
    if (!movie) {
        throw new Error('Movie not found');
    }
    // Add movie to user's watched list
    user.watchlist.push(movieId);
    await user.save();

    // Add user to movie's watchedBy list
    movie.watchedBy.push(userId);
    await movie.save();

    return user;
};
module.exports = {createUser, getUserById, authenticateUser,addMovieToWatched};