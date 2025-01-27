export const addMovieToWatched = async (userId, movieId) => {
    const response = await fetch('http://localhost:5000/api/add-movie-to-watched', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ userId, movieId }),
    });
  
    if (!response.ok) {
      throw new Error('Failed to add movie to watchlist');
    }
  
    const data = await response.json();
    return data;
  };