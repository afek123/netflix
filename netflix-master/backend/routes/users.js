const express = require('express');
const router = express.Router();
const userController = require('../controllers/users');

// Register a new user
router.post('/users', userController.registerUser);

// Get user details by ID
router.get('/users/:id', userController.getUserDetails);
router.post('/add-movie-to-watched', userController.addMovieToWatched);
// Login user
router.post('/login', userController.loginUser);

// Check if user is registered and return user ID
router.post('/tokens', userController.checkUserToken);

module.exports = router;