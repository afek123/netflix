const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema({
    username: {
        type: String,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    },
    name: {
        type: String,
    },
    picture: {
        type: String
    },
    watchlist: {
        type: [Schema.Types.ObjectId],
        ref: 'Movie',
        default: []
    },
},{versionKey: false});
const User = mongoose.model('User', userSchema);

module.exports = User;