const express = require('express');
const router = express.Router();
const userController = require('../controllers/users');
const uploadFields = require('../middleware/multer2'); // Import the Multer middleware
const checkRole = require('../middleware/checkRole'); // Import the checkRole middleware

// Register a new user
router.post('/users',uploadFields, userController.registerUser);

// Get user details by ID
router.get('/users/:id', userController.getUserDetails);
router.post('/add-movie-to-watched', userController.addMovieToWatched);
// Login user

router.post('/login', userController.loginUser);

// Check if user is registered and return user ID
router.post('/tokens', userController.checkUserToken);
router.use('/manager', checkRole('manager')); // Apply the checkRole middleware to manager routes

module.exports = router;