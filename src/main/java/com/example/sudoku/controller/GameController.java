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

public class GameController {
    @FXML
    private GridPane gridPaneSudoku;

    public boolean isNaN() {
        return false;
    }

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

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField textField = new TextField();
                textField.setMaxWidth(37);
                textField.setMaxHeight(37);
                textField.setStyle("-fx-border-color: black;");
                boolean mostrarInicialmente = random.nextBoolean();
                if (mostrarInicialmente) {
                    int numero = generarNumeroUnico(i, j, random);
                    textField.setText(String.valueOf(numero));
                    textField.setEditable(false);
                    // Agregar número a las listas correspondientes
                    numerosEnFila[i].addLast(numero);
                    numerosEnColumna[j].addLast(numero);
                    numerosEnCuadro[i / 3 * 3 + j / 3].add(numero);
                }
                gridPaneSudoku.add(textField, i, j);
                textFieldLetterGiven(textField, i, j);
            }
        }
    }

    // Método para no generar el mismo número en una columna/fila
    private int generarNumeroUnico(int fila, int columna, Random random) {
        int numero;
        int subgridRow = fila / 3;
        int subgridCol = columna / 3;
        do {
            numero = random.nextInt(9) + 1;
        } while (numerosEnFila[fila].contains(numero) || numerosEnColumna[columna].contains(numero) || numerosEnCuadro[subgridRow * 3 + subgridCol].contains(numero));
        return numero;
    }

    private void textFieldLetterGiven(TextField textField, int i, int j) {
        textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String input = textField.getText();
                if (!input.isEmpty()) {
                    try {
                        int numero = Integer.parseInt(input);
                        // Verificar si el número está duplicado en la columna
                        if (numerosEnFila[i].contains(numero)) {
                            mostrarAlerta("Número ya ubicado", "El número " + numero + " ya está presente en esta columna.");
                            textField.clear();
                            return;
                        }
                        // Verificar si el número está duplicado en la fila
                        if (numerosEnColumna[j].contains(numero)) {
                            mostrarAlerta("Número ya ubicado", "El número " + numero + " ya está presente en esta fila.");
                            textField.clear();
                            return;
                        }
                        if (numero==0){
                            mostrarAlerta("Número inválido", "Por favor ingresa números del 1 al 9");
                            textField.clear();
                            return;

                        }
                        if(numerosEnColumna[j].contains(numero)&&numerosEnFila[i].contains(numero)){
                            mostrarAlerta("Número ya ubicado", "El número " + numero + " ya está presente en la columa y la fila.");
                            textField.clear();
                            return;
                        }
                        int cuadroIndex = (i / 3) * 3 + (j / 3);
                        //cuadro de 3x3
                        if (numerosEnCuadro[cuadroIndex].contains(numero)) {
                            mostrarAlerta("Número duplicado", "El número " + numero + " ya está en el cuadro 3x3.");
                            textField.clear();
                            return; // Salir del método para evitar más validaciones
                        }
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", "Por favor ingresa números del 1 al 9");
                        textField.clear();
                    }
                }
            }
        });
    }

    // Shows an AlertBox
    private void mostrarAlerta(String titulo, String mensaje) {
        AlertBox alertBox = new AlertBox();
        alertBox.showMessage(titulo, null, mensaje);
    }
}