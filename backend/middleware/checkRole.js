const jwt = require('jsonwebtoken');
const User = require('../models/users');

const checkRole = (role) => {
    return async (req, res, next) => {
        try {
            const token = req.headers.authorization.split(' ')[1];
            const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
            const user = await User.findById(decodedToken.userId);

            if (!user || user.role !== role) {
                return res.status(403).json({ error: 'Access denied' });
            }

            req.user = user;
            next();
        } catch (error) {
            res.status(401).json({ error: 'Unauthorized' });
        }
    };
};

module.exports = checkRole;