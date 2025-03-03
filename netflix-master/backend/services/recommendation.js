const userService = require('../services/users'); // Import userService

const net = require('net');

const sendCommandToRecommendationServer = (command) => {
    const serverIp = process.env.RECOMMENDATION_SERVER_IP;
    const serverPort = process.env.RECOMMENDATION_SERVER_PORT;
    console.log(serverIp);
    console.log(serverPort);
    return new Promise((resolve, reject) => {
        const client = new net.Socket();
        console.log(command);

        // Attempt to connect to the recommendation server
        client.connect(serverPort, serverIp, () => {
            console.log(`Connected to reommendation server at ${serverIp}:${serverPort}`);
            client.write(command + "\n");
        });

        let response = '';
        
        client.on('data', (data) => {
            response += data.toString();
            if (response.includes("\n")) {
                client.end(); // Close connection after receiving response
            }
            console.log("dtat",response);
        });
        client.on('end', () => {
            console.log("Connection closed by recommendation server.");
            resolve(response.trim()); // Resolve with the trimmed response
        });

        client.on('error', (err) => {
            console.error("Error communicating with recommendation server:", err.message);
            reject(new Error("Failed to connect to recommendation server."));
        });
    });
};

const getRecommendations = async (userId, movieId) => {
    const command = `GET ${userId} ${movieId}`;
    const respone=await sendCommandToRecommendationServer(command);
    console.log("respone",respone);
    return respone;
};

const addRecommendation = async (userId, movieId) => {
    // First, add the movie to the user's watched list
    await userService.addMovieToWatched(userId, movieId);
    // Then, send the command to the recommendation server (if needed)
    const command = `POST ${userId} ${movieId}`;
    return await sendCommandToRecommendationServer(command);
};
const deleteRecommendation = async (userId, movieId) => {
    const command = `DELETE ${userId} ${movieId}`;
    return await sendCommandToRecommendationServer(command);
};

module.exports = { getRecommendations, addRecommendation ,deleteRecommendation};
