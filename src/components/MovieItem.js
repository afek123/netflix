import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/styles.css'; // Import the CSS file

function MovieItem({ movie }) {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div
      className="movie-item"
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      {isHovered ? (
        <div className="hover-content">
          <video className="movie-video" controls>
            <source src={movie.videoUrl} type="video/mp4" />
            Your browser does not support the video tag.
          </video>
          <Link to={`/movie/${movie._id}`} className="see-details-button">See Details</Link>
        </div>
      ) : (
        <img src={movie.posterUrl} alt={movie.title} className="movie-poster" />
      )}
    </div>
  );
}

export default MovieItem;