const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || "oursecretKey12yevce943bv3498g3v@ece389!@#$%^&*()";

const createJWT = (user) => {
    const payload = {
        id: user.id,
        role: user.role
    };
    const options = {
        expiresIn: '1d'
    };

    const token = jwt.sign(payload, JWT_SECRET, options);
    return token;
};

module.exports = createJWT;