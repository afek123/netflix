const router = require('express').Router();
const uploadFields = require('../middleware/multer'); // Import the Multer middleware
const movieController = require('../controllers/movie');
const recommendationController = require('../controllers/recommendation');
let Movie = require('../models/movie');

router.route('/').get((req, res) => {
    Movie.find()
      .then(movies => res.json(movies))
      .catch(err => res.status(400).json('Error: ' + err));
  });
  
// Routes for movies
router.route('/')
    .get(movieController.getMovies)
    .post(uploadFields, movieController.createMovie); // Pass `upload` directly

router.route('/:id')
    .get(movieController.getMovie) // Get a movie by ID
    .put(movieController.updateMovie) // Update a movie
    .delete(movieController.deleteMovie); // Delete a movie

// Route for searching movies
router.route('/search/:query')
    .get(movieController.searchMovies);

// Routes for recommendations
router.route('/:userId/:movieId/recommend')
    .get(recommendationController.getRecommendations)
    .post(recommendationController.addRecommendation)
    .delete(recommendationController.deleteRecommendation);

module.exports = router;
