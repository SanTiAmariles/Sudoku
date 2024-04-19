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
import java.util.Collections;
import java.util.Random;

public class GameController {
    @FXML
    private GridPane gridPaneSudoku;

    private IList<Integer>[] numerosEnFila;
    private IList<Integer>[] numerosEnColumna;
    private ArrayList<Integer>[] numerosEnCuadro;

    @FXML
    public void initialize() {
        Random random = new Random();
        numerosEnFila = new IList[9];
        numerosEnColumna = new IList[9];
        numerosEnCuadro = new ArrayList[9];

        for (int i = 0; i < 9; i++) {
            numerosEnFila[i] = new LinkedList<>();
            numerosEnColumna[i] = new LinkedList<>();
            numerosEnCuadro[i] = new ArrayList<>();
        }
        ArrayList<Integer> posiciones = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            posiciones.add(i);
        }
        Collections.shuffle(posiciones);
        for (int index : posiciones) {
            int i = index / 9;
            int j = index % 9;
            TextField textField = new TextField();
            textField.setMaxWidth(37);
            textField.setMaxHeight(37);
            textField.setStyle("-fx-border-color: black;");

            int subgridRow = i / 3;
            int subgridCol = j / 3;
            int cuadroIndex = subgridRow * 3 + subgridCol;

            // Verificar si ya hay 4 números en el cuadro 3x3
            if (numerosEnCuadro[cuadroIndex].size() == 4) {
                gridPaneSudoku.add(textField, j, i);
                continue;  // Continuar recorriendo
            }

            int numero;
            int intentos = 0;
            while (intentos < 5) {
                numero = random.nextInt(9) + 1;
                if (!numerosEnFila[i].contains(numero) && !numerosEnColumna[j].contains(numero) && !numerosEnCuadro[cuadroIndex].contains(numero)) {
                    numerosEnFila[i].addLast(numero);
                    numerosEnColumna[j].addLast(numero);
                    numerosEnCuadro[cuadroIndex].add(numero);
                    textField.setText(String.valueOf(numero));
                    textField.setEditable(false);
                    break;
                }
                intentos++;
            }

            gridPaneSudoku.add(textField, j, i);
            textFieldLetterGiven(textField, i, j);
        }
    }
    private void textFieldLetterGiven(TextField textField, int i, int j) {
        textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String input = textField.getText();
                if (!input.isEmpty()) {
                    int numero = Integer.parseInt(input);
                    // Verificar si el número está duplicado en la columna
                    if (numerosEnFila[i].contains(numero)) {
                        mostrarAlerta("Número ya ubicado", "El número " + numero + " ya está presente en esta fila.");
                        textField.clear();
                        return;
                    }
                    // Verificar si el número está duplicado en la fila
                    if (numerosEnColumna[j].contains(numero)) {
                        mostrarAlerta("Número ya ubicado", "El número " + numero + " ya está presente en esta columna.");
                        textField.clear();
                        return;
                    }
                    int cuadroIndex = (i / 3) * 3 + (j / 3);
                    // Verificar si el número está duplicado en el cuadro 3x3
                    if (numerosEnCuadro[cuadroIndex].contains(numero)) {
                        mostrarAlerta("Número duplicado", "El número " + numero + " ya está en el cuadro 3x3.");
                        textField.clear();
                        return;
                    }
                }
            }
        });
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        AlertBox alertBox = new AlertBox();
        alertBox.showMessage(titulo, null, mensaje);
    }
}