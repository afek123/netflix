import React from 'react';
import { Routes, Route } from 'react-router-dom';
import HomePage from '../pages/HomePage';
import PlayMoviePage from '../pages/PlayMoviePage';
import SearchPage from '../pages/Searchpage';
import ManagerPage from '../pages/ManagerPage';
import MoviesPage from "../pages/moviesPage";
import MoviePage from "../pages/MoviePage";
import SignIn from "../pages/SignInPage";
import SignUp from "../pages/SignUpPage";
function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/signin" element={<SignIn />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/movies" element={<MoviesPage />} />
      <Route path="/movie/:id" element={<MoviePage/>} />
      <Route path="/play/:id" element={<PlayMoviePage/>} />
      <Route path="/search" element={<SearchPage />} />
      <Route path="/manager" element={<ManagerPage />} />
    </Routes>
  );
}

export default AppRouter;
