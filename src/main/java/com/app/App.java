package com.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        stage.setTitle("SmartPay");
        stage.setResizable(false);

        Parent root = loadFXML("Login"); // load Login.fxml
        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Gagal memuat FXML: " + fxml);
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
