import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/SignInPage.css';

const SignInPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

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
                const { token } = res.data;
                localStorage.setItem('jwt', token);
                console.log('Signed in');
                navigate('/mainPage');
            })
            .catch((err) => {
                console.log(err);
                alert('Invalid username or password');
            });
    };

    return (
        <div className="container">
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
        </div>
    );
};

export default SignInPage;
