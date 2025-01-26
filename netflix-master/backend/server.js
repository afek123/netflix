const express = require('express');
const cors = require('cors');
const path = require('path');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const customEnv = require('custom-env');
const multer = require('multer');

const app = express();
const port = process.env.PORT || 5000;

// Load environment variables
const environment = process.env.NODE_ENV || 'default';
customEnv.env(environment, './config');

// Middleware
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json({ limit: '4mb' }));
app.use(express.json());

// Serve static files
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Connect to MongoDB
mongoose
  .connect(process.env.CONNECTION_STRING || 'mongodb://127.0.0.1:27017/netflix', {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => console.log('MongoDB connected successfully'))
  .catch((err) => console.error('MongoDB connection error:', err));




// Movie routes
const movieRoutes = require('./routes/movie');
app.use('/api/movies', movieRoutes);

// Category routes
const categoryRoutes = require('./routes/category');
app.use('/api/categories', categoryRoutes);

// Root health check endpoint
app.get('/', (req, res) => {
  res.send('Server is running');
});



// Start server
app.listen(port, () => {
  console.log(`Server is running on port ${port} in ${environment} mode`);
});
