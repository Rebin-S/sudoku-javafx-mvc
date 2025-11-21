package se.kth.rebins.sudoku.View;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.FileChooser;
import se.kth.rebins.sudoku.Model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static se.kth.rebins.sudoku.Model.SudokuUtilities.GRID_SIZE;

public class SudokuController {

    private final GridView gridView;
    private SudokuLevel currentLevel;
    private final Facade facade;
    private int selectedNumber;

    public SudokuController(Facade facade, GridView gridView, SudokuLevel currentDifficulty) {
        this.facade = facade;
        this.gridView = gridView;
        this.selectedNumber = 0;

    }

    public void saveGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sudoku Files", "*.sudoku"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                facade.saveGame(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sudoku Files", "*.sudoku"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                facade.loadGame(file);
                updateView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void provideHint() {
        int[] hint = facade.getRandomHint();
        int row = hint[0];
        int col = hint[1];
        int value = hint[2];

        facade.setUserValue(row, col, value);
        gridView.updateSudokuGrid(row, col, value);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint Used!");
        alert.setHeaderText("Hint: Cell (" + row + ", " + col + ")  is " + value);
        alert.showAndWait();
    }

    public void checkSudoku() {
        boolean isCurrentlyCorrect = facade.isCurrentlySolved();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sudoku Check!");
        if (isCurrentlyCorrect) {
            alert.setHeaderText("Currently Correct!");
        } else {
            alert.setHeaderText("Currently incorrect!");
        }
        alert.showAndWait();
    }

    public void numberSelected(int number) {
        this.selectedNumber = number;

    }

    public void selectedTile(int row, int col) {
    }

    public int getSelectedNumber() {
        return selectedNumber;
    }

    public void resetGame() {
        facade.resetGame();
        updateView();
    }

    public void changeDifficulty() {
        List<String> choices = Arrays.asList("EASY", "MEDIUM", "HARD");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("MEDIUM", choices);
        dialog.setTitle("Change Difficulty");
        dialog.setHeaderText("Select Sudoku Difficulty Level");
        dialog.setContentText("Choose your level:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(level -> {
            SudokuUtilities.SudokuLevel newLevel = SudokuUtilities.SudokuLevel.valueOf(level);
            facade.newGame(newLevel);
            gridView.updateBoard();
        });
    }


    public void clearSelection(int row, int col) {
        facade.setUserValue(row, col, 0);
        gridView.updateSudokuGrid(row, col, 0);

    }

    private void updateView() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int value = facade.getUserValue(row, col);
                gridView.updateSudokuGrid(row, col, value);
            }
        }
    }

    public void clearPlacedCells() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (facade.isCellEditable(row, col)) {
                    facade.setUserValue(row, col, 0);
                    gridView.updateSudokuGrid(row, col, 0);
                }
            }
        }
    }

    public void setClearSelection() {
        this.selectedNumber = -1;
    }
}