package com.example.sudoku.controller;

import com.example.sudoku.model.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class GameController {
    //Test second commit
    @FXML
    private GridPane gridPaneSudoku;
    @FXML
    void onHandleButtonHowToPlay(ActionEvent event) {
        String tittle = "¿Cómo jugar?";
        String header = "Binvenide!";
        String content = "Si eres quien establece la palabra secreta, escríbela en este espacio y presiona 'jugar'."
                + " Si eres quien va a adivinar la palabra, escribe letra por letra en los espacios, sabrás que la letra es correcta si no se le suman partes al ahorcado. Buena suerte!";
        AlertBox alertBox = new AlertBox();
        alertBox.showMessage(tittle, header, content);
    }

    private int numRandom;
    @FXML
    public void initialize(){
        Random random = new Random();
        System.out.println("El numero actual es: "+numRandom);
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                TextField textField = new TextField();

                textField.setMaxWidth(37);
                textField.setMaxHeight(37);
                textField.setStyle("-fx-border-color: black;");
                boolean mostrarInicialmente = random.nextBoolean();
                if (mostrarInicialmente) {
                    // Genera un número aleatorio del 1 al 9
                    int numero = random.nextInt(9) + 1;
                    textField.setText(String.valueOf(numero));
                    // Deshabilita la edición del TextField
                    textField.setEditable(false);
                }
                gridPaneSudoku.add(textField, i, j);
                textFieldLetterGiven(textField, i, j);
            }
        }
    }

    private void textFieldLetterGiven(TextField textField, int i, int j){
        textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

            }

        });
    }
}