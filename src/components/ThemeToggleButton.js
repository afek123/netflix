import React, { useContext, forwardRef } from 'react';
import { ThemeContext } from '../ThemeContext';


const ThemeToggleButton = forwardRef((props, ref) => {
    const { theme, toggleTheme } = useContext(ThemeContext);

    return (
        <button
            ref={ref}
            className="theme-toggle-button"
            onClick={toggleTheme}
        >
            Switch to {theme === 'dark' ? 'light' : 'dark'} Mode
        </button>
    );
});

export default ThemeToggleButton;