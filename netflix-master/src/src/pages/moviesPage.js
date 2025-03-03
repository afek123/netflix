import React, { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchMovies, fetchCategories } from "../services/movieService";
import CategoryMovies from "../components/CategoryMovies";
import RandomMovie from "../components/RandomMovie";
import "../styles/styles.css"; // Import the common CSS file for MoviesPage
import ThemeToggleButton from "../components/ThemeToggleButton";

function HomePage() {
  const [categories, setCategories] = useState([]);
  const [movies, setMovies] = useState([]);
  const [randomMovie, setRandomMovie] = useState(null);
  const [profilePicture, setProfilePicture] = useState(null); // State for profile picture
  const navigate = useNavigate();
  const toggleButtonRef = useRef(null);

  // Default categories
  const defaultCategories = [
    { name: "Action", promoted: true },
    { name: "Sci-Fi", promoted: false },
  ];

  // Default movies
  const defaultMovies = [
    {
      title: "Inception",
      director: "Christopher Nolan",
      categoryIds: [], // Will be populated with category IDs
      videoFile: "/videos/inception.mp4", // Path to the video file in the public folder
      posterFile: "/posters/inception-poster.jpg", // Path to the poster file in the public folder
    },
    {
      title: "The Dark Knight",
      director: "Christopher Nolan",
      categoryIds: [], // Will be populated with category IDs
      videoFile: "/videos/dark-knight.mp4", // Path to the video file in the public folder
      posterFile: "/posters/dark-knight-poster.jpg", // Path to the poster file in the public folder
    },
  ];

  // Function to fetch a file from the public folder and convert it to a File object
  const fetchFileAsBlob = async (filePath) => {
    try {
      const response = await fetch(filePath);
      if (!response.ok) throw new Error(`Failed to fetch file: ${filePath}`);
      const blob = await response.blob();
      return new File([blob], filePath.split("/").pop(), { type: blob.type });
    } catch (err) {
      console.error("Error fetching file:", err);
      return null;
    }
  };

  // Function to add default categories
  const addDefaultCategories = async () => {
    try {
      const response = await fetch("http://localhost:5000/api/categories");
      if (!response.ok) throw new Error("Failed to fetch categories from the server");

      const existingCategories = await response.json();

      const categoriesToAdd = defaultCategories.filter((category) => {
        return !existingCategories.some((existingCategory) => existingCategory.name === category.name);
      });

      if (categoriesToAdd.length > 0) {
        for (const category of categoriesToAdd) {
          const addResponse = await fetch("http://localhost:5000/api/categories", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(category),
          });

          if (!addResponse.ok) throw new Error("Failed to add default category");
        }

        console.log("Default categories added successfully!");
      } else {
        console.log("Default categories already exist on the server.");
      }
    } catch (err) {
      console.error("Error adding default categories:", err);
    }
  };

  // Function to add default movies
  const addDefaultMovies = async (categories) => {
    console.log("addDefaultMovies function called"); // Debugging log
    try {
      const response = await fetch("http://localhost:5000/api/movies");
      if (!response.ok) throw new Error("Failed to fetch movies from the server");

      const existingMovies = await response.json();

      const moviesToAdd = defaultMovies.filter((movie) => {
        return !existingMovies.some((existingMovie) => existingMovie.title === movie.title);
      });

      if (moviesToAdd.length > 0) {
        for (const movie of moviesToAdd) {
          // Fetch video and poster files as File objects
          const videoFile = await fetchFileAsBlob(movie.videoFile);
          const posterFile = await fetchFileAsBlob(movie.posterFile);

          if (!videoFile || !posterFile) {
            console.error("Failed to fetch video or poster file for movie:", movie.title);
            continue;
          }

          // Populate category IDs for the movie
          const categoryIds = categories
            .filter((cat) => defaultCategories.some((defaultCat) => defaultCat.name === cat.name))
            .map((cat) => cat._id);

          if (categoryIds.length === 0) {
            console.error("No category IDs found for movie:", movie.title);
            continue;
          }

          const formData = new FormData();
          formData.append("title", movie.title);
          formData.append("director", movie.director);

          // Send category IDs as a comma-separated string
          formData.append("category", categoryIds.join(","));

          // Append files to FormData
          formData.append("videoUrl", videoFile, videoFile.name); // Add filename for clarity
          formData.append("posterUrl", posterFile, posterFile.name); // Add filename for clarity

          // Log FormData for debugging
          for (const [key, value] of formData.entries()) {
            console.log(key, value);
          }

          const addResponse = await fetch("http://localhost:5000/api/movies", {
            method: "POST",
            body: formData,
          });

          if (!addResponse.ok) {
            const errorData = await addResponse.json();
            console.error("Failed to add default movie:", errorData);
            throw new Error("Failed to add default movie");
          }

          console.log("Movie added successfully:", movie.title);
        }

        console.log("Default movies added successfully!");
      } else {
        console.log("Default movies already exist on the server.");
      }
    } catch (err) {
      console.error("Error adding default movies:", err);
    }
  };

  useEffect(() => {
    // Programmatically click the toggle button to synchronize the background image
    if (toggleButtonRef.current) {
      toggleButtonRef.current.click();
    }
  }, []);

  useEffect(() => {
    const getData = async () => {
      try {
        console.log("Fetching data..."); // Debugging log

        // Add default categories if they don't already exist
        const hasDefaultCategoriesBeenAdded = localStorage.getItem("defaultCategoriesAdded");
        if (!hasDefaultCategoriesBeenAdded) {
          console.log("Adding default categories..."); // Debugging log
          await addDefaultCategories();
          localStorage.setItem("defaultCategoriesAdded", "true");
        }

        // Fetch categories and movies
        const [categoriesData, moviesData] = await Promise.all([
          fetchCategories(),
          fetchMovies(),
        ]);
        setCategories(categoriesData);
        setMovies(moviesData);

        // Add default movies if they don't already exist
        const hasDefaultMoviesBeenAdded = localStorage.getItem("defaultMoviesAdded");
        if (!hasDefaultMoviesBeenAdded) {
          console.log("Adding default movies..."); // Debugging log
          await addDefaultMovies(categoriesData); // Pass fetched categories
          localStorage.setItem("defaultMoviesAdded", "true");
        }

        // Select a random movie
        if (moviesData.length > 0) {
          const randomIndex = Math.floor(Math.random() * moviesData.length);
          setRandomMovie(moviesData[randomIndex]);
        }

        // Fetch user profile picture
        const userId = localStorage.getItem("userId");
        if (userId) {
          const response = await fetch(`http://localhost:5000/api/users/${userId}`);
          if (!response.ok) throw new Error("Failed to fetch user details");
          const userData = await response.json();
          setProfilePicture(`http://localhost:5000${userData.picture}`);
          localStorage.setItem("profilePicture", `http://localhost:5000${userData.picture}`);
        }
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    };

    getData();
  }, []); // Ensure this runs only once on mount

  return (
    <div className="content">
      {/* Profile Picture */}
      {profilePicture && (
        <div className="profile-preview-container">
          <img src={profilePicture} alt="Profile Preview" className="profile-preview-movie" />
        </div>
      )}
  
  
      {/* Random Movie Section */}
      {randomMovie && <RandomMovie movie={randomMovie} />}
  
      {/* Categories */}
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
  
      {/* Navigation Buttons */}
      <div className="navigation-buttons">
        <button onClick={() => navigate("/")}>logout</button>
        <button onClick={() => navigate("/manager")}>Go to Manager Page</button>
        <button onClick={() => navigate("/search")}>Go to Search Page</button>
      </div>
  
      {/* Theme Toggle Button */}
      <ThemeToggleButton ref={toggleButtonRef} />
    </div>
  );
}

export default HomePage;