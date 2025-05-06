package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class CarnetContact extends Application {

    private List<contact> contacts = new ArrayList<>();//liste d'objet
    private ContactDAO contactDAO = new ContactDAO();//instance de ContactDAO,

    private ListView<String> contactListView = new ListView<>();
    private TextField firstNameField = new TextField();
    private TextField nameField = new TextField();
    private TextField phoneField = new TextField();
    private TextField searchField = new TextField();

    private Label infoLabelAdd = new Label();
    private Label infoLabelDelete = new Label();
    private Label infoLabelSearch = new Label();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Carnet de Contacts Stylé");

        // Création du layout principal
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f0f8ff;");

        // Définition des champs de texte
        firstNameField.setPromptText("Prénom (lettres)");
        nameField.setPromptText("Nom (lettres)");
        phoneField.setPromptText("Téléphone (8 chiffres)");
        searchField.setPromptText("Rechercher par nom ou téléphone");


        Button addButton = new Button("Ajouter");
        Button deleteButton = new Button("Supprimer");
        Button searchButton = new Button("Rechercher");


        styleButton(addButton);
        styleButton(deleteButton);
        styleButton(searchButton);

        contactListView.setPrefHeight(300);

        // Layout du formulaire (ajout et suppression)
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.getChildren().addAll(firstNameField, nameField, phoneField, addButton, infoLabelAdd);

        // Layout de l'affichage de la liste des contacts
        VBox contactLayout = new VBox(10);
        contactLayout.setAlignment(Pos.CENTER);
        contactLayout.getChildren().addAll(contactListView); // Liste des contacts

        // Layout de la recherche
        HBox searchLayout = new HBox(10);
        searchLayout.setAlignment(Pos.CENTER); // Recherche centrée uniquement
        searchLayout.getChildren().addAll(searchField, searchButton, infoLabelSearch);

        // Layout de la suppression
        VBox deleteLayout = new VBox(10);
        deleteLayout.setAlignment(Pos.CENTER);
        deleteLayout.getChildren().addAll(deleteButton, infoLabelDelete);


        layout.getChildren().addAll(formLayout, deleteLayout, searchLayout, contactLayout);

        addButton.setOnAction(e -> handleAdd());
        deleteButton.setOnAction(e -> handleDelete());
        searchButton.setOnAction(e -> handleSearch());
        loadContactsFromDB();
        updateContactListView();


        primaryStage.setScene(new Scene(layout, 450, 600));
        primaryStage.show();
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    private boolean validateInputs() {
        String firstName = firstNameField.getText();
        String name = nameField.getText();
        String phone = phoneField.getText();

        if (!firstName.matches("[a-zA-Z]+") || !name.matches("[a-zA-Z]+")) {
            infoLabelAdd.setText("Le prénom et le nom doivent contenir uniquement des lettres.");
            return false;
        }

        if (!phone.matches("\\d{8}")) {
            infoLabelAdd.setText(" Le numéro de téléphone doit contenir 8 chiffres.");
            return false;
        }

        return true;
    }

    // Fonction d'ajout d'un contact
    private void handleAdd() {

        if (validateInputs()) {
            contact newContact = new contact(firstNameField.getText(), nameField.getText(), phoneField.getText());

            if (contactDAO.addContact(newContact)) {
                contacts.add(newContact);
                clearFields();
                updateContactListView();
                infoLabelAdd.setText(" Contact ajouté avec succès !");
            } else {
                infoLabelAdd.setText(" Erreur lors de l'ajout du contact.");
            }
        }
    }


    private void handleDelete() {
        String selected = contactListView.getSelectionModel().getSelectedItem();

        if (selected != null) {
            String phone = selected.split(" - ")[1];

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer ce contact ?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                if (contactDAO.deleteContact(phone)) {
                    contacts.removeIf(c -> c.getPhone().equals(phone));
                    updateContactListView();
                    infoLabelDelete.setText(" Contact supprimé !");
                } else {
                    infoLabelDelete.setText(" Erreur lors de la suppression.");
                }
            }
        } else {
            infoLabelDelete.setText(" Sélectionnez un contact à supprimer.");
        }
    }

    private void handleSearch() {

        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            updateContactListView();
            infoLabelSearch.setText(" Tous les contacts affichés.");
            return;
        }

        List<String> results = new ArrayList<>();
        for (contact c : contacts) {
            if (c.getFirstName().toLowerCase().contains(searchText) ||
                    c.getName().toLowerCase().contains(searchText) ||
                    c.getPhone().contains(searchText)) {
                results.add(c.toString());
            }
        }

        contactListView.getItems().setAll(results);

        if (results.isEmpty()) {
            infoLabelSearch.setText("Aucun contact trouvé.");
        } else {
            infoLabelSearch.setText(results.size() + " résultat(s) trouvé(s).");
        }
    }
    private void clearFields() {
        firstNameField.clear();
        nameField.clear();
        phoneField.clear();
    }

    // Fonction pour charger les contacts depuis la base de donnée
    private void loadContactsFromDB() {
        contacts = contactDAO.findAll();
    }

    private void updateContactListView() {
        contactListView.getItems().clear();
        contacts.forEach(c -> contactListView.getItems().add(c.toString()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}