import React, { useState } from 'react';

const AddMovieForm = ({
  title,
  director,
  category,
  videoUrl,
  categories,
  setTitle,
  setDirector,
  setCategory,
  setVideoUrl,
  setPosterUrl,
  handleSubmit,
  errorMessage,
}) => {
  const [posterFile, setPosterFile] = useState(null); // State for the selected file

  // Handle file selection
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setPosterFile(file); // Set the file in the state
    }
  };

  // Ensure categories is an object before calling Object.entries()
  const categoryOptions = categories ? Object.entries(categories) : [];

  // Submit handler
  const onSubmit = async (e) => {
    e.preventDefault();
  
    const formData = new FormData();
    formData.append('title', title);
    formData.append('director', director);
    formData.append('category', category);
    formData.append('videoUrl', videoUrl);
    if (posterFile) formData.append('posterUrl', posterFile); // Make sure 'poster' matches multer's field name
  
    try {
      const response = await fetch('http://localhost:5000/api/movies', {
        method: 'POST',
        body: formData, // Send FormData directly
      });
  
      if (!response.ok) throw new Error('Failed to add the movie');
      const data = await response.json();
      console.log('Movie added:', data);
    } catch (err) {
      console.error('Error:', err);
    }
  };
  
  return (
    <>
      {errorMessage && <p className="error">{errorMessage}</p>}
      <form onSubmit={onSubmit}>
        <div>
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="director">Director:</label>
          <input
            type="text"
            id="director"
            value={director}
            onChange={(e) => setDirector(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="category">Category:</label>
          <select
            id="category"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            required
          >
            <option value="">Select a category</option>
            {categoryOptions.map(([id, name]) => (
              <option key={id} value={id}>
                {name}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label htmlFor="videoUrl">Video URL:</label>
          <input
            type="text"
            id="videoUrl"
            value={videoUrl}
            onChange={(e) => setVideoUrl(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="posterFile">Poster File:</label>
          <input type="file" id="posterFile" onChange={handleFileChange} />
        </div>
        {posterFile && <p>Selected file: {posterFile.name}</p>}
        <button type="submit">Add Movie</button>
      </form>
    </>
  );
};

export default AddMovieForm;
