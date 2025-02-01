import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { fetchMovieById, fetchCategories } from "../services/movieService";
import { addMovieToWatched } from "../services/userService";
import "./MoviePage.css"; // Import the CSS file

function MoviePage() {
  const { id } = useParams(); // Get movie ID from URL
  const [movie, setMovie] = useState(null); // State to store the current movie
  const [categories, setCategories] = useState({}); // State to store categories
  const navigate = useNavigate(); // For navigation

  useEffect(() => {
    const getMovieDetails = async () => {
      try {
        const movieData = await fetchMovieById(id); // Fetch movie by ID
        setMovie(movieData); // Set movie data
      } catch (error) {
        console.error("Failed to fetch movie details:", error);
      }
    };

    const getCategories = async () => {
      try {
        const categoriesData = await fetchCategories(); // Fetch categories
        const categoryMap = categoriesData.reduce((map, category) => {
          map[category._id] = category.name;
          return map;
        }, {});
        setCategories(categoryMap); // Set categories data
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      }
    };

    getMovieDetails();
    getCategories();
  }, [id]); // Re-run effect if the movie ID changes

  if (!movie) {
    return (
      <div className="movie-page-loading">
        <h2>Loading movie details...</h2>
      </div>
    );
  }

  const handlePlayClick = async () => {
    try {
      console.log(`Navigating to /play/${movie._id}`); // Log the navigation
      const userId = localStorage.getItem('userId'); // Get the user ID from local storage
      await addMovieToWatched(userId, movie._id); // Add movie to user's watchlist
      navigate(`/play/${movie._id}`); // Navigate to the play screen using useNavigate
    } catch (error) {
      console.error("Failed to add movie to watchlist:", error);
    }
  };

  return (
    <div className="movie-card-container">
      <div className="movie-card">
        <div className="movie-card-content">
          {/* Movie Poster */}
          <div className="movie-poster">
            <img
              src={
                movie.posterUrl
                  ? `http://localhost:5000${movie.posterUrl}` // Use the correct URL for the uploaded poster
                  : `https://via.placeholder.com/150x225?text=${movie.title}`
              }
              alt={movie.title || "Untitled"}
              className="poster"
            />
          </div>

          {/* Movie Details */}
          <div className="movie-details">
            <h1 className="movie-title">{movie.title}</h1>
            <p className="movie-director">
              Directed by: <span>{movie.director}</span>
            </p>

            {/* Category */}
            <div className="movie-category">
              <h3>Category:</h3>
              <div className="category-tag">
                {movie.category ? (
                  <span>{categories[movie.category] || "Unknown"}</span>
                ) : (
                  <span className="no-category">No category available</span>
                )}
              </div>
            </div>

            {/* Play Button */}
            <button className="play-button" onClick={handlePlayClick}>
              Play Movie
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MoviePage;