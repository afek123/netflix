import React, { useState, useEffect } from "react";
import "../pages/ManagerPage.css";

const UpdateMovieForm = ({ movies, categories, handleUpdate }) => {
  const [selectedMovieId, setSelectedMovieId] = useState("");
  const [title, setTitle] = useState("");
  const [director, setDirector] = useState("");
  const [selectedCategories, setSelectedCategories] = useState([]); // Use an array for multiple categories
  const [videoFile, setVideoFile] = useState(null);
  const [posterFile, setPosterFile] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");

  // Handle movie selection
  const handleMovieSelect = (e) => {
    const movieId = e.target.value;
    setSelectedMovieId(movieId);

    const movie = movies.find((movie) => movie._id === movieId);
    if (movie) {
      setTitle(movie.title);
      setDirector(movie.director);
      setSelectedCategories(movie.category || []); // Set selected categories as an array
      setVideoFile(null);
      setPosterFile(null);
    } else {
      // Reset the form if no movie is selected
      setTitle("");
      setDirector("");
      setSelectedCategories([]);
      setVideoFile(null);
      setPosterFile(null);
    }
    setErrorMessage(""); // Clear any previous errors
  };

  // Handle category selection
  const handleCategoryChange = (e) => {
    const selectedOptions = Array.from(e.target.selectedOptions, (option) => ({
      id: option.value,
      name: option.text,
    }));
    setSelectedCategories(selectedOptions); // Update selected categories
  };

  // Handle video file selection
  const handleVideoFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setVideoFile(file);
    }
  };

  // Handle poster file selection
  const handlePosterFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setPosterFile(file);
    }
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!selectedMovieId) {
      setErrorMessage("Please select a movie to update.");
      return;
    }

    if (!title || !director || selectedCategories.length === 0) {
      setErrorMessage("Title, Director, and at least one Category are required.");
      return;
    }

    const formData = new FormData();
    formData.append("title", title);
    formData.append("director", director);
    selectedCategories.forEach((category) => formData.append("category", category.id)); // Append each category ID
    if (videoFile) formData.append("videoUrl", videoFile);
    if (posterFile) formData.append("posterUrl", posterFile);

    try {
      await handleUpdate(selectedMovieId, formData);

      // Clear form after successful update
      setTitle("");
      setDirector("");
      setSelectedCategories([]);
      setVideoFile(null);
      setPosterFile(null);
      setErrorMessage(""); // Clear errors after successful update
    } catch (err) {
      setErrorMessage("Failed to update the movie.");
    }
  };

  return (
    <div className="updateMovieForm">
      <h2>Update Movie</h2>
      {errorMessage && <p className="error">{errorMessage}</p>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="movieSelect">Select a Movie:</label>
          <select id="movieSelect" value={selectedMovieId} onChange={handleMovieSelect}>
            <option value="">-- Select a Movie --</option>
            {movies.map((movie) => (
              <option key={movie._id} value={movie._id}>
                {movie.title}
              </option>
            ))}
          </select>
        </div>

        {selectedMovieId && (
          <>
            <div className="form-group">
              <label htmlFor="title">Title:</label>
              <input
                type="text"
                id="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Enter movie title"
              />
            </div>
            <div className="form-group">
              <label htmlFor="director">Director:</label>
              <input
                type="text"
                id="director"
                value={director}
                onChange={(e) => setDirector(e.target.value)}
                placeholder="Enter director name"
              />
            </div>
            <div className="form-group">
              <label htmlFor="category">Categories:</label>
              <select
                id="category"
                multiple
                value={selectedCategories.map((cat) => cat.id)} // Use the IDs of the selected categories
                onChange={handleCategoryChange}
                style={{ width: "100%", height: "100px" }}
              >
                {Object.entries(categories).map(([id, name]) => (
                  <option key={id} value={id}>
                    {name}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="videoFile">Video File:</label>
              <input
                type="file"
                id="videoFile"
                onChange={handleVideoFileChange}
              />
            </div>
            {videoFile && <p>Selected video: {videoFile.name}</p>}
            <div className="form-group">
              <label htmlFor="posterFile">Poster File:</label>
              <input
                type="file"
                id="posterFile"
                onChange={handlePosterFileChange}
              />
            </div>
            {posterFile && <p>Selected poster: {posterFile.name}</p>}
            <button type="submit" className="btn-update">
              Update Movie
            </button>
          </>
        )}
      </form>
    </div>
  );
};

export default UpdateMovieForm;