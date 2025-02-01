import { BrowserRouter as Router } from 'react-router-dom';
import AppRouter from './routes/AppRouter';
import ThemeProvider from './ThemeContext';
import ThemeToggle from './components/ThemeToggle';
import './styles/App.css';

const App = () => {
  return (
    <ThemeProvider>
      <Router>
        <div className="App">
          <ThemeToggle />
          <AppRouter />
        </div>
      </Router>
    </ThemeProvider>
  );
};

export default App;
