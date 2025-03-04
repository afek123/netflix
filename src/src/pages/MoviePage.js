import React, { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { fetchMovieById, fetchCategories, fetchMovieRecommendations } from "../services/movieService";
import { addMovieToWatched } from "../services/userService";
import "./MoviePage.css"; // Import the CSS file
import ThemeToggleButton from '../components/ThemeToggleButton';

function MoviePage() {
  const { id } = useParams(); // Get movie ID from URL
  const [movie, setMovie] = useState(null); // State to store the current movie
  const [categories, setCategories] = useState({}); // State to store categories
  const navigate = useNavigate(); // For navigation
  const [recommendedMovies, setRecommendedMovies] = useState([]); // State to store recommended movies
  const toggleButtonRef = useRef(null);

  useEffect(() => {
    const getMovieDetails = async () => {
      try {
        const userId = localStorage.getItem('userId');
        const movie = await fetchMovieById(id);
        setMovie(movie);
        // Load recommendations only if userId exists
        if (userId) {
          const recommendedMovies = await fetchMovieRecommendations(userId, id);
          console.log("Recommended movies:", recommendedMovies);
          setRecommendedMovies(recommendedMovies);
        }
      } catch (error) {
        console.error("Failed to fetch movie details:", error);
      }
    };

    getMovieDetails();
  }, [id]);

  useEffect(() => {
    // Programmatically click the toggle button to synchronize the background image
    if (toggleButtonRef.current) {
      toggleButtonRef.current.click();
    }
  }, []);

  useEffect(() => {
    const getCategories = async () => {
      try {
        const categoriesData = await fetchCategories(); // Fetch categories
        const categoryMap = categoriesData.reduce((map, category) => {
          map[category._id] = category.name;
          return map;
        }, {});
        setCategories(categoryMap);
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      }
    };

    getCategories();
  }, []);

  if (!movie) {
    return (
      <div className="movie-page-loading">
        <h2>Loading movie details...</h2>
      </div>
    );
  }

  const handlePlayClick = async () => {
    console.log(`Navigating to /play/${movie._id}`); // Log the navigation
    const userId = localStorage.getItem('userId'); // Get the user ID from local storage
    await addMovieToWatched(userId, movie._id); // Add movie to user's watchlist
    navigate(`/play/${movie._id}`); // Navigate to the play screen using useNavigate
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
              <h3>Categories:</h3>
              <div className="category-tags">
                {movie.category && movie.category.length > 0 ? (
                  movie.category.map((categoryId) => (
                    <span key={categoryId} className="category-tag">
                      {categories[categoryId] || "Unknown"}
                    </span>
                  ))
                ) : (
                  <span className="no-category">No categories available</span>
                )}
              </div>
            </div>

            {/* Play Button */}
            <button className="play-button" onClick={handlePlayClick}>
              Play Movie
            </button>
          </div>
        </div>

        {/* Recommended Movies */}
        <div className="recommended-movies">
          <h2>Recommended Movies</h2>
          <div className="recommended-movies-list">
            {recommendedMovies.length > 0 ? (
              recommendedMovies.map((recommendedMovie) => (
                <div key={recommendedMovie._id} className="recommended-movie-item">
                  <img
                    src={
                      recommendedMovie.posterUrl
                        ? `http://localhost:5000${recommendedMovie.posterUrl}`
                        : `https://via.placeholder.com/150x225?text=${recommendedMovie.title}`
                    }
                    alt={recommendedMovie.title || "Untitled"}
                    className="recommended-movie-poster"
                  />
                  <h3>{recommendedMovie.title}</h3>
                </div>
              ))
            ) : (
              <p>No recommended movies available.</p>
            )}
          </div>
        </div>
      </div>
      <ThemeToggleButton ref={toggleButtonRef} />
    </div>
  );
}

export default MoviePage;