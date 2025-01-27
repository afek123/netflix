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
  const movie = await response.json();
  return movie;
};