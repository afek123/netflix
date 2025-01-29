import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';
import ThemeToggleButton from '../components/ThemeToggleButton';

const HomePage = () => {
    return (
        <div className="HomePage">
            <h1>Home Page</h1>
            <h2>Welcome to your movie world!</h2>
            <div>
                <Link to="/signin">
                    <button>Sign In</button>
                </Link>
                <Link to="/signup">
                    <button>Sign Up</button>
                </Link>
            </div>
            <ThemeToggleButton />
        </div>
    );
}
export default HomePage;