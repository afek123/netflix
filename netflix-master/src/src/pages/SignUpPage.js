import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/SignUpPage.css';
import ThemeToggleButton from '../components/ThemeToggleButton';

const SignUpPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [name, setName] = useState('');
    const [picture, setPicture] = useState(null);
    const [preview, setPreview] = useState(null);
    const navigate = useNavigate();

    const handleSignUp = (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            alert('Passwords do not match');
            return;
        }
        if (password.length < 8) {
            alert('Password must be at least 8 characters long');
            return;
        }
        if (!username) {
            alert('Please enter a username');
            return;
        }
        if (!picture) {
            alert('Please upload a profile picture');
            return;
        }
        if (!name) {
            alert('Please enter a name to display');
            return;
        }
        const hasNumber = /\d/;
        const hasLetter = /[a-zA-Z]/;
        if (!hasNumber.test(password) || !hasLetter.test(password)) {
            alert('Password must contain at least one number and one letter');
            return;
        }

        const formData = new FormData();
        formData.append('username', username);
        formData.append('password', password);
        formData.append('name', name);
        formData.append('picture', picture);
        handleSignups(formData);
    };

    const handleSignups = async (formData) => {
        try {
            const res = await axios.post('http://localhost:5000/api/users', formData, { 
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            console.log('Signed up', res);
            navigate('/signin'); // Redirect to sign in page after successful signup
        } catch (err) {
            console.error('Error signing up', err);
            alert('Error signing up: ' + (err.response ? JSON.stringify(err.response.data) : err.message));
        }
    };

    const handlePreview = (e) => {
        const file = e.target.files[0];
        setPicture(file);
        setPreview(URL.createObjectURL(file));
    };

    return (
        <div >
            <h1>Sign Up</h1>
            <form onSubmit={handleSignUp}>
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
                <input
                    type="password"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Name to Display"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <input
                    type="file"
                    accept="image/*"
                    onChange={handlePreview}
                />
                {preview && <img src={preview} alt="Profile Preview" className="profile-preview" />}
                <button type="submit">Sign Up</button>
            </form>
            <button onClick={() => navigate('/signin')}>already have an account?</button>
            <ThemeToggleButton />
        </div>
    );
};

export default SignUpPage;