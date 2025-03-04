const { create } = require('../models/users');
const userService = require('../services/users');
const createJWT = require('../utils/jwt');
const { createToken } = require('../utils/jwt');


// Register a new user
const registerUser = async (req, res) => {
    try {
        console.log('Request Body:', req.body);
        console.log('Uploaded File:', req.files);

        const { username, password, name } = req.body;
        const picture = req.files.picture ? `/profilePictures/${req.files.picture[0].filename}` : null;

        if (!username || !password || !name || !picture) {
            return res.status(400).json({ error: 'Username, password, name and picture are required' });
        }
        const user = await userService.createUser({ username, password, name, picture });
        res.status(201).json(user);
    } catch (error) {
        console.error('Error:', error);
        res.status(500).json({ error: 'Failed to create user' });
    }
};

// Get user details by ID
const getUserDetails = async (req, res) => {
    try {
        const user = await userService.getUserById(req.params.id);
        if (!user) {
            return res.status(404).json({ error: 'User not found' });
        }
        res.status(200).json(user);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};

// Login user
const loginUser = async (req, res) => {
    const { username, password } = req.body;
    if (!username || !password) {
        return res.status(400).json({ error: 'Username and password are required' });
    }
    try {
        const user = await userService.authenticateUser(req.body);
        if (!user) {
            return res.status(401).json({ error: 'Invalid credentials' });
        }
        const token = createJWT(user);
        console.log('Token:', token);
        res.status(200).json({ token , userId: user._id ,role: user.role});
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};
// Add movie to watched list
const addMovieToWatched = async (req, res) => {
    try {
        const { userId, movieId } = req.params;
        if (!userId || !movieId) {
            return res.status(400).json({ error: 'User ID and Movie ID are required' });
        }
        const user = await userService.addMovieToWatched(userId, movieId);
        res.status(200).json(user);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
};
// Check if user is registered and return user ID
const checkUserToken = async (req, res) => {
    const { username, password } = req.body;
    if (!username || !password || Object.keys(req.body).length > 2) {
        return res.status(400).json({ error: 'Username and password are the only required' });
    }
    try {
        const user = await userService.authenticateUser(req.body);
        if (!user) {
            return res.status(401).json({ error: 'Invalid credentials' });
        }
        res.status(200).json({ userId: user._id });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};
module.exports = {addMovieToWatched,registerUser, getUserDetails, loginUser, checkUserToken };