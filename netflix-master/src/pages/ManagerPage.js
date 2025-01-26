import React, { useState, useEffect } from "react";
import { fetchMovies, fetchCategories, addMovie, updateMovie, deleteMovie, deleteCategory } from "../services/movieService";
import ModeSelector from "../components/ModeSelector";
import AddMovieForm from "../components/AddMovieForm";
import DeleteMovieList from "../components/DeleteMovieList";
import UpdateMovieForm from "../components/UpdateMovieForm";
import AddCategoryForm from "../components/AddCategoryForm"; 
import FormResetter from "../components/FormResetter"; 
import DeleteCategoryForm from "../components/DeleteCategoryForm";
import UpdateCategoryForm from "../components/UpdateCategoryForm";
import "./ManagerPage.css";

function ManagerPage() {
  const [mode, setMode] = useState("add");
  const [title, setTitle] = useState("");
  const [director, setDirector] = useState("");
  const [category, setCategory] = useState("");
  const [videoUrl, setVideoUrl] = useState("");
  const [posterUrl, setPosterUrl] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [categories, setCategories] = useState({});
  const [movies, setMovies] = useState([]);
  const [selectedMovie, setSelectedMovie] = useState(null);

  // Fetch Movies and Categories when the component mounts
  useEffect(() => {
    const getCategories = async () => {
      try {
        const categoriesData = await fetchCategories();
        const categoryMap = categoriesData.reduce((map, category) => {
          map[category._id] = category.name;
          return map;
        }, {});
        setCategories(categoryMap);
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      }
    };

    getCategories();
  }, []);

  // Fetch Movies when the component mounts
  useEffect(() => {
    const getMovies = async () => {
      try {
        const moviesData = await fetchMovies();
        setMovies(moviesData);
      } catch (error) {
        console.error("Failed to fetch movies:", error);
      }
    };

    getMovies();
  }, []);

  const handleAddCategory = (newCategory) => {
    setCategories((prevCategories) => ({
      ...prevCategories,
      [newCategory._id]: newCategory.name,
    }));
  };

  const handleDeleteCategory = async (categoryId) => {
      await deleteCategory(categoryId);
      setCategories((prevCategories) => {
        const newCategories = { ...prevCategories };
        delete newCategories[categoryId];
        return newCategories;
      });
      alert("Category deleted successfully!");
  };

  const handleAddSubmit = async (e) => {
    const newMovie = { title, director, category, videoUrl, posterUrl };
    try {
      const createdMovie = await addMovie(newMovie);
      setMovies([...movies, createdMovie]); // Update movies state with new movie
      resetForm();
    } catch (error) {
      setErrorMessage("An error occurred while adding the movie.");
    }
  };

  const handleUpdateSubmit = async (movieId, updatedMovie) => {
    // Make the PUT request to update the movie
    await updateMovie(movieId, updatedMovie);
    // Optimistically update the UI
    setMovies((prevMovies) =>
      prevMovies.map((movie) =>
        movie._id === movieId ? { ...movie, ...updatedMovie } : movie
      )
    );

    alert("Movie updated successfully!");
    resetForm();
    setMode("add");
  };
  
  const handleDelete = async (id, title) => {
    try {
      await deleteMovie(id);
      setMovies(movies.filter((movie) => movie._id !== id));
      alert(`Movie "${title}" deleted successfully!`);
    } catch (error) {
      console.error("Error deleting movie:", error);
    }
  };
  const handleUpdateCategory = async (categoryId, updatedData) => {
      await fetch(`http://localhost:5000/api/categories/${categoryId}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedData),
      });
  
      // Optimistically update the UI
      setCategories((prevCategories) => ({
        ...prevCategories,
        [categoryId]: updatedData.name || prevCategories[categoryId],
      }));
  
      alert("Category updated successfully!");
    
  };
  

  const handleEdit = (movie) => {
    setSelectedMovie(movie);
    setMode("update");
  };

  const resetForm = () => {
    setTitle("");
    setDirector("");
    setCategory("");
    setVideoUrl("");
    setPosterUrl("");
    setErrorMessage("");
    setSelectedMovie(null);
  };

  return (
    <div className="managerPage">
      <ModeSelector
        mode={mode}
        setMode={(newMode) => {
          setMode(newMode);
          resetForm();
        }}
      />

      <FormResetter 
        setTitle={setTitle} 
        setDirector={setDirector} 
        setCategory={setCategory} 
        setVideoUrl={setVideoUrl} 
        setPosterUrl={setPosterUrl} 
        setErrorMessage={setErrorMessage} 
        setSelectedMovie={setSelectedMovie} 
      />

      {/* Add Category Form Section */}
      {mode === "addCategory" && (
        <AddCategoryForm onCategoryAdded={handleAddCategory} />
      )}
      {/* Delete Category List Section */}
      {mode === "deleteCategory" && (
        <DeleteCategoryForm
          categories={categories}
          handleDeleteCategory={handleDeleteCategory}
        />
      )}
       {/* Update Category Form Section */}
       {mode === "updateCategory" && (
        <UpdateCategoryForm
          categories={categories}
          handleUpdateCategory={handleUpdateCategory}
        />
      )}

      {/* Add Movie Form Section */}
      {mode === "add" && (
        <AddMovieForm
          title={title}
          director={director}
          category={category}
          videoUrl={videoUrl}
          posterUrl={posterUrl}
          categories={categories}
          setTitle={setTitle}
          setDirector={setDirector}
          setCategory={setCategory}
          setVideoUrl={setVideoUrl}
          setPosterUrl={setPosterUrl}
          handleSubmit={handleAddSubmit}
          errorMessage={errorMessage}
        />
      )}

      {/* Update Movie Form Section */}
      {mode === "update" && (
        <UpdateMovieForm
          movies={movies}
          categories={categories}
          handleUpdate={handleUpdateSubmit}
        />
      )}

      {/* Delete Movie List Section */}
      {mode === "delete" && (
        <DeleteMovieList
          movies={movies}
          categories={categories}
          handleDelete={handleDelete}
          handleEdit={handleEdit}
        />
      )}
    </div>
  );
}

export default ManagerPage;
