import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchMovieById } from '../services/movieService';

function MoviePage() {
  const { id } = useParams(); // Get movie ID from URL
  const [movie, setMovie] = useState(null); // State to store the current movie
  const navigate = useNavigate(); // For navigation

  useEffect(() => {
    const getMovieDetails = async () => {
      try {
        const movieData = await fetchMovieById(id); // Fetch movie by ID
        console.log('Fetched movie data:', movieData); // Debugging log
        setMovie(movieData); // Set movie data
      } catch (error) {
        console.error('Failed to fetch movie details:', error);
      }
    };

    getMovieDetails();
  }, [id]); // Re-run effect if the movie ID changes

  if (!movie) {
    return <h2>Loading movie details...</h2>;
  }

  console.log('Movie state:', movie); // Debugging log

  const handlePlayClick = () => {
    navigate(`/play/${movie._id}`); // Navigate to the play screen using useNavigate
  };

  return (
    <div>
      <h1>{movie.title}</h1>
      <p>{movie.director}</p>
      <button onClick={handlePlayClick}>Play Movie</button>
    </div>
  );
}

export default MoviePage;