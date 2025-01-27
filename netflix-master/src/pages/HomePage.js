import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';

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
            <audio autoPlay loop>
                <source src="/mixkit-intro-transition-1146.wav" type="audio/wav" />
            </audio>
        </div>
    );
}

export default HomePage;