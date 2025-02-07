import React,{useEffect, useRef}  from "react";
import { useNavigate } from "react-router-dom";
import { fetchMovies, fetchCategories } from "../services/movieService";
import CategoryMovies from "../components/CategoryMovies";
import RandomMovie from "../components/RandomMovie";
import '../styles/styles.css'; // Import the common CSS file for MoviesPage
import ThemeToggleButton from '../components/ThemeToggleButton';

function HomePage() {
  const [categories, setCategories] = React.useState([]);
  const [movies, setMovies] = React.useState([]);
  const [randomMovie, setRandomMovie] = React.useState(null);
  const [profilePicture, setProfilePicture] = React.useState(null); // State for profile picture
  const navigate = useNavigate();
  const toggleButtonRef = useRef(null);
  useEffect(() => {
    // Programmatically click the toggle button to synchronize the background image
    if (toggleButtonRef.current) {
        toggleButtonRef.current.click();
    }
}, []);
  useEffect(() => {
    const getData = async () => {
      try {
        const [categoriesData, moviesData] = await Promise.all([
          fetchCategories(),
          fetchMovies(),
        ]);
        setCategories(categoriesData);
        setMovies(moviesData);

        // Select a random movie
        if (moviesData.length > 0) {
          const randomIndex = Math.floor(Math.random() * moviesData.length);
          setRandomMovie(moviesData[randomIndex]);
        }
        const storedProfilePicture = localStorage.getItem('profilePicture');
        if (storedProfilePicture) {
          setProfilePicture(storedProfilePicture);
        } else {
          const userId = localStorage.getItem('userId');
          const response = await fetch(`http://localhost:5000/api/users/${userId}`);
          const userData = await response.json();
          setProfilePicture(`http://localhost:5000${userData.picture}`);
          localStorage.setItem('profilePicture', `http://localhost:5000${userData.picture}`);
        }
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    };
    getData();
  }, []);

  return (
    <div className="content">
      {profilePicture && (
        <div className="profile-preview-container">
          <img src={profilePicture} alt="Profile Preview" className="profile-preview-movie" />
        </div>
      )}
      {randomMovie && <RandomMovie movie={randomMovie} />}
      {categories.map((category) => {
        const categoryMovies = movies.filter((movie) =>
          movie.category.includes(category._id)
        );
        return (
          <CategoryMovies
            key={category._id}
            category={category}
            movies={categoryMovies}
          />
        );
      })}
      <div className="navigation-buttons">
        <button onClick={() => navigate("/")}>logout</button>
        <button onClick={() => navigate("/manager")}>Go to Manager Page</button>
        <button onClick={() => navigate("/search")}>Go to Search Page</button>
      </div>
      <ThemeToggleButton ref={toggleButtonRef} />
    </div>
  );
}

export default HomePage;