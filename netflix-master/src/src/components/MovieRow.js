import React, { useRef, useState, useEffect } from 'react';
import MovieItem from './MovieItem'; // Make sure to use the MovieItem component
import '../styles/MovieRow.css';

const MovieRow = ({ title, movies, rowId }) => {
  const rowRef = useRef(null);
  const [showLeft, setShowLeft] = useState(false);
  const [showRight, setShowRight] = useState(true);

  const handleScroll = () => {
    const el = rowRef.current;
    setShowLeft(el.scrollLeft > 0);
    setShowRight(el.scrollLeft + el.clientWidth < el.scrollWidth);
  };

  const scrollLeft = () => {
    rowRef.current.scrollBy({ left: -300, behavior: 'smooth' });
  };

  const scrollRight = () => {
    rowRef.current.scrollBy({ left: 300, behavior: 'smooth' });
  };

  useEffect(() => {
    handleScroll();
  }, [movies]);

  return (
    <div className="movie-row-container">
      <h2 className="category-title">{title}</h2>
      <div className="movie-row-wrapper">
        {/* Only show left button if needed */}
        {showLeft && (
          <button className="scroll-button left" onClick={scrollLeft}>
            &#10094;
          </button>
        )}
        <div
          className="movie-row"
          id={rowId}
          onScroll={handleScroll}
          ref={rowRef}
        >
          {movies.map((movie) => (
            <MovieItem key={movie._id} movie={movie} />
          ))}
        </div>
        {/* Only show right button if needed */}
        {showRight && (
          <button className="scroll-button right" onClick={scrollRight}>
            &#10095;
          </button>
        )}
      </div>
    </div>
  );
};

export default MovieRow;
