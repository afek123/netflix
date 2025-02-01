import React, { createContext, useState, useEffect } from 'react';
import './styles/themes.css';
export const ThemeContext = createContext();

const ThemeProvider = ({ children }) => {
    const [theme, setTheme] = useState('dark');

    useEffect(() => {
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme) {
            setTheme(savedTheme);
        }
    }, []);

    useEffect(() => {
        document.body.className = theme;
         // Check if we're on the homepage
        if (window.location.pathname === '/') {
            if (theme === 'dark') {
                require('./styles/HomePage.css');
                document.body.style.backgroundImage = "url('/pictures/homeP.webp')";
            } else {
                require('./styles/BackGroundLightMode.css');
                document.body.style.backgroundImage = "url('/pictures/LightMode.webp')";
            }
        }
        else if (window.location.pathname === '/signin') {
            console.log('signin');
            if (theme === 'dark') {
                require('./styles/HomePage.css');
                document.body.style.backgroundImage = "url('/pictures/homeP.webp')";
            } else {
                require('./styles/BackGroundLightMode.css');
                document.body.style.backgroundImage = "url('/pictures/LightMode.webp')";
            }
        }
        else if (window.location.pathname === '/signup') {
            console.log('signup');
            if (theme === 'dark') {
                require('./styles/HomePage.css');
                document.body.style.backgroundImage = "url('/pictures/homeP.webp')";
            } else {
                require('./styles/BackGroundLightMode.css');
                document.body.style.backgroundImage = "url('/pictures/LightMode.webp')";
            }
        }
        else if (window.location.pathname === '/movies') {
            if (theme === 'dark') {
                require('./styles/MoviesDarkMode.css');
                document.body.style.backgroundImage = "url('/pictures/black.png')";

            } else {
                require('./styles/MoviesLightMode.css');
                document.body.style.backgroundImage = "url('/pictures/white.png')";
            }
        } else {
            if (theme === 'dark') {
                console.log('dark');
                require('./styles/HomePage.css');
            } else {
                console.log('light');
                require('./styles/BackGroundLightMode.css');
            }
        }
    }, [theme]);

    const toggleTheme = () => {
        const newTheme = theme === 'dark' ? 'light' : 'dark';
        setTheme(newTheme);
        localStorage.setItem('theme', newTheme);
    };

    return (
        <ThemeContext.Provider value={{ theme, toggleTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};

export default ThemeProvider;