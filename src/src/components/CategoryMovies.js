import React from "react";
import MovieRow from "./MovieRow";
import "../styles/categoryMovies.css"; // Make sure to use a specific CSS file for this component

function CategoryMovies({ category, movies }) {
  return (
    <div className="category-movies mb-4">
      <h2 className="category-title">{category.name}</h2>
      <MovieRow  movies={movies} rowId={category._id} />
    </div>
  );
}

export default CategoryMovies;
