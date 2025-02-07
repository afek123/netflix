const recommendationService = require('../services/recommendation');
const movieService = require('../services/movie'); // Import movieService for fetching movie details

const getRecommendations = async (req, res) => {
    try {
        const userId = req.params.userId; // Extract userId from route
        const movieId = req.params.movieId; // Extract movieId from route
        // Fetch raw recommendations from the recommendation server

        const recommendationResponse = await recommendationService.getRecommendations(userId, movieId);

        // Check if the response indicates "404 Not Found"
        if (recommendationResponse === "404 Not Found") {
            return res.status(404).json({ error: "No recommendations found" });
        }
        // Check if the response indicates "200 OK" and contains movie IDs
        if (recommendationResponse.startsWith("200 OK")) {
            // Extract movie IDs from the response (after "\n\n")
            const movieIdsRaw = recommendationResponse.split("\n\n")[1];
            if (!movieIdsRaw) {
                return res.status(404).json({ error: "No recommendations found" });
            }

            // Split the raw movie IDs by spaces to get an array of IDs
            const movieIds = movieIdsRaw.trim().split(" ");

            // Handle case if no movie IDs are provided
            if (movieIds.length === 0) {
                return res.status(404).json({ error: "No valid movie IDs provided" });
            }

            // Fetch movie details for each movie ID (handle up to 10 movie IDs)
            const movies = await Promise.all(
                movieIds.map(async (id) => {
                    try {
                        const movie = await movieService.getMovieById(id);
                        if (!movie) {
                            console.warn(`Movie with ID ${id} not found`);
                        }
                        return movie;
                    } catch (err) {
                        console.error(`Error fetching movie with ID ${id}:`, err.message);
                        return null;
                    }
                })
            );

            // Filter out any null results in case some movie IDs were invalid
            const validMovies = movies.filter(movie => movie !== null);

            // Handle case if no valid movies were found
            if (validMovies.length === 0) {
                return res.status(404).json({ error: "No valid recommendations found" });
            }

            res.status(200).json(validMovies );
        } else {
            // Handle unexpected response
            return res.status(500).json({ error: "Unexpected response format" });
        }
    } catch (err) {
        console.error("Error fetching recommendation details:", err.message);
        res.status(500).json({ error: "Failed to fetch recommendation details" });
    }
};


const addRecommendation = async (req, res) => {
    try {
        const userId = req.params.userId; // Extract userId from route
        const movieId = req.params.movieId; // Extract movieId from route

        const response = await recommendationService.addRecommendation(userId, movieId);
        res.status(201).json({ success: response });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};
const deleteRecommendation = async (req, res) => {
    try {
        const userId = req.params.userId; // Extract userId from route
        const movieId = req.params.movieId; // Extract movieId from route

        const response = await recommendationService.deleteRecommendation(userId, movieId);
        res.status(201).json({ success: response });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

module.exports = { getRecommendations, addRecommendation,deleteRecommendation };