import React from 'react';
import { Routes, Route } from 'react-router-dom';
import HomePage from '../pages/HomePage';
import MoviePage from '../pages/MoviePage';
import PlayMoviePage from '../pages/PlayMoviePage';

function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/movie/:id" element={<MoviePage />} />
      <Route path="/play/:id" element={<PlayMoviePage/>} />

    </Routes>
  );
}

export default AppRouter;
