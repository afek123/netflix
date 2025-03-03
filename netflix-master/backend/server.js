const express = require('express');
const cors = require('cors');
const path = require('path');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const multer = require('multer');
const customEnv = require('custom-env');
require('dotenv').config();

const app = express();
const port = process.env.PORT || 5000;

// Load environment variables
const environment = process.env.NODE_ENV || 'default';
customEnv.env(environment, './config');

// Middleware
app.use(cors());

app.use(express.json());

// Serve static files
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));
app.use('/profilePictures', express.static(path.join(__dirname, 'profilePictures')));

// Connect to MongoDB
const connectionString = process.env.CONNECTION_STRING || 'mongodb://127.0.0.1:27017/netflix';
mongoose
  .connect(connectionString, {
    serverSelectionTimeoutMS: 30000, // Increase timeout to 30 seconds
    socketTimeoutMS: 45000, // Increase socket timeout to 45 seconds
  })
  .then(() => console.log('MongoDB connected successfully'))
  .catch((err) => console.error('MongoDB connection error:', err));

const connection = mongoose.connection;
connection.once('open', () => {
  console.log('MongoDB database connection established successfully');
});

// Define routes
app.use('/api/movies', require('./routes/movie'));
app.use('/api/categories', require('./routes/category'));
app.use('/api', require('./routes/users'));


// Start server
app.listen(port, () => {
  console.log(`Server is running on port ${port} in ${environment} mode`);
});