import React from 'react';
import MovieItem from './MovieItem';
import '../styles/styles.css'; // Import the CSS file

function CategoryMovies({ category, movies }) {
  return (
    <div>
      <h2>{category.name}</h2>
      <div className="movie-list">
        {movies.map(movie => (
          <MovieItem key={movie._id} movie={movie} />
        ))}
      </div>
    </div>
  );
}

export default CategoryMovies;