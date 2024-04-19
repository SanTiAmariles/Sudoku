package com.example.sudoku.controller;

import com.example.sudoku.model.alert.AlertBox;
import com.example.sudoku.model.list.IList;
import com.example.sudoku.model.list.LinkedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Random;

/**
 * Controller class for managing Sudoku game logic and UI interactions.
 */

public class GameController {
    @FXML
    private GridPane gridPaneSudoku;
    // Lists to keep track of numbers in rows, columns, and 3x3 grids
    private IList<Integer>[] rowNumbers;
    private IList<Integer>[] columnNumbers;
    private ArrayList<Integer>[] gridNumbers;

     // Initializes the Sudoku game grid.

    @FXML
    public void initialize() {
        Random random = new Random();
        rowNumbers = new IList[9];
        columnNumbers = new IList[9];
        gridNumbers = new ArrayList[9];

        for (int i = 0; i < 9; i++) {
            rowNumbers[i] = new LinkedList<>();
            columnNumbers[i] = new LinkedList<>();
            gridNumbers[i] = new ArrayList<>();
        }
        // Generate Sudoku grid and fill with initial numbers
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField textField = new TextField();
                textField.setMaxWidth(37);
                textField.setMaxHeight(37);
                textField.setStyle("-fx-border-color: black;");
                boolean showInitially = random.nextBoolean();
                if (showInitially) {
                    int number = generateUniqueNumber(i, j, random);
                    textField.setText(String.valueOf(number));
                    textField.setEditable(false);
                    // Add number to corresponding lists
                    rowNumbers[i].addLast(number);
                    columnNumbers[j].addLast(number);
                    gridNumbers[i / 3 * 3 + j / 3].add(number);
                }
                gridPaneSudoku.add(textField, i, j);
                textFieldLetterGiven(textField, i, j);
            }
        }
    }

    // Method with the purpose of not generating the same number in a column/row
    private int generateUniqueNumber(int row, int column, Random random) {
        int number;
        int subgridRow = row / 3;
        int subgridCol = column / 3;
        do {
            number = random.nextInt(9) + 1;
        } while (rowNumbers[row].contains(number) || columnNumbers[column].contains(number) || gridNumbers[subgridRow * 3 + subgridCol].contains(number));
        return number;
    }
    /**
     * Sets event handler for text fields to validate user input.
     * @param textField The text field to which the event handler is attached.
     * @param i The row index of the text field.
     * @param j The column index of the text field.
     */

    private void textFieldLetterGiven(TextField textField, int i, int j) {
        textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String input = textField.getText();
                if (input.length()>1){
                    showAlert("Cuidado", "Solo puedes ingresar un número a la vez");
                    textField.deletePreviousChar();
                }
                if (!input.isEmpty()) {
                    try {
                        int number = Integer.parseInt(input);
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
                        if (number==0){
                            showAlert("Número inválido", "Por favor ingresa números del 1 al 9");
                            textField.clear();
                            return;

                        }
                        if(columnNumbers[j].contains(number)&& rowNumbers[i].contains(number)){
                            showAlert("Número ya ubicado", "El número " + number + " ya está presente en la columa y la fila.");
                            textField.clear();
                            return;
                        }
                        int indexSquare = (i / 3) * 3 + (j / 3);
                        // Check if the number is duplicated in the 3x3 grid
                        if (gridNumbers[indexSquare].contains(number)) {
                            showAlert("Número duplicado", "El número " + number + " ya está en el cuadro 3x3.");
                            textField.clear();
                                                    }
                    } catch (NumberFormatException e) {
                        showAlert("Error", "Por favor ingresa números del 1 al 9");
                        textField.clear();
                    }
                }
            }
        });
    }

    // Shows an alert
    private void showAlert(String title, String message) {
        AlertBox alertBox = new AlertBox();
        alertBox.showMessage(title, null, message);
    }
}