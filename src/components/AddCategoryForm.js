import React, { useState } from "react";

function AddCategoryForm({ onCategoryAdded }) {
  const [newCategoryName, setNewCategoryName] = useState("");
  const [newCategoryPromoted, setNewCategoryPromoted] = useState(false);
  const [categoryError, setCategoryError] = useState("");
  const [loading] = useState(false);

  const handleAddCategory = async (e) => {
    e.preventDefault();
  
    if (!newCategoryName) {
      setCategoryError("Category name is required!");
      return;
    }
      const response = await fetch("http://localhost:5000/api/categories", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: newCategoryName,
          promoted: newCategoryPromoted,
        }),
      });
  
      if (response.ok) {
        alert("Category added successfully!");
        // Optionally, update the local categories state or call onCategoryAdded
        onCategoryAdded({
          name: newCategoryName,
          promoted: newCategoryPromoted,
        });
  
        // Reset form fields
        setNewCategoryName("");
        setNewCategoryPromoted(false);
        setCategoryError("");
      } else {
        const errorData = await response.json();
        console.error("Error adding category:", errorData); // Log error details
        setCategoryError(errorData.message || "Failed to add category.");
      }
    
  };
  

  return (
    <div className="addCategorySection">
      <h3>Add New Category</h3>
      {categoryError && <p className="error">{categoryError}</p>}
      <form onSubmit={handleAddCategory}>
        <div>
          <label htmlFor="newCategoryName">Category Name:</label>
          <input
            type="text"
            id="newCategoryName"
            value={newCategoryName}
            onChange={(e) => setNewCategoryName(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="newCategoryPromoted">
            Promoted:
            <input
              type="checkbox"
              id="newCategoryPromoted"
              checked={newCategoryPromoted}
              onChange={(e) => setNewCategoryPromoted(e.target.checked)}
            />
          </label>
        </div>
        <button type="submit" disabled={loading}>
          {loading ? "Adding..." : "Add Category"}
        </button>
      </form>
    </div>
  );
}

export default AddCategoryForm;
