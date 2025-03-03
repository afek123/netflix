import React from "react";

const MovieCard = ({ movie, categories, handleDelete }) => {
  return (
    <div
      className="movieCard"
      onClick={() => handleDelete(movie._id, movie.title)}
      style={{
        border: "1px solid #ddd",
        borderRadius: "8px",
        overflow: "hidden",
        margin: "10px",
        cursor: "pointer",
        width: "200px",
        boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
      }}
    >
      <div className="cardContent">
        {/* Movie Poster */}
        <img
          src={
            movie.posterUrl
              ? `http://localhost:5000${movie.posterUrl}` // Use the correct URL for the uploaded poster
              : `https://via.placeholder.com/150x225?text=${movie.title}`
          }
          alt={movie.title || "Untitled"}
          style={{
            width: "100%",
            height: "300px",
            objectFit: "cover",
            borderBottom: "1px solid #ddd",
          }}
        />

        {/* Movie Information */}
        <div
          className="movieInfo"
          style={{
            padding: "10px",
            textAlign: "center",
          }}
        >
          <h3
            className="movieTitle"
            style={{
              fontSize: "1.2rem",
              fontWeight: "bold",
              margin: "5px 0",
            }}
          >
            {movie.title}
          </h3>
          <h3
            className="movieDirector"
          
          >
            Director: {movie.director || "Unknown"}
          </h3>

          {/* Categories */}
          <div
            className="movieCategories"
            style={{
              display: "flex",
              flexWrap: "wrap",
              gap: "5px", // Add space between category tags
              justifyContent: "center",
              marginTop: "10px",
            }}
          >
            Categories:{" "}
            {movie.category && movie.category.length > 0 ? (
              movie.category.map((categoryId) => (
                <span
                  key={categoryId}
                  className="categoryTag"
                  style={{
                    backgroundColor: "#e50914", // Netflix red
                    color: "white",
                    padding: "3px 8px",
                    borderRadius: "4px",
                    fontSize: "0.8rem",
                    fontWeight: "bold",
                  }}
                >
                  {categories[categoryId] || "Unknown"}
                </span>
              ))
            ) : (
              <span>No categories available</span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default MovieCard;