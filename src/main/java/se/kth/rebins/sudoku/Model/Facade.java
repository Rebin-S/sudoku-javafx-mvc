package se.kth.rebins.sudoku.Model;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.util.Random;
import java.io.File;
import java.io.IOException;

public class Facade {
    private final Cell[][] board;
    private SudokuUtilities.SudokuLevel currentDifficulty;

    /**
     * Constructs a Facade and initializes a new Sudoku game board with a specified difficulty level.
     *
     * @param level The difficulty level of the Sudoku game as defined in SudokuUtilities.SudokuLevel.
     */
    public Facade(SudokuUtilities.SudokuLevel level) {
        int[][][] matrix = SudokuUtilities.generateSudokuMatrix(level);
        board = new Cell[SudokuUtilities.GRID_SIZE][SudokuUtilities.GRID_SIZE];
        currentDifficulty = level;
        initializeBoard(matrix);
    }


    /**
     * Starts a new game with a specified difficulty level.
     *
     * @param level The difficulty level for the new game.
     */
    public void newGame(SudokuUtilities.SudokuLevel level) {
        int[][][] matrix = SudokuUtilities.generateSudokuMatrix(level);
        initializeBoard(matrix);
    }

    /**
     * Initializes the board with a given Sudoku matrix, setting up each cell's state.
     *
     * @param matrix The matrix used to initialize the game board.
     */
    private void initializeBoard(int[][][] matrix) {
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                int initialValue = matrix[row][col][0];
                int correctValue = matrix[row][col][1];
                boolean visible = (initialValue != 0);
                board[row][col] = new Cell(correctValue, initialValue, visible);
            }
        }
    }



    /**
     * Returns the user-entered value of a specified cell.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return The user value of the cell.
     */
    public int getUserValue(int row, int col) {
        return board[row][col].getUserValue();
    }

    /**
     * Sets the user-entered value for a specified cell if it is editable.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @param value The value to set.
     */
    public void setUserValue(int row, int col, int value) {
        if (board[row][col].isEditable()) {
            board[row][col].setUserValue(value);
            checkIfPuzzleSolved();
        }
    }

    /**
     * Checks if a specified cell is visible.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return True if the cell is visible, false otherwise.
     */
    public boolean isCellVisible(int row, int col) {
        return board[row][col].isVisible();
    }

    /**
     * Checks if the current state of the board solves the Sudoku puzzle.
     *
     * @return True if the puzzle is currently solved, false otherwise.
     */
    public boolean isCurrentlySolved() {
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (board[row][col].getUserValue() != 0 && !board[row][col].isCorrect()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Triggers actions based on whether the puzzle is solved or incorrectly filled.
     */
    private void checkIfPuzzleSolved() {
        if (isSolved()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Puzzle Solved");
                alert.setHeaderText(null);
                alert.setContentText("Congratulations! You've correctly solved the puzzle.");
                alert.showAndWait();
            });
        } else if (isCompletelyFilled() && !isCurrentlySolved()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect Solution");
                alert.setHeaderText(null);
                alert.setContentText("The puzzle is not solved correctly. Try again!");
                alert.showAndWait();
            });
        }
    }

    /**
     * Provides a hint by randomly selecting an editable and empty cell, revealing its correct value.
     *
     * @return An array containing the row and column indices and the correct value of the hinted cell.
     */
    public int[] getRandomHint() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(SudokuUtilities.GRID_SIZE);
            col = random.nextInt(SudokuUtilities.GRID_SIZE);
        } while (!(board[row][col].isEditable() && board[row][col].getUserValue() == 0));
        return new int[]{row, col, board[row][col].getCorrectValue()};
    }

    /**
     * Saves the current game state to a file.
     *
     * @param file The file to save the game state to.
     * @throws IOException If an error occurs during file writing.
     */
    public void saveGame(File file) throws IOException {
        int[][][] boardData = new int[SudokuUtilities.GRID_SIZE][SudokuUtilities.GRID_SIZE][2];
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                boardData[row][col][0] = board[row][col].getUserValue();
                boardData[row][col][1] = board[row][col].getCorrectValue();
            }
        }
        FileLogic.saveGame(boardData, file);
    }

    /**
     * Loads a game state from a file.
     *
     * @param file The file from which to load the game.
     * @throws IOException If an error occurs during file reading.
     */
    public void loadGame(File file) throws IOException {
        int[][][] loadedBoard = FileLogic.loadGame(file);
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                board[row][col].setUserValue(loadedBoard[row][col][0]);
            }
        }
    }

    /**
     * Resets the game to the initial state based on the current difficulty.
     */
    public void resetGame() {
        int[][][] newMatrix = SudokuUtilities.generateSudokuMatrix(currentDifficulty);
        initializeBoard(newMatrix);
    }

    /**
     * Checks if every cell on the board is filled with a user value.
     *
     * @return True if all cells are filled, false if any cell is empty.
     */
    private boolean isCompletelyFilled() {
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (board[row][col].getUserValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the cell at specified row and column is editable.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return True if the cell is editable, false otherwise.
     */
    public boolean isCellEditable(int row, int col) {
        return board[row][col].isEditable();
    }

    /**
     * Checks if the current configuration of the Sudoku board is a correct solution.
     *
     * @return True if the solution is correct, false otherwise.
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                if (!board[row][col].isCorrect()) {
                    return false;
                }
            }
        }
        return true;
    }
}