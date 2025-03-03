import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const MovieInfo = () => {
  const { movieId } = useParams();
  const [movie, setMovie] = useState(null);
  const [recommendedMovies, setRecommendedMovies] = useState([]);

  useEffect(() => {
    const fetchMovieInfo = async () => {
      const response = await fetch(`http://foo.com/api/movies/${movieId}`);
      const data = await response.json();
      setMovie(data.movie);
      setRecommendedMovies(data.recommendedMovies);
    };

    fetchMovieInfo();
  }, [movieId]);

  if (!movie) return <div>Loading...</div>;

  return (
    <div>
      <h1>{movie.title}</h1>
      <p>{movie.description}</p>
      <h3>Recommended Movies</h3>
      <ul>
        {recommendedMovies.map((recommendedMovie) => (
          <li key={recommendedMovie.id}>{recommendedMovie.title}</li>
        ))}
      </ul>
    </div>
  );
};

export default MovieInfo;
