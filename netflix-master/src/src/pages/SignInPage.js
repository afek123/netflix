import React, { useEffect,useState,useRef } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/SignInPage.css';
import ThemeToggleButton from '../components/ThemeToggleButton';
const SignInPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const toggleButtonRef = useRef(null);

    useEffect(() => {
        // Programmatically click the toggle button to synchronize the background image
        if (toggleButtonRef.current) {
            toggleButtonRef.current.click();
        }
    }, []);
    const handleSignIn = (e) => {
        e.preventDefault();
        if (!username || !password) {
            alert('Please enter a valid username and password');
            return;
        }
        if (!username) {
            alert('Please enter a username');
            return;
        }
        if (!password) {
            alert('Please enter a password');
            return;
        }
        axios.post('http://localhost:5000/api/login', { username, password })
            .then((res) => {
                const { token ,userId,role} = res.data;
                localStorage.setItem('jwt', token);
                localStorage.setItem('userId', userId); // Store the user ID
                localStorage.setItem('role', role); // Store the user role

                console.log('Signed in');
                
                navigate('/movies');
            })
            .catch((err) => {
                console.log(err);
                alert('Invalid username or password');
            });
    };

    return (
        <div>
            <h1>Sign In</h1>
            <form onSubmit={handleSignIn}>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">Sign In</button>
            </form>
            <button onClick={() => navigate('/signup')}>new to NETFLIX? sign up now!</button>
            <ThemeToggleButton ref={toggleButtonRef} />
        </div>
    );
};

export default SignInPage;
