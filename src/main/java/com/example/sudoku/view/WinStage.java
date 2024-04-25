package com.example.sudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class WinStage extends Stage {

    public WinStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sudoku/win-stage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        setTitle("Sudoku");
        getIcons().add(new Image(String.valueOf(getClass().getResource("/com/example/sudoku/images/favicon.png"))));
        setResizable(false);
        setScene(scene);
        show();
    }

    public static WinStage getInstance() throws IOException {
        return WinStageHolder.INSTANCE;
    }

    private static class WinStageHolder {
        private static final WinStage INSTANCE;

        static {
            try {
                INSTANCE = new WinStage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void deleteInstance() {
        WinStageHolder.INSTANCE.close();
    }
}