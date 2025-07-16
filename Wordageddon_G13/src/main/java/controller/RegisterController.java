package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.User;
import model.db.WordageddonDAOSQLite;

public class RegisterController {

    @FXML
    private TextField nomeUtenteTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private CheckBox adminCheckBox;

    private WordageddonDAOSQLite dao = new WordageddonDAOSQLite();

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = nomeUtenteTF.getText();
        String password = passwordTF.getText();
        boolean isAdmin = adminCheckBox.isSelected();

        // Controlla se i campi sono vuoti
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Per favore completa tutti i campi.");
            return;
        }

       try {
    // Inserisce un nuovo utente nel database
    User newUser = dao.insertUser(username, password, isAdmin);
    if (newUser == null) {
        // Se l'utente esiste già o la registrazione fallisce
        showAlert(AlertType.ERROR, "Errore: l'utente esiste già o non è stato possibile registrarsi.");
    } else {
        // Registro avvenuto con successo e torna al menu di login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomeUtenteTF.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Errore nel caricamento del login: " + e.getMessage());
        }
    }
} catch (Exception e) {
    e.printStackTrace();
    // Mostra errore se la registrazione genera un'eccezione
    showAlert(AlertType.ERROR, "Errore durante la registrazione: " + e.getMessage());}
}


    @FXML
    private void handleBackToLoginClick(MouseEvent event) {
        try {
            // Carica il file FXML della vista di login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
            Parent root = loader.load();

            // Ottiene lo stage dalla sorgente dell'evento (il nodo cliccato)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Imposta la nuova scena con la vista di login
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Mostra messaggio di errore se il caricamento fallisce
            showAlert(AlertType.ERROR, "Errore nel caricamento del login: " + e.getMessage());
        }
    }

    // Metodo di supporto per mostrare finestre di alert
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Registrazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
