export const addMovieToWatched = async (userId, movieId) => {
   const response = await fetch(`http://localhost:5000/api/movies/${userId}/${movieId}/recommend`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const data = await response.json();
    return data;
  };