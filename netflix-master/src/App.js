import React from 'react';
import AppRouter from './routes/AppRouter';
import ThemeProvider from './ThemeContext';
import './styles/App.css';
const App = () => {
    return (
        <ThemeProvider>
            <AppRouter />
        </ThemeProvider>
    );
};

export default App;