import React from "react";
import "../pages/ManagerPage.css";

const DeleteCategoryForm = ({ categories, handleDeleteCategory }) => {
  return (
    <div className="deleteCategorySection">
      <h3>Delete Categories</h3>
      <ul>
        {Object.entries(categories).map(([id, name]) => (
          <li key={id}>
            {name}{" "}
            <button onClick={() => handleDeleteCategory(id)}>
              Delete
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default DeleteCategoryForm;
