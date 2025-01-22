import React from 'react';
import { Link } from 'react-router-dom';

function MovieCard({ movie }) {
  return (
    <div className="movie-card">
      <img src={movie.posterUrl} alt={movie.title} />
      <h3>{movie.title}</h3>
      <Link to={`/movie/${movie.id}`}>View Details</Link>
    </div>
  );
}

export default MovieCard;
