const movieService = require('../services/movie');
const Movie = require('../models/movie');
// Create a new movie
const createMovie = async (req, res) => {
    try {
      console.log('Request Body:', req.body);
      console.log('Uploaded Files:', req.files);
  
      const { title, director, category } = req.body;
  
      // Check for video and poster files in req.files
      const poster = req.files.posterUrl ? `/uploads/${req.files.posterUrl[0].filename}` : null; 
      const video = req.files.videoUrl ? `/uploads/${req.files.videoUrl[0].filename}` : null;
      const categoryArray = typeof category === 'string' ? category.split(',') : [];

      // Validate input
      if (!title || !director || !category || !video || !poster) {
        return res.status(400).json({ error: 'All fields (title, director, category, video, poster) are required' });
      }
  
      // Create a new movie object
      const newMovie = new Movie({
        title,
        director,
        category:categoryArray,
        videoUrl: video,  // Use the uploaded video file path
        posterUrl: poster, // Use the uploaded poster file path
      });
  
      // Save to database
      const savedMovie = await newMovie.save();
      res.status(201).json(savedMovie);
    } catch (err) {
      console.error('Error:', err);
      res.status(500).json({ error: 'Failed to create movie' });
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

const updateMovie = async (req, res) => {
    try {
        console.log('Request Body:', req.body);
        console.log('Uploaded Files:', req.files);

        const { title, director, category } = req.body;

        // Handle file uploads (if files are uploaded)
        const posterUrl = req.files.posterUrl ? `/uploads/${req.files.posterUrl[0].filename}` : null;
        const videoUrl = req.files.videoUrl ? `/uploads/${req.files.videoUrl[0].filename}` : null;

        // Prepare the movie data for updating
        const movieData = {
            title,
            director,
            category,
            videoUrl,
            posterUrl,
        };

        // Call the service to update the movie
        const updatedMovie = await movieService.updateMovie(req.params.id, movieData);

        if (!updatedMovie) {
            return res.status(404).json({ error: 'Movie not found or invalid category' });
        }

        // Return the updated movie
        res.status(200).json(updatedMovie);
    } catch (err) {
        console.error('Error:', err);
        res.status(500).json({ error: 'Failed to update movie' });
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