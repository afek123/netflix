const CategoryService = require('../services/category');

const createCategory = async (req, res) => {
    try {
        if (!req.body.name) {
            return res.status(400).json({ error: ['Name is required'] });
        }
        
        // Await the creation of the category before sending a response
        await CategoryService.createCategory(req.body.name, req.body.promoted);

        // Send the response after successfully creating the category
        return res.status(201).send();
    } catch (err) {
        return res.status(400).json({ error: err.message });
    }
};


const getCategories = async (req, res) => {
    try {
        const categories = await CategoryService.getCategories();
        res.status(200).json(categories.map(category => {
            return {
                id: category._id,
                name: category.name,
                promoted: category.promoted
            };
        }
        ));
    }
    catch (err) {
        res.status(400).json({ error: err.message });
    }
};

const getCategory = async (req, res) => {
    try {
        const category = await CategoryService.getCategoryById(req.params.id);
        if (!category) {
            return res.status(404).json({ error: ['Category not found'] });
        }
        res.status(200).json({
            id: category._id,
            name: category.name,
            promoted: category.promoted
        });
    }
    catch (err) {
        res.status(400).json({ error: err.message });
    }
};

const updateCategory = async (req, res) => {
    try {
        const update = {};
        if (req.body.name !== undefined) {
            update.name = req.body.name;
        }
        if (req.body.promoted !== undefined) {
            update.promoted = req.body.promoted;
        }

        if (Object.keys(update).length === 0) {
            return res.status(400).json({ error: 'No valid fields to update' });
        }

        const categoryID = await CategoryService.updateCategory(req.params.id);
        if (!categoryID) {
            return res.status(404).json({ error: ['Category not found'] });
        }
        const category = await CategoryService.updateCategory(req.params.id, update.name, update.promoted);
        res.status(204).json(category);
    }
    catch (err) {
        res.status(400).json({ error: err.message });
    }
};


const deleteCategory = async (req, res) => {
    try {
        const category = await CategoryService.deleteCategory(req.params.id);
        if (!category) {
            return res.status(404).json({ error: ['Category not found'] });
        }
        res.status(204).send();
    }
    catch (err) {
        res.status(400).json({ error: err.message });
    }
};

module.exports = { createCategory, getCategories, getCategory, updateCategory, deleteCategory };
