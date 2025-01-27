import React, { useContext } from 'react';
import { ThemeContext } from '../ThemeContext';

const ThemeToggleButton = () => {
    const { theme, toggleTheme } = useContext(ThemeContext);

    return (
        <button className="theme-toggle-button" onClick={toggleTheme}>
            Switch to {theme === 'dark' ? 'light' : 'dark'} Mode
        </button>
    );
};

export default ThemeToggleButton;