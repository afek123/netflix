const movieService = require('../services/movie');
// Create a new movie
const createMovie = async (req, res) => {
    try {
        const movie = await movieService.createMovie(req.body);
        if (!movie) {
            return res.status(400).json({ error: 'Movie could not be created' });
        }
        res.status(201).json(movie);
    }
    catch (err) {
        res.status(400).json({ error: 'unable to create the movie' });
    }
};


// Get movie list of the user by ID
const getMovies = async (req, res) => {
    try {
        const userId = req.headers['userid'];
        if (!userId) {
            return res.status(400).json({ error: 'User ID is required' });
        }
        const movies = await movieService.getMovies(userId);
        if (movies == null) {
            return res.status(404).json({ error: 'user not exist' });
        }
        res.status(200).json(movies);
    }
    catch (err) {
        res.status(400).json({ error: 'unable to get the movies' });
    }
}

// Get a movie by movie ID
const getMovie = async (req, res) => {
    try {
        const movie = await movieService.getMovieById(req.params.id);
        if (!movie) {
            return res.status(404).json({ error: 'Movie not found' });
        }
        res.status(200).json(movie);
    }
    catch (err) {
        res.status(400).json({ error: 'unable to get the movie' });
    }
};

// Update a movie
const updateMovie = async (req, res) => {
    try {
        const movie = await movieService.updateMovie(req.params.id, req.body);
        if (!movie) {
            return res.status(404).json({ error: 'Movie not found' });
        }
        res.status(204).json(movie);
    }
    catch (err) {
        res.status(400).json({ error: 'unable to update the movie' });
    }
};

// Delete a movie
const deleteMovie = async (req, res) => {
    try {
        const movie = await movieService.deleteMovie(req.params.id);
        if (!movie) {
            return res.status(404).json({ error: 'Movie not found' });
        }
        res.status(204).json(movie);
    }
    catch (err) {
        res.status(400).json({ error: 'unable to delete the movie' });
    }
};

// Search movies by query
const searchMovies = async (req, res) => {
    try {
        const query = req.params.query;
        console.log('Search query:', query);
        const movies = await movieService.searchMovies(query);
        console.log('Search results:', movies);
        res.status(200).json(movies);
    }
    catch (err) {
        res.status(400).json({ error: 'unable to answer the search' });
    }
};

module.exports = { createMovie, getMovies, getMovie, updateMovie, deleteMovie , searchMovies};
