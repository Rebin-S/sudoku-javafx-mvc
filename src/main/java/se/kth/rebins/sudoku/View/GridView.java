package se.kth.rebins.sudoku.View;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import se.kth.rebins.sudoku.Model.*;

import static se.kth.rebins.sudoku.Model.SudokuUtilities.*;

public class GridView {
    private final Label[][] numberTiles;
    private final GridPane numberPane;
    private final Facade facade;
    private SudokuController controller;

    public GridView(Facade facade) {
        this.facade = facade;
        numberTiles = new Label[GRID_SIZE][GRID_SIZE];
        initNumberTiles();
        numberPane = makeNumberPane();
    }

    public BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.setTop(createMenuBar());

        VBox numberButtons = createNumberButtons();
        root.setRight(numberButtons);
        VBox actionButtons = createActionButtons();
        root.setLeft(actionButtons);
        root.setCenter(numberPane);
        return root;
    }

    private VBox createActionButtons() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        Button checkButton = new Button("Check");
        checkButton.setOnAction(e -> controller.checkSudoku());

        Button hintButton = new Button("Hint");
        hintButton.setOnAction(e -> controller.provideHint());

        vbox.getChildren().addAll(checkButton, hintButton);
        return vbox;
    }

    private VBox createNumberButtons() {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);

        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            int number = i;
            numberButton.setOnAction(e -> controller.numberSelected(number));
            vbox.getChildren().add(numberButton);
        }

        Button clearButton = new Button("C");
        clearButton.setOnAction(e -> {
            controller.setClearSelection();
        });
        vbox.getChildren().add(clearButton);

        return vbox;
    }

    public BorderPane getNumberPane() {
        return createMainLayout();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem loadGame = new MenuItem("Load Game");
        loadGame.setOnAction(e -> controller.loadGame());
        MenuItem saveGame = new MenuItem("Save Game");
        saveGame.setOnAction(e -> controller.saveGame());
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(loadGame, saveGame, new SeparatorMenuItem(), exit);

        Menu gameMenu = new Menu("Game");
        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(e -> controller.resetGame());
        MenuItem changeDifficulty = new MenuItem("Change Difficulty");
        changeDifficulty.setOnAction(e -> controller.changeDifficulty());
        gameMenu.getItems().addAll(resetGame, changeDifficulty);

        Menu helpMenu = new Menu("Help");
        MenuItem viewRules = new MenuItem("View Rules");
        viewRules.setOnAction(e -> displayRules());
        helpMenu.getItems().add(viewRules);

        MenuItem clearPlacedCells = new MenuItem("Clear Placed Cells");
        clearPlacedCells.setOnAction(e -> controller.clearPlacedCells());
        helpMenu.getItems().add(clearPlacedCells);

        menuBar.getMenus().addAll(fileMenu, gameMenu, helpMenu);
        return menuBar;
    }

    private void displayRules() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sudoku Rules");
        alert.setHeaderText("How to play Sudoku");
        alert.setContentText("Sudoku is a game where you need to fill a 9x9 grid both horisontally and vertically, each number can only appear once horisontally and vertically, the grid is further split into 3x3 Squares where duplicate numbers are not Allowed!");
        alert.showAndWait();
    }

    private final void initNumberTiles() {
        Font font = Font.font("Monospaced", FontWeight.NORMAL, 20);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                String value = facade.isCellVisible(row, col) ?
                        String.valueOf(facade.getUserValue(row, col)) :
                        "";
                Label tile = new Label(value);
                tile.setPrefWidth(32);
                tile.setPrefHeight(32);
                tile.setFont(font);
                tile.setAlignment(Pos.CENTER);
                tile.setStyle("-fx-border-color: black; -fx-border-width: 0.5px;");
                int finalRow = row;
                int finalCol = col;
                tile.setOnMouseClicked(event -> handleTileClick(finalRow, finalCol));
                numberTiles[row][col] = tile;
            }
        }
    }

    private void handleTileClick(int row, int col) {
        if (facade.isCellVisible(row, col)) {
            return;
        }

        controller.selectedTile(row, col);
        int userValue = controller.getSelectedNumber();

        if (userValue == -1) {
            facade.setUserValue(row, col, 0);
            numberTiles[row][col].setText("");
            return;
        }
        if (userValue == 0) {
            return;
        }
        if (userValue == 'C') {
            controller.clearSelection(row, col);
        }
        facade.setUserValue(row, col, userValue);

        numberTiles[row][col].setText(String.valueOf(userValue));
    }

    public void updateBoard() {
        for (int row = 0; row < SudokuUtilities.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuUtilities.GRID_SIZE; col++) {
                String value = facade.isCellVisible(row, col) ? String.valueOf(facade.getUserValue(row, col)) : "";
                numberTiles[row][col].setText(value);
            }
        }
    }

    private final GridPane makeNumberPane() {
        GridPane root = new GridPane();
        root.setStyle(
                "-fx-border-color: black; -fx-border-width: 1.0px; -fx-background-color: white;");

        for (int srow = 0; srow < SECTIONS_PER_ROW; srow++) {
            for (int scol = 0; scol < SECTIONS_PER_ROW; scol++) {
                GridPane section = new GridPane();
                section.setStyle("-fx-border-color: black; -fx-border-width: 0.5px;");
                for (int row = 0; row < SECTION_SIZE; row++) {
                    for (int col = 0; col < SECTION_SIZE; col++) {

                        section.add(
                                numberTiles[srow * SECTION_SIZE + row][scol * SECTION_SIZE + col],
                                col, row);
                    }
                }
                root.add(section, scol, srow);
            }
        }

        return root;
    }

    public void updateSudokuGrid(int row, int col, int value) {
        String displayValue;
        if (value == 0) {
            displayValue = "";
        } else {
            displayValue = String.valueOf(value);
        }
        numberTiles[row][col].setText(displayValue);
    }

    public void setController(SudokuController controller) {
        this.controller = controller;
    }
}