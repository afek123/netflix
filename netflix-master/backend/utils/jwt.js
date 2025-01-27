const jwt = require('jsonwebtoken');

const JWT_SECRET = "mysecretkeyforjsonwebtokenN0tT0B3U53D1nPr0duct10n";

const createJWT = (user) => {
    const payload = {
        id: user.id,
        username: user.username,
        name: user.name,
        picture: user.picture,
    };
    const options = {
        expiresIn: '1d',
    };

    const token = jwt.sign(payload, JWT_SECRET, options);
    return token;
};

module.exports = createJWT;