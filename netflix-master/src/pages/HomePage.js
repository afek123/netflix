import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom'; // Import Link component
import { fetchMovies, fetchCategories } from '../services/movieService';
import CategoryMovies from '../components/CategoryMovies';
import RandomMovie from '../components/RandomMovie';
import '../styles/styles.css'; // Import the CSS file

function HomePage() {
  const [categories, setCategories] = useState([]);
  const [movies, setMovies] = useState([]);
  const [randomMovie, setRandomMovie] = useState(null);

  useEffect(() => {
    const getData = async () => {
      try {
        const [categoriesData, moviesData] = await Promise.all([fetchCategories(), fetchMovies()]);
        setCategories(categoriesData);
        setMovies(moviesData);

        // Select a random movie
        if (moviesData.length > 0) {
          const randomIndex = Math.floor(Math.random() * moviesData.length);
          setRandomMovie(moviesData[randomIndex]);
        }
      } catch (error) {
        console.error('Failed to fetch data:', error);
      }
    };
    getData();
  }, []);

  return (
    <div className="content">
      {randomMovie && <RandomMovie movie={randomMovie} />}
      
      <h1>Movies</h1>

      {/* Navigation buttons */}
      <div className="navigation-buttons">
        <Link to="/manager">
          <button className="button">Go to Manager Page</button>
        </Link>
        <Link to="/search">
          <button className="button">Go to Search Page</button>
        </Link>
      </div>

      {categories.map(category => {
        const categoryMovies = movies.filter(movie => movie.category.includes(category._id));
        return <CategoryMovies key={category._id} category={category} movies={categoryMovies} />;
      })}
    </div>
  );
}

export default HomePage;
