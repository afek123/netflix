import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { fetchMovieById } from "../services/movieService";
import { addMovieToWatched } from "../services/userService";
import "./MoviePage.css"; // Import the CSS file

function MoviePage() {
  const { id } = useParams(); // Get movie ID from URL
  const [movie, setMovie] = useState(null); // State to store the current movie
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

    getMovieDetails();
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
      const userId = "6795112050cfff934593584b"; // Replace with the actual user ID
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
              src={movie.posterUrl || "https://via.placeholder.com/300x450"}
              alt={movie.title}
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
                  <span>{movie.category}</span>
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