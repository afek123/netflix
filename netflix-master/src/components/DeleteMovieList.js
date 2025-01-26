import React from "react";
import MovieCard from "./MovieCard2"; // Import the MovieCard component

const DeleteMovieList = ({ movies = [], categories, handleDelete }) => {
  return (
    <div className="deleteContainer">
      <h2>Existing Movies</h2>
      {movies.length > 0 ? (
        <div className="grid">
          {movies.map((movie) => (
            <MovieCard
              key={movie._id}
              movie={movie}
              categories={categories}
              handleDelete={handleDelete}
            />
          ))}
        </div>
      ) : (
        <p>No movies available to delete.</p>
      )}
    </div>
  );
};

export default DeleteMovieList;
