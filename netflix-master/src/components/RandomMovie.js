import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/styles.css'; // Import the CSS file

function RandomMovie({ movie }) {
  return (
    <div className="random-movie">
      <video className="random-movie-video" controls>
        <source src={movie.videoUrl} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
      <div className="random-movie-details">
        <h2>{movie.title}</h2>
        <Link to={`/movie/${movie._id}`} className="random-movie-button">See Details</Link>
      </div>
    </div>
  );
}

export default RandomMovie;