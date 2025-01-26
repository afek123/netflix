const Category = require('../models/category');
const Movie = require('../models/movie');
const createCategory = async (name, promoted) => {
    const category = new Category({ name : name, promoted: promoted});
    return await category.save();
};

const getCategoryById = async (id) => {
    return await Category.findById(id);
};

const getCategories = async () => {
    return await Category.find({});
};

const updateCategory = async (id, name, promoted) => {
    const category = await getCategoryById(id);
    if (!category) return null;

    if (name !== undefined) category.name = name;
    if (promoted !== undefined) category.promoted = promoted;

    await category.save();
    return category;
};

const deleteCategory = async (id) => {
    const category = await getCategoryById(id);
    if (!category) return null;
    // Remove the category reference from the Movie documents
    await Movie.updateMany(
    { category: id },
    { $unset: { category: "" } } // Remove the category field
);    await category.deleteOne();
    return category;
};

module.exports = {createCategory, getCategoryById, getCategories, updateCategory, deleteCategory}