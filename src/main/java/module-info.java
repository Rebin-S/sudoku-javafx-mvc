module se.kth.rebins.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens se.kth.rebins.sudoku to javafx.fxml;
    exports se.kth.rebins.sudoku;
}