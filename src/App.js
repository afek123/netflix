import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import AppRouter from './routes/AppRouter';
import './styles/App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <AppRouter />
      </div>
    </Router>
  );
}

export default App;
