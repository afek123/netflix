import React, { useState, useEffect } from "react";

const AddMovieForm = ({ handleSubmit }) => {
  const [categories, setCategories] = useState([]);
  const [title, setTitle] = useState("");
  const [director, setDirector] = useState("");
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [videoFile, setVideoFile] = useState(null);
  const [posterFile, setPosterFile] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");

  // Fetch categories from the backend
  useEffect(() => {
    fetch("http://localhost:5000/api/categories")
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP error! Status: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        if (Array.isArray(data)) {
          setCategories(data);
        } else {
          throw new Error("Invalid categories format");
        }
      })
      .catch((err) => {
        console.error("Error fetching categories:", err);
        setCategories([]); // Fallback to empty array
      });
  }, []);

  // Handle category selection
  const handleCategoryChange = (e) => {
    // Get selected values as an array of objects
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
  const onSubmit = async (e) => {
    e.preventDefault();

    if (!title || !director || selectedCategories.length === 0) {
      setErrorMessage("All fields are required!");
      return;
    }

    if (!videoFile || !posterFile) {
      setErrorMessage("Please select both a video file and a poster file!");
      return;
    }

    const formData = new FormData();
    formData.append("title", title);
    formData.append("director", director);

    // Send categories as a comma-separated string
    const categoryIds = selectedCategories.map((cat) => cat.id).join(",");
    formData.append("category", categoryIds);

    formData.append("videoUrl", videoFile);
    formData.append("posterUrl", posterFile);

    try {
      const response = await fetch("http://localhost:5000/api/movies", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) throw new Error("Failed to add the movie");

      const data = await response.json();
      handleSubmit(data); // Pass the newly created movie to the parent component

      // Reset form fields
      setTitle("");
      setDirector("");
      setSelectedCategories([]);
      setVideoFile(null);
      setPosterFile(null);
      setErrorMessage("");

      console.log("Movie added:", data);
    } catch (err) {
      console.error("Error:", err);
      setErrorMessage("Failed to add the movie. Please try again.");
    }
  };

  return (
    <div>
      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
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
          <label htmlFor="category">Categories:</label>
          <select
            id="category"
            multiple
            value={selectedCategories.map((cat) => cat.id)} // Use the IDs of the selected categories
            onChange={handleCategoryChange}
            style={{ width: "100%", height: "100px" }}
          >
            {categories.map((category) => (
              <option key={category._id} value={category._id}>
                {category.name}
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
          <input type="file" id="posterFile" onChange={handlePosterFileChange} required />
        </div>
        {posterFile && <p>Selected poster: {posterFile.name}</p>}
        <button type="submit">Add Movie</button>
      </form>
    </div>
  );
};

export default AddMovieForm;