package se.kth.rebins.sudoku.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileLogic {

    /**
     * Saves the current state of the Sudoku board to a specified file.
     *
     * @param board The current state of the Sudoku board.
     * @param file The file to which the game state will be saved.
     * @throws IOException If an error occurs during file writing.
     */
    public static void saveGame(int[][][] board, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    writer.write(board[row][col][0] + "," + board[row][col][1]);
                    if (col < board[row].length - 1) {
                        writer.write(";");
                    }
                }
                writer.newLine();
            }
        }
    }

    /**
     * Loads a Sudoku game state from a specified file.
     *
     * @param file The file from which the game state is to be loaded.
     * @return A 3D integer array representing the loaded Sudoku board state.
     * @throws IOException If an error occurs during file reading.
     */
    public static int[][][] loadGame(File file) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        int[][][] board = new int[9][9][2];  // Assumes a 9x9 Sudoku board

        for (int row = 0; row < lines.size(); row++) {
            String[] values = lines.get(row).split(";");
            for (int col = 0; col < values.length; col++) {
                String[] pair = values[col].split(",");
                board[row][col][0] = Integer.parseInt(pair[0]); // User value
                board[row][col][1] = Integer.parseInt(pair[1]); // Correct value
            }
        }
        return board;
    }
}