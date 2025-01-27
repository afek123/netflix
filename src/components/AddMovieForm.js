import React, { useState } from 'react';

const AddMovieForm = ({
  title,
  director,
  category,
  categories,
  setTitle,
  setDirector,
  setCategory,
  handleSubmit,
  errorMessage,
}) => {
  const [videoFile, setVideoFile] = useState(null);   // State for the video file
  const [posterFile, setPosterFile] = useState(null); // State for the poster file

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

  // Ensure categories is an object before calling Object.entries()
  const categoryOptions = categories ? Object.entries(categories) : [];

  const onSubmit = async (e) => {
    e.preventDefault();
  
    const formData = new FormData();
    formData.append('title', title);
    formData.append('director', director);
    formData.append('category', category);
    if (videoFile) formData.append('videoUrl', videoFile);   // Corrected video field name
    if (posterFile) formData.append('posterUrl', posterFile); // Corrected poster field name
  
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
          <label htmlFor="videoFile">Video File:</label>
          <input type="file" id="videoFile" onChange={handleVideoFileChange} required />
        </div>
        {videoFile && <p>Selected video: {videoFile.name}</p>}
        <div>
          <label htmlFor="posterFile">Poster File:</label>
          <input type="file" id="posterFile" onChange={handlePosterFileChange} />
        </div>
        {posterFile && <p>Selected poster: {posterFile.name}</p>}
        <button type="submit">Add Movie</button>
      </form>
    </>
  );
};

export default AddMovieForm;
