import React from "react";

const ModeSelector = ({ mode, setMode }) => {
  return (
    <div className="modeSelector">
      <button onClick={() => setMode("add")} className={mode === "add" ? "active" : ""}>
        Add Movie
      </button>
      <button onClick={() => setMode("update")} className={mode === "update" ? "active" : ""}>
        Update Movie
      </button>
      <button onClick={() => setMode("delete")} className={mode === "delete" ? "active" : ""}>
        Delete Movie
      </button>
      <button onClick={() => setMode("addCategory")} className={mode === "addCategory" ? "active" : ""}>
        Add Category
      </button>
      <button onClick={() => setMode("deleteCategory")} className={mode === "deleteCategory" ? "active" : ""}>
        Delete Category
      </button>
      <button onClick={() => setMode("updateCategory")} className={mode === "updateCategory" ? "active" : ""}>
        Update Category
      </button>
    </div>
  );
};

export default ModeSelector;
