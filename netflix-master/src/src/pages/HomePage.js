import React,{useEffect, useRef} from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';
import ThemeToggleButton from '../components/ThemeToggleButton';

const HomePage = () => {
    const toggleButtonRef = useRef(null);

    useEffect(() => {
        // Programmatically click the toggle button to synchronize the background image
        if (toggleButtonRef.current) {
            toggleButtonRef.current.click();
        }
    }, []);
    return (
        <div className="homepage-background">
            <div className="homepage-content">
            <h1>Home Page</h1>
            <h2>Welcome to your movie world!</h2>
            <div>
                <Link to="/signin">
                    <button >Sign In</button>
                </Link>
                <Link to="/signup">
                    <button>Sign Up</button>
                </Link>
            </div>
            <ThemeToggleButton ref={toggleButtonRef} />
            </div>
        </div>
    );
}
export default HomePage;