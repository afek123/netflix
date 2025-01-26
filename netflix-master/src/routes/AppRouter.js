import React from 'react';
import { Routes, Route } from 'react-router-dom';
import HomePage from '../pages/HomePage';
import MoviePage from '../pages/MoviePage';
import PlayMoviePage from '../pages/PlayMoviePage';
import SearchPage from '../pages/Searchpage';
import ManagerPage from '../pages/ManagerPage';  // Import the ManagerPage

function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/movie/:id" element={<MoviePage />} />
      <Route path="/play/:id" element={<PlayMoviePage/>} />
      <Route path="/search" element={<SearchPage />} />
      <Route path="/manager" element={<ManagerPage />} />  {/* Add the ManagerPage route */}
    </Routes>
  );
}

export default AppRouter;