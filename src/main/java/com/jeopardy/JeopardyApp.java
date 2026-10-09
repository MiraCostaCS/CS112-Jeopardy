package com.jeopardy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JeopardyApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jeopardy/fxml/main-view.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("JavaFX Jeopardy (MVC)");
        primaryStage.setScene(new Scene(root, 900, 550));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
