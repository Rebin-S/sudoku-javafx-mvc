package se.kth.rebins.sudoku;

import se.kth.rebins.sudoku.View.*;
import se.kth.rebins.sudoku.Model.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        SudokuUtilities.SudokuLevel currentDifficulty = SudokuUtilities.SudokuLevel.MEDIUM;

        Facade facade = new Facade(currentDifficulty);

        GridView gridView = new GridView(facade);
        SudokuController controller = new SudokuController(facade,gridView, SudokuLevel.valueOf(String.valueOf(currentDifficulty)));
        gridView.setController(controller);

        Scene scene = new Scene(gridView.getNumberPane());

        Stage stage = new Stage();
        stage.setTitle("Sudoku Game");

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}