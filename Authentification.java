package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Authentification extends Application {

    private static final String USERNAME = "sabeh";
    private static final String PASSWORD = "102004";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Page d'Authentification");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Label errorLabel = new Label();
        Button loginButton = new Button("Se connecter");

        // Personnalisation des champs
        usernameField.setPromptText("Nom d'utilisateur");
        passwordField.setPromptText("Mot de passe");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                primaryStage.close();
                new CarnetContact().start(new Stage());
            } else {
                errorLabel.setText("Identifiants incorrects !");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        });

        VBox layout = new VBox(10, usernameField, passwordField, loginButton, errorLabel);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f0f8ff;");

        loginButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setPrefWidth(200);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
