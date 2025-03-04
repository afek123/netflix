import React, { useState } from "react";

const UpdateCategoryForm = ({ categories, handleUpdateCategory }) => {
  const [selectedCategory, setSelectedCategory] = useState("");
  const [newName, setNewName] = useState("");
  const [promoted, setPromoted] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedCategory) {
      alert("Please select a category to update.");
      return;
    }

    const updatedData = {
      ...(newName && { name: newName }),
      promoted,
    };

    handleUpdateCategory(selectedCategory, updatedData);
    setNewName("");
    setPromoted(false);
  };

  return (
    <form onSubmit={handleSubmit} className="updateCategoryForm">
      <h2>Update Category</h2>
      <div>
        <label htmlFor="categorySelect">Category:</label>
        <select
          id="categorySelect"
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
        >
          <option value="">Select a category</option>
          {Object.entries(categories).map(([id, name]) => (
            <option key={id} value={id}>
              {name}
            </option>
          ))}
        </select>
      </div>

      <div>
        <label htmlFor="newName">New Name:</label>
        <input
          id="newName"
          type="text"
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          placeholder="Enter new category name"
        />
      </div>

      <div>
        <label>
          <input
            type="checkbox"
            checked={promoted}
            onChange={(e) => setPromoted(e.target.checked)}
          />
          Promoted
        </label>
      </div>

      <button type="submit">Update Category</button>
    </form>
  );
};

export default UpdateCategoryForm;
