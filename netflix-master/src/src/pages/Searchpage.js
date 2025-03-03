import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { fetchMovies, fetchCategories } from "../services/movieService";
import MovieCard from "../components/MovieCard"; // Import the MovieCard component
import './Searchpage.css';
import ThemeToggleButton from '../components/ThemeToggleButton';

const SearchPage = () => {
  const [searchQuery, setSearchQuery] = useState("");
  const [results, setResults] = useState([]);
  const [movies, setMovies] = useState([]);
  const [categories, setCategories] = useState({});
  const navigate = useNavigate(); // For navigation

  useEffect(() => {
    const getData = async () => {
      try {
        const [moviesData, categoriesData] = await Promise.all([
          fetchMovies(),
          fetchCategories(),
        ]);

        setMovies(moviesData);

        const categoryMap = categoriesData.reduce((map, category) => {
          map[category._id] = category.name;
          return map;
        }, {});
        setCategories(categoryMap);
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    };

    getData();
  }, []);

  const handleSearch = () => {
    if (searchQuery.trim() !== "") {
      const filteredResults = movies.filter((movie) =>
        movie.title.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setResults(filteredResults);
    } else {
      setResults([]);
    }
  };

  const handleMovieClick = (id) => {
    navigate(`/movie/${id}`); // Navigate to the MoviePage
  };

  return (
    <div className="pageContainer">
      <div className="container">
        <h1 className="header">Search Movies</h1>
        <div className="searchBar">
          <input
            type="text"
            placeholder="Enter movie name"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="input-search"
          />
          <button onClick={handleSearch} className="button">
            Search
          </button>
        </div>
        <div className="results">
          {results.length > 0 ? (
            <div className="grid">
              {results.map((result) => (
                <MovieCard
                  key={result._id}
                  movie={result}
                  categories={categories}
                  handleDelete={() => handleMovieClick(result._id)} // Use handleMovieClick for navigation
                />
              ))}
            </div>
          ) : (
            <p className="noResults">No results found</p>
          )}
        </div>
      </div>
      <ThemeToggleButton />
    </div>
  );
};

export default SearchPage;