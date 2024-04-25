package com.example.sudoku.controller;

import com.example.sudoku.model.alert.AlertBox;
import com.example.sudoku.model.list.IList;
import com.example.sudoku.model.list.LinkedList;
import com.example.sudoku.view.GameStage;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller class for managing Sudoku game logic.
 */
public class GameController {
    @FXML
    private GridPane gridPaneSudoku;
    // Lists to keep track of numbers in rows, columns, and 3x3 grids
    private IList<Integer>[] rowNumbers;
    private IList<Integer>[] columnNumbers;
    private ArrayList<Integer>[] gridNumbers;
    private GameStage gameStage;

    private final int[][] finalSolution = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    /**
     * Initializes the Sudoku game grid with random numbers in each cell of the grid.
     */
    @FXML
    public void initialize() {
        rowNumbers = new IList[9];
        columnNumbers = new IList[9];
        gridNumbers = new ArrayList[9];

        for (int i = 0; i < 9; i++) {
            rowNumbers[i] = new LinkedList<>();
            columnNumbers[i] = new LinkedList<>();
            gridNumbers[i] = new ArrayList<>();
        }

        // Create default matrix
        int initialNumbers[][] = {
                {0, 3, 0, 0, 7, 0, 9, 1, 0},
                {6, 0, 2, 1, 9, 5, 0, 0, 8},
                {0, 9, 0, 0, 0, 0, 0, 6, 0},

                {8, 0, 9, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 7, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},

                {0, 6, 1, 0, 0, 0, 2, 8, 0},
                {0, 8, 0, 4, 1, 9, 0, 0, 5},
                {3, 0, 0, 0, 8, 0, 0, 7, 0}
        };

        // Generate Sudoku grid and fill with initial numbers
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = initialNumbers[j][i];
                TextField textField = new TextField(value == 0 ? "" : String.valueOf(value)); // if value equals 0, the result is ""
                textField.setMaxWidth(37);
                textField.setMaxHeight(37);
                textField.setStyle("-fx-border-color: white;");
                textField.setEditable(value == 0); // Only editable if the value is not initially set

                int subgridRow = i / 3;
                int subgridCol = j / 3;
                int subgridIndex = subgridRow * 3 + subgridCol;

                if (value != 0) {
                    System.out.println("Valor leído desde la matriz inicial: " + value);
                    // Add number to corresponding lists
                    rowNumbers[i].addLast(value);
                    columnNumbers[j].addLast(value);
                    gridNumbers[subgridIndex].add(value);
                }
                gridPaneSudoku.add(textField, i, j);
                textFieldGivenNumber(textField, i, j);
            }
        }
    }

    /**
     * Sets event handler for text fields to validate user input.
     *
     * @param textField The text field to which the event handler is attached.
     * @param i         The row index of the text field.
     * @param j         The column index of the text field.
     */
    private void textFieldGivenNumber(TextField textField, int i, int j) {
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Handles the key events for the text field. If the Backspace or Delete key is pressed,
             * it checks if the text field is empty. If it is, it clears the corresponding row, column,
             * and square in the Sudoku grid.
             *
             * @param keyEvent The KeyEvent representing the key being pressed.
             */
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.BACK_SPACE) || keyEvent.getCode().equals(KeyCode.DELETE)) {
                    removeNumberFromLists(textField, i, j);
                    System.out.println("Row Numbers:");
                    for (int row = 0; row < rowNumbers.length; row++) {
                        System.out.print("Row " + row + ": ");
                        IList<Integer> currentRow = rowNumbers[row];
                        for (int i = 0; i < currentRow.size(); i++) {
                            System.out.print(currentRow.get(i) + " ");
                        }
                        System.out.println();
                    }

                    System.out.println("Column Numbers:");
                    for (int col = 0; col < columnNumbers.length; col++) {
                        System.out.print("Column " + col + ": ");
                        IList<Integer> currentCol = columnNumbers[col];
                        for (int i = 0; i < currentCol.size(); i++) {
                            System.out.print(currentCol.get(i) + " ");
                        }
                        System.out.println();
                    }

                    System.out.println("Grid Numbers:");
                    for (int grid = 0; grid < gridNumbers.length; grid++) {
                        System.out.print("Grid " + grid + ": ");
                        ArrayList<Integer> currentGrid = gridNumbers[grid];
                        for (int i = 0; i < currentGrid.size(); i++) {
                            System.out.print(currentGrid.get(i) + " ");
                        }
                        System.out.println();
                    }
                }
            }
        });

        /**
         * Sets an event handler to handle key typed events on the text field.
         * This event handler validates user input according to Sudoku rules
         * and updates data structures storing Sudoku grid information.
         * If the input is longer than one character, it displays a warning message
         * and allows only one character input at a time.
         *
         * @param new EventHandler<KeyEvent> The event handler to be set on the text field.
         */

        textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String input = textField.getText();

                if (input.length() > 1) {
                    showAlert("Cuidado", "Solo puedes ingresar un número a la vez");
                    textField.deletePreviousChar();
                    int number = Integer.parseInt(input);
                    rowNumbers[i].removeElement(number);
                    columnNumbers[j].removeElement(number);
                }

                if (!input.isEmpty()) {
                    try {
                        int number = Integer.parseInt(input);

                        // Verify if the number is in both the column and row
                        if (columnNumbers[j].contains(number) && rowNumbers[i].contains(number)) {
                            showAlert("Número ya ubicado", "El número " + number + " ya está presente en la columna y la fila.");
                            textField.clear();
                            return;
                        }
                        // Verify if the number is twice in the column
                        if (rowNumbers[i].contains(number)) {
                            showAlert("Número ya ubicado", "El número " + number + " ya está presente en esta columna.");
                            textField.clear();
                            return;
                        }

                        // Verify if the number is twice in the row
                        if (columnNumbers[j].contains(number)) {
                            showAlert("Número ya ubicado", "El número " + number + " ya está presente en esta fila.");
                            textField.clear();
                            return;
                        }
                        if (number == 0) {
                            showAlert("Número inválido", "Por favor ingresa números del 1 al 9");
                            textField.clear();
                            return;
                        }

                        int indexSquare = (i / 3) * 3 + (j / 3);
                        // Check if the number is duplicated in the 3x3 grid
                        if (gridNumbers[indexSquare].contains(number)) {
                            showAlert("Número duplicado", "El número " + number + " ya está en el cuadro 3x3.");
                            textField.clear();
                        }

                        // Updates data structure
                        rowNumbers[i].addLast(number);
                        columnNumbers[j].addLast(number);
                        gridNumbers[indexSquare].add(number);

                    } catch (NumberFormatException e) {
                        showAlert("Error", "Por favor ingresa números del 1 al 9");
                        textField.clear();
                    }
                }
            }
        });
    }

    /**
     * Checks if the Sudoku puzzle is complete based on user input.
     *
     * @return true if the Sudoku puzzle is complete, false otherwise.
     */
    private boolean isSudokuComplete() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField textField = (TextField) getNodeByRowColumnIndex(j, i, gridPaneSudoku);
                String userInput = textField.getText().trim();
                System.out.println("Número introducido por el usuario en la fila " + j + " y columna " + i + ": " + userInput);
                if (userInput.isEmpty()) {
                    System.out.println("Campo vacío encontrado en la fila " + j + " y columna " + i);
                    return false; // If there is an empty text field, the sudoku is incomplete
                }
                int userValue = Integer.parseInt(userInput);
                int solutionValue = finalSolution[j][i]; // Get the value of the final solution
                System.out.println("Valor de la solución en la fila " + j + " y columna " + i + ": " + solutionValue);
                if (userValue != solutionValue) {
                    System.out.println("Valor incorrecto encontrado en la fila " + j + " y columna " + i + ": " + userValue);
                    return false; // If any value does not match with the solution, the sudoku is incomplete

                }
            }
        }
        return true; // If all the text fields are filled in and the values match, the sudoku is complete

    }

    /**
     * Returns the node located at the specified row and column within a GridPane.
     *
     * @param row      The index of the row of the node to retrieve.
     * @param column   The index of the column of the node to retrieve.
     * @param gridPane The GridPane in which to search for the node.
     * @return The node located at the specified row and column, or null if no node is found at that position.
     */

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    /**
     * Handles the action event when the "Verify" button is clicked.
     * It checks if the Sudoku puzzle is complete and displays a corresponding message.
     *
     * @param event The action event triggered by clicking the "Verify" button.
     */

    @FXML
    void onVerifyButtonClick(ActionEvent event) {
        if (isSudokuComplete()) {
            showAlert("Sudoku Completo", "¡Felicidades! Has completado el Sudoku. :)");
        } else {
            showAlert("Sudoku Incompleto", "Por favor complete todas las casillas antes de verificar. :(");
        }
    }
    /**
     * Handles the action event when the "Instrucciones" button is clicked.
     * It displays instructions on how to play Sudoku.
     *
     * @param event The action event triggered by clicking the "Instrucciones" button.
     */

    @FXML
    void onHandleButtonHowToPlay(ActionEvent event) {
        String tittle="Intrucciones";
        String header ="Bienvenide!";
        String content ="Para inciar, digita un número en cada casilla " +
                "\ndicho número no deberá coincidir con el mismo número" +
                " ni en la fila, columna, cuadro 3x3" +
                " Buena suerte, a devorar. :)";
        AlertBox alertBox=new AlertBox();
        alertBox.showMessage(tittle,header,content);
    }
    /**
     * Displays an alert with the specified title and message.
     *
     * @param title   The title of the alert.
     * @param message The content/message of the alert.
     */

    private void showAlert(String title, String message) {
        AlertBox alertBox = new AlertBox();
        alertBox.showMessage(title, null, message);

    }

    /**
     * Removes the last element from the given ArrayList if it is not empty.
     *
     * @param arrayList The ArrayList from which to remove the last element.
     */
    private void removeLastElement(ArrayList<Integer> arrayList) {
        if (!arrayList.isEmpty()) {
            arrayList.remove(arrayList.size() - 1);
        }
    }
    /**
     * Removes the number from the corresponding lists representing rows, columns, and 3x3 grids.
     * based on the provided TextField's position (i, j). After removing the number, it updates the corresponding lists
     * and removes the last element from the ArrayList representing the 3x3 grid to which the number belongs.
     *
     * @param textField The TextField containing the number to be removed.
     * @param i         The row index of the TextField.
     * @param j         The column index of the TextField.
     */
        private void removeNumberFromLists(TextField textField, int i, int j) {
            String currentText = textField.getText();

            rowNumbers[i].removeLast();
            columnNumbers[j].removeLast();


            int indexSquare = (i/3)*3+(j/3);

            removeLastElement(gridNumbers[indexSquare]);

        }

}