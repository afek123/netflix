import React from "react";
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

  React.useEffect(() => {
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
          // Fetch from API if not in local storage
          // const profilePictureData = await fetchProfilePicture();
          // setProfilePicture(profilePictureData);
        }
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    };
    getData();
  }, []);

  return (
    <div className="content">
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
      <ThemeToggleButton />
    </div>
  );
}

export default HomePage;