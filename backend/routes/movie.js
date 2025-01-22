const router = require('express').Router();
let Movie = require('../models/movie');

// Get all movies
router.route('/').get((req, res) => {
  Movie.find()
    .then(movies => res.json(movies))
    .catch(err => res.status(400).json('Error: ' + err));
});

// Get movie by ID
router.route('/:id').get((req, res) => {
  Movie.findById(req.params.id)
    .then(movie => res.json(movie))
    .catch(err => res.status(400).json('Error: ' + err));
});

module.exports = router;
const recommendationController = require('../controllers/recommendation');

// Routes for recommendations
router.route('/:userId/:movieId/recommend')
    .get(recommendationController.getRecommendations)
    .post(recommendationController.addRecommendation)
    .delete(recommendationController.deleteRecommendation);

const movieController = require('../controllers/movie');

router.route('/')
    .get(movieController.getMovies)
    .post(movieController.createMovie);

router.route('/:id')
    .get(movieController.getMovie)
    .put(movieController.updateMovie)
    .delete(movieController.deleteMovie);

router.route('/search/:query')
    .get(movieController.searchMovies);
module.exports = router;