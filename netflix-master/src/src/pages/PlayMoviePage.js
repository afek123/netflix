import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchMovieById } from '../services/movieService';
import "../styles/MoviePage.css";

function PlayMoviePage() {
  const { id } = useParams(); // Get movie ID from URL
  const [movie, setMovie] = useState(null); // State to store the current movie
  const navigate = useNavigate(); // For navigation

  useEffect(() => {
    const getMovieDetails = async () => {
      try {
        const movieData = await fetchMovieById(id); // Fetch movie by ID
        setMovie(movieData); // Set movie data
        console.log('Movie details:', movieData);
      } catch (error) {
        console.error('Failed to fetch movie details:', error);
      }
    };
    getMovieDetails();
  }, [id]); // Re-run effect if the movie ID changes

  if (!movie) {
    return (
      <div className="play-movie-loading">
        <h2>Loading movie...</h2>
      </div>
    );
  }

  return (
    <div className="play-movie-wrapper">
      <div className="play-movie-container">
        {/* Invisible Spacer */}
        <div className="invisible-spacer" style={{ height: "400px" }}></div>

        {/* Movie Title */}
        <h1 className="movie-title">{movie.title}</h1>

        {/* Movie Video */}
        <video className="movie-video" controls>
          <source
            src={`http://localhost:5000${movie.videoUrl}`} // Use the correct URL for the uploaded video
            type="video/mp4"
          />
          Your browser does not support the video tag.
        </video>

        {/* Back Button */}
        <button className="back-button" onClick={() => navigate('/movies')}>
          Back to Movies
        </button>
      </div>
    </div>
  );
}

export default PlayMoviePage;