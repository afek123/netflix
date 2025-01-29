import React from "react";

const MovieCard = ({ movie, categories, handleDelete }) => {
  return (
    <div
      className="movieCard"
      onClick={() => handleDelete(movie._id, movie.title)}
    >
      <div className="cardContent">
        <img
          src={
            movie.posterUrl
              ? `http://localhost:5000${movie.posterUrl}` // Use the correct URL for the uploaded poster
              : `https://via.placeholder.com/150x225?text=${movie.title}`
          }
          alt={movie.title || "Untitled"}
          className="poster"
        />
        <div className="movieInfo">
          <h3 className="movieTitle">{movie.title}</h3>
          <h3 className="movieDirector">
            Director: {movie.director || "Unknown"}
          </h3>
          <p className="movieCategory">
            Category: {categories[movie.category] || "Unknown"}
          </p>
        </div>
      </div>
    </div>
  );
};

export default MovieCard;
