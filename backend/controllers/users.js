const userService = require('../services/users');

// Register a new user
const registerUser = async (req, res) => {
    const { username, password, name, picture } = req.body;
    if (!username || !password || !name || !picture) {
        return res.status(400).json({ error: 'Username, password, name and picture are required' });
    }
    try {
        const user = await userService.createUser(req.body);
        res.status(201).json(user);
    } catch (error) {
        res.status(400).json({ error: error.message });
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
        res.status(200).json(user);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};
// Add movie to watched list
const addMovieToWatched = async (req, res) => {
    try {
        const { userId, movieId } = req.body;
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