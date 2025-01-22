import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchMovieById } from '../services/movieService';

function PlayMoviePage() {
  const { id } = useParams(); // Get movie ID from URL
  const [movie, setMovie] = useState(null); // State to store the current movie

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
    return <h2>Loading movie...</h2>;
  }

  return (
    <div>
      <h1>{movie.title}</h1>
      <video controls>
        <source src={movie.videoUrl} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
    </div>
  );
}

export default PlayMoviePage;