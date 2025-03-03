export const fetchMovies = async () => {
  const response = await fetch('http://localhost:5000/api/movies');
  if (!response.ok) {
    throw new Error('Failed to fetch movies');
  }
  const movies = await response.json();
  return movies;
};

export const fetchCategories = async () => {
const response = await fetch('http://localhost:5000/api/categories');
if (!response.ok) {
  throw new Error('Failed to fetch categories');
}
const categories = await response.json();
return categories;
};
export const fetchMovieById = async (id) => {
  const response = await fetch(`http://localhost:5000/api/movies/${id}`);
  if (!response.ok) {
    throw new Error('Failed to fetch movie');
  }
  return response.json();
};

export const fetchMovieRecommendations = async (userId, movieId) => {
  const response = await fetch(
    `http://localhost:5000/api/movies/${userId}/${movieId}/recommend`);

  if (!response.ok) {
    throw new Error('Failed to fetch recommendations');
  }

  return response.json();
};




export const addMovie = async (formData) => {
const response = await fetch("http://localhost:5000/api/movies", {
  method: "POST",
  body: formData, // Send FormData directly
});
if (!response.ok) throw new Error("Failed to add the movie.");
return await response.json();
};


export const updateMovie = async (movieId, formData) => {
  const response = await fetch(`http://localhost:5000/api/movies/${movieId}`, {
    method: "PUT",
    body: formData, // Send FormData directly
  });
  if (!response.ok) throw new Error("Failed to update the movie.");
  return await response.json();
};

export const deleteMovie = async (movieId) => {
const response = await fetch(`http://localhost:5000/api/movies/${movieId}`, {
  method: "DELETE",
});
if (!response.ok) throw new Error("Failed to delete the movie.");
};

export const deleteCategory = async (categoryId) => {

  console.log("Attempting to delete category with ID:", categoryId); // Log the categoryId
  await fetch(`http://localhost:5000/api/categories/${categoryId}`, {
    method: "DELETE",
  });
    return { message: "Category deleted successfully" };
  
};
