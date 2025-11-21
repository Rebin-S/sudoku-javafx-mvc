package se.kth.rebins.sudoku.Model;

public class Cell {
    private final int correctValue;
    private int userValue;
    private final boolean visible;
    private final boolean editable;

    /**
     * Constructs a new Cell with specified correct value, initial user value, and visibility.
     *
     * @param correctValue the correct value of the cell
     * @param initialValue the initial value input by the user
     * @param visible      true if the cell is initially visible to the user, otherwise false
     */
    public Cell(int correctValue, int initialValue, boolean visible) {
        this.correctValue = correctValue;
        this.userValue = visible ? initialValue : 0;
        this.visible = visible;
        this.editable = !visible;
    }

    /**
     * Returns the correct value of the cell.
     *
     * @return the correct value
     */
    public int getCorrectValue() {
        return correctValue;
    }

    /**
     * Returns the user-entered value of the cell.
     *
     * @return the user value
     */
    public int getUserValue() {
        return userValue;
    }

    /**
     * Sets the user-entered value of the cell. This method allows modifying the user value.
     *
     * @param value the new value to set
     */
    public void setUserValue(int value) {
        this.userValue = value;
    }

    /**
     * Checks if the user-entered value matches the correct value of the cell.
     *
     * @return true if the values match, otherwise false
     */
    public boolean isCorrect() {
        return userValue == correctValue;
    }

    /**
     * Determines if the cell is visible to the user.
     *
     * @return true if the cell is visible, otherwise false
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Determines if the cell's value can be edited by the user.
     *
     * @return true if the cell is editable, otherwise false
     */
    public boolean isEditable() {
        return editable;
    }
}
