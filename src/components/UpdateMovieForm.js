import React, { useState } from "react";
import "../pages/ManagerPage.css";

const UpdateMovieForm = ({ movies, categories, handleUpdate }) => {
  const [selectedMovieId, setSelectedMovieId] = useState("");
  const [title, setTitle] = useState("");
  const [director, setDirector] = useState("");
  const [category, setCategory] = useState("");
  const [videoUrl, setVideoUrl] = useState("");
  const [posterUrl, setPosterUrl] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleMovieSelect = (e) => {
    const movieId = e.target.value;
    setSelectedMovieId(movieId);

    const movie = movies.find((movie) => movie._id === movieId);
    if (movie) {
      setTitle(movie.title);
      setDirector(movie.director);
      setCategory(movie.category);
      setVideoUrl(movie.videoUrl);
      setPosterUrl(movie.posterUrl);
    } else {
      // Reset the form if no movie is selected
      setTitle("");
      setDirector("");
      setCategory("");
      setVideoUrl("");
      setPosterUrl("");
    }
    setErrorMessage(""); // Clear any previous errors
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!selectedMovieId) {
      setErrorMessage("Please select a movie to update.");
      return;
    }

    if (!title || !director || !category || !videoUrl || !posterUrl) {
      setErrorMessage("All fields are required.");
      return;
    }

    handleUpdate(selectedMovieId, { title, director, category, videoUrl, posterUrl });

    // Clear form after successful update
    setTitle("");
    setDirector("");
    setCategory("");
    setVideoUrl("");
    setPosterUrl("");
    setErrorMessage(""); // Clear errors after successful update
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
              <label htmlFor="category">Category:</label>
              <select
                id="category"
                value={category || ""}
                onChange={(e) => setCategory(e.target.value)}
              >
                <option value="">-- Select a Category --</option>
                {Object.entries(categories).map(([id, name]) => (
                  <option key={id} value={id}>
                    {name}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="videoUrl">Video URL:</label>
              <input
                type="text"
                id="videoUrl"
                value={videoUrl}
                onChange={(e) => setVideoUrl(e.target.value)}
                placeholder="Enter video URL"
              />
            </div>
            <div className="form-group">
              <label htmlFor="posterUrl">Poster URL:</label>
              <input
                type="text"
                id="posterUrl"
                value={posterUrl}
                onChange={(e) => setPosterUrl(e.target.value)}
                placeholder="Enter poster URL"
              />
            </div>
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
