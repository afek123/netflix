const mongoose = require('mongoose');
const category = require('./category');
const { watch } = require('./users');
const main = require('custom-env/lib/main');
const Schema = mongoose.Schema;

const movieSchema = new Schema({
    title: {
        type: String,
        required: true
    },
    category: {
        type: [Schema.Types.ObjectId],
        ref: 'Category',
        default: []
    },
    videoUrl: { type: String, required: true },
    posterUrl: { type: String, required: true },
    director: {
        type: String,
    },
    
    description: {
        type: String,
    },
    releseYear: {
        type: Number,
    },
    watchedBy: [{
        type: [Schema.Types.ObjectId],
        ref: 'User',
        default: []
    }],
    duration: {
        type: Number
    },
    watchedAt: {
        type: Date,
        default: Date.now
    },
    rating: {
        type: Number,
        min: 0,
        max: 100
    },
    Comments: [{
        type: String
    }],
    promoted: {
        type: Boolean,
        default: false
    }
},{versionKey: false});
const Movie = mongoose.model('Movie', movieSchema);

module.exports = Movie;