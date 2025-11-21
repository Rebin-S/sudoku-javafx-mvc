# Sudoku Game – JavaFX & MVC Architecture

A fully interactive Sudoku application built with **JavaFX** and organized using the **Model–View–Controller (MVC)** design pattern.  
The application supports multiple difficulty levels, real-time board interaction, file saving/loading of game state, input validation, and hint/check functions.

This project demonstrates clean separation of concerns, object-oriented design, event handling in JavaFX, and serialization of complex game state.

---

## Features

### 🎮 Gameplay
- Complete 9×9 Sudoku board with 3×3 sub-grids.
- Pre-filled cells that the user cannot modify.
- Ability to:
  - Insert numbers (1–9)
  - Clear a selected cell
  - Reset all user-entered cells
- Final validation when the last cell is filled.

### 🤖 Game Logic
- Sudoku generation for **three difficulty levels**:
  - Easy  
  - Medium  
  - Hard  
- Each difficulty generates a **unique randomized Sudoku configuration** by:
  - Horizontal and vertical board mirroring  
  - Digit permutation (e.g., swap all 1’s with all 2’s)  
- Internal model stores:
  - The correct solution  
  - Whether a cell is fixed or user-editable  
  - The user’s current input  

### 💾 Save & Load
- Save game state to `.sudoku` files using Java object serialization.
- Load previous games from disk.
- Uses `FileChooser` for file selection.

### 🖥️ User Interface (JavaFX)
- Menu bar with:
  - **Game:** New game, change difficulty  
  - **File:** Save, Load  
  - **Help:** Clear moves, Check board, Hint, About  
- Left-side buttons for “Check” and “Hint”.
- Right-side number pad (1–9) for selecting numbers to insert.
- Main 9×9 grid implemented using `TilePane`.

### 🧩 Validation & Hint System
- **Check:** Verifies that all user-entered values are correct so far.  
- **Hint:** Fills one random empty cell with the correct value.

---

## Architecture

The project follows strict **MVC architecture**:

