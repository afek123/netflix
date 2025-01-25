const express = require('express');
const cors = require('cors');

const app = express();
const port = process.env.PORT || 5000;
require('custom-env').env(process.env.NODE_ENV, './config');
console.log(process.env.CONNECTION_STRING);
console.log(process.env.PORT);
const mongoose = require('mongoose');

mongoose.connect('mongodb://127.0.0.1:27017/netflix', {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});
// Middleware
app.use(cors());
app.use(express.json());

// Connect to MongoDB

const connection = mongoose.connection;
connection.once('open', () => {
  console.log('MongoDB database connection established successfully');
});

// Define routes
app.use('/api/movies', require('./routes/movie'));
app.use('/api/categories', require('./routes/category'));
app.use('/api', require('./routes/users')); // Ensure this line is present

app.listen(port, () => {
  console.log(`Server is running on port: ${port}`);
});