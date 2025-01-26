
const router = require('express').Router();
let Category = require('../models/category');

// Get all categories
router.route('/').get((req, res) => {
  Category.find()
    .then(categories => res.json(categories))
    .catch(err => res.status(400).json('Error: ' + err));
});

module.exports = router;
const categoryController = require('../controllers/category');

router.route('/')
    .get(categoryController.getCategories)
    .post(categoryController.createCategory);

router.route('/:id')
    .get(categoryController.getCategory)
    .patch(categoryController.updateCategory)
    .delete(categoryController.deleteCategory);

module.exports = router;