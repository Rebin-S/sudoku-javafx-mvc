package se.kth.rebins.sudoku.Model;

import java.util.Random;

public class SudokuUtilities {
    public enum SudokuLevel {EASY, MEDIUM, HARD}

    public static final int GRID_SIZE = 9;
    public static final int SECTIONS_PER_ROW = 3;
    public static final int SECTION_SIZE = 3;
    private static final Random random = new Random();


    /**
     * Create a 3-dimensional matrix with initial values and solution in Sudoku.
     *
     * @param level The level, i.e. the difficulty, of the initial standing.
     * @return A 3-dimensional int matrix.
     * [row][col][0] represents the initial values, zero representing an empty cell.
     * [row][col][1] represents the solution.
     * @throws IllegalArgumentException if the length of stringRepresentation is not 2*81 characters and
     *                                  for characters other than '0'-'9'.
     */
    public static int[][][] generateSudokuMatrix(SudokuLevel level) {
        String representationString;
        switch (level) {
            case EASY:
                representationString = easy;
                break;
            case MEDIUM:
                representationString = medium;
                break;
            case HARD:
                representationString = hard;
                break;
            default:
                representationString = medium;
        }
        int[][][] matrix = convertStringToIntMatrix(representationString);

        mirrorHorizontally(matrix);
        mirrorVertically(matrix);
        swapNumbers(matrix);

        return matrix;
    }


    /**
     * Create a 3-dimensional matrix with initial values and solution in Sudoku.
     *
     * @param stringRepresentation A string of 2*81 characters, 0-9. The first 81 characters represents
     *                             the initial values, '0' representing an empty cell.
     *                             The following 81 characters represents the solution.
     * @return A 3-dimensional int matrix.
     * [row][col][0] represents the initial values, zero representing an empty cell.
     * [row][col][1] represents the solution.
     * @throws IllegalArgumentException if the length of stringRepresentation is not 2*81 characters and
     *                                  for characters other than '0'-'9'.
     */
    /*package private*/
    static int[][][] convertStringToIntMatrix(String stringRepresentation) {
        if (stringRepresentation.length() != GRID_SIZE * GRID_SIZE * 2)
            throw new IllegalArgumentException("Representation length " +
                    stringRepresentation.length() + " is invalid");

        int[][][] values = new int[GRID_SIZE][GRID_SIZE][2];
        char[] charRepresentation = stringRepresentation.toCharArray();
        int charIndex = 0;
        // initial values
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                values[row][col][0] = convertCharToSudokuInt(charRepresentation[charIndex++]);
            }
        }

        // solution values
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                values[row][col][1] = convertCharToSudokuInt(charRepresentation[charIndex++]);
            }
        }

        return values;
    }

    private static int convertCharToSudokuInt(char ch) {
        if (ch < '0' || ch > '9') throw new IllegalArgumentException("Character " + ch + " is invalid");
        return ch - '0';
    }

    /**
     * Mirrors the Sudoku matrix horisontally.
     *
     * @param matrix The 3-dimensional matrix to mirror.
     */
    private static void mirrorHorizontally(int[][][] matrix) {
        for (int row = 0; row < GRID_SIZE / 2; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int[] temp = matrix[row][col];
                matrix[row][col] = matrix[GRID_SIZE - row - 1][col];
                matrix[GRID_SIZE - row - 1][col] = temp;
            }
        }
    }

    /**
     * Mirrors the Sudoku matrix vertically.
     *
     * @param matrix The 3-dimensional matrix to mirror.
     */
    private static void mirrorVertically(int[][][] matrix) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE / 2; col++) {
                int[] temp = matrix[row][col];
                matrix[row][col] = matrix[row][GRID_SIZE - col - 1];
                matrix[row][GRID_SIZE - col - 1] = temp;
            }
        }
    }

    /**
     * Swaps two randomly selected numbers (from 1 to 9) in the Sudoku matrix.
     *
     * @param matrix The 3-dimensional matrix.
     */
    private static void swapNumbers(int[][][] matrix) {
        int num1 = random.nextInt(9) + 1;
        int num2;
        do {
            num2 = random.nextInt(9) + 1;
        } while (num2 == num1);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (matrix[row][col][0] == num1) matrix[row][col][0] = num2;
                else if (matrix[row][col][0] == num2) matrix[row][col][0] = num1;

                if (matrix[row][col][1] == num1) matrix[row][col][1] = num2;
                else if (matrix[row][col][1] == num2) matrix[row][col][1] = num1;
            }
        }
    }

    private static final String easy =
            "000914070" +
                    "010000054" +
                    "040002000" +
                    "007569001" +
                    "401000500" +
                    "300100000" +
                    "039000408" +
                    "650800030" +
                    "000403260" +  // solution values after this substring
                    "583914672" +
                    "712386954" +
                    "946752183" +
                    "827569341" +
                    "461238597" +
                    "395147826" +
                    "239675418" +
                    "654821739" +
                    "178493265";

    private static final String medium =
            "300000010" +
                    "000050906" +
                    "050401200" +
                    "030000080" +
                    "002069400" +
                    "000000002" +
                    "900610000" +
                    "200300058" +
                    "100800090" +
                    "324976815" +
                    "718253946" +
                    "659481273" +
                    "536142789" +
                    "872569431" +
                    "491738562" +
                    "985617324" +
                    "267394158" +
                    "143825697";

    private static final String hard =
            "030600000" +
                    "000010070" +
                    "080000000" +
                    "000020000" +
                    "340000800" +
                    "500030094" +
                    "000400000" +
                    "150800200" +
                    "700006050" +
                    "931687542" +
                    "465219378" +
                    "287345916" +
                    "876924135" +
                    "349561827" +
                    "512738694" +
                    "693452781" +
                    "154873269" +
                    "728196453";
}