import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../MovieList.css';

function MovieList({ movies }) {
  const navigate = useNavigate();

  const handleDetailsClick = (_id) => {
    navigate(`/movie/${_id}`);
  };

  return (
    <div className="movie-list">
      <h2>Movie List</h2>
      {movies.map(movie => (
        <div 
          key={movie._id} 
          className="movie-item"
          onMouseEnter={() => document.getElementById(`video-${movie._id}`).play()}
          onMouseLeave={() => document.getElementById(`video-${movie._id}`).pause()}
        >
          <video
            id={`video-${movie._id}`}
            className="movie-video"
            src={movie.videoUrl} // Video URL
            muted
            loop
          ></video>
          <div className="movie-info">
            <h3>{movie.title}</h3>
            <button 
              className="details-button" 
              onClick={() => handleDetailsClick(movie._id)}
            >
              See Details
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}

export default MovieList;
