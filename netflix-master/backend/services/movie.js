const Movie = require('../models/movie');
const User = require('../models/users');
const Category = require('../models/category');
const { deleteRecommendation } = require('../services/recommendation');
// Create a new movie
const createMovie = async (movieData) => {
    const categoryExists = await Category.findById(movieData.category);
    if (!categoryExists) return null;
    const movie = new Movie(movieData);
    await movie.save();
    return movie;
};

// Get for each promoted category 20 random movies that are not in the user's watchlist, and the last 20 movies watched by the user in random order.
const getMovies = async (userId) => {
    const user = await User.findById(userId);
    if (!user) return null;
    const userWatchlist = user.watchlist || [];
    const userPromotedCategories = await Category.find({promoted: true});
    const promotedCategories = await Promise.all(userPromotedCategories.map(async (category) => {
        const movies = await Movie.aggregate([
            { $match: { category: category._id , _id: { $nin: userWatchlist } } },
            { $sample: { size: 20 } },
            { $project: { _id: 1, title: 1} }
        ]);
        if (movies.length > 0)
            return { category: category.name, movies };
        return null;

    }
    ));
    const filteredPromotedCategories = promotedCategories.filter(category => category !== null);
    const lastWatched = await Movie.aggregate([
        { $match: { _id: { $in: userWatchlist} } },
        { $sort: {watchedAt: -1} },
        { $limit: 20 },
        { $sample: { size: 20 } },
        { $project: { _id: 1, title: 1} }
    ]);
    return { promotedCategories:filteredPromotedCategories,lastWatched };

};
// Get movie by ID
const getMovieById = async (id) => {
    return await Movie.findById(id);
};

// Update a movie
const updateMovie = async (id, movieData) => {
    const movie = await getMovieById(id);
    if (!movie) return null;
    const categoryExists = await Category.findById(movieData.category);
    if (!categoryExists) return err;
    movie.set(movieData);
    await movie.save();
    return movie;
}

// Delete a movie
const deleteMovie = async (id) => {
    // Find the movie by ID
    const movie = await getMovieById(id);
    if (!movie) return null;

    // Get all users who have watched this movie
    const usersWhoWatched = movie.watchedBy;

    // Remove the movie reference from the User watchlist and watchedBy list
    await User.updateMany(
        { _id: { $in: usersWhoWatched } },  // Find users who have watched the movie
        { $pull: { watchlist: id, watchedMovies: id } }  // Remove movie from watchlist and watchedMovies array (if applicable)
    );

    // Optionally, also delete the movie from the recommendation system for each user who watched it
    try {
        for (let userId of usersWhoWatched) {
            await deleteRecommendation(userId, id);  // Pass both userId and movieId to the recommendation service
            console.log(`Successfully deleted movie ${id} from recommendation system for user ${userId}`);
        }
    } catch (err) {
        console.error(`Error deleting movie ${id} from recommendation system:`, err.message);
        // You can decide how to handle errors here, e.g., log it or rollback changes.
    }

    // Delete the movie document from the database
    await movie.deleteOne();
    return movie;
};

const searchMovies = async (query) => {
    const regexQuery = { $regex: query, $options: 'i' };
    const movies = await Movie.find({
        $or: [
            { title: regexQuery },
            { description: regexQuery },
            { listOfActors: regexQuery },
            { category: { $in: await Category.find({ name: regexQuery }).select('_id') } },
            { director: regexQuery },
            { releaseYear: isNaN(query) ? regexQuery : Number(query) },
            { rating: isNaN(query) ? undefined : Number(query) },
            { Comments: regexQuery },
             { duration: isNaN(query) ? undefined : Number(query) },
             { watchedAt: isNaN(Date.parse(query)) ? undefined : new Date(query) }
            ].filter(condition => condition[Object.keys(condition)[0]] !== undefined)
    });
    return movies;
};
    

module.exports = { createMovie, getMovies, getMovieById, updateMovie, deleteMovie , searchMovies};