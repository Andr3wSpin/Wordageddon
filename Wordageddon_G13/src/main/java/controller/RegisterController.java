package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.User;
import model.db.WordageddonDAOSQLite;

/**
 * Controller per la vista di registrazione utente.
 * Gestisce la logica di interazione per la schermata di registrazione,
 * inclusa la creazione di nuovi utenti e la navigazione tra le viste.
 */
public class RegisterController {

    @FXML
    private TextField nomeUtenteTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private Button registerButton;

    private WordageddonDAOSQLite dao = new WordageddonDAOSQLite();

    /**
     * Gestisce l'azione del pulsante di registrazione.
     * Recupera lo username, la password e lo stato di amministratore dai campi di input.
     * Valida i campi, tenta di registrare il nuovo utente nel database.
     * In caso di successo, reindirizza l'utente alla schermata di autenticazione (login).
     * In caso di errore per campi vuoti, utente esistente o fallimento della registrazione, mostra un alert.
     * @param event L'evento che ha scatenato l'azione (click del pulsante).
     */
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = nomeUtenteTF.getText();
        String password = passwordTF.getText();
        boolean isAdmin = adminCheckBox.isSelected();

        // Controlla se i campi sono vuoti
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Per favore compila tutti i campi.");
            return;
        }

        try {
            // Inserisce un nuovo utente nel database
            User newUser = dao.insertUser(username, password, isAdmin);
            if (newUser == null) {
                // Se l'utente esiste già o la registrazione fallisce per altri motivi
                showAlert(AlertType.ERROR, "Errore: l'utente esiste già o non è stato possibile registrarsi.");
            } else {
                // Registro avvenuto con successo e torna al menu di login
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
                    Parent root = loader.load();
                    // Ottiene lo stage corrente e imposta la nuova scena
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
            // Mostra errore se la registrazione genera un'eccezione imprevista
            showAlert(AlertType.ERROR, "Errore durante la registrazione: " + e.getMessage());
        }
    }

    /**
     * Gestisce l'evento di click del mouse sul link/elemento "Torna al Login".
     * Carica la vista di autenticazione (`AuthView.fxml`) e la imposta come nuova scena
     * sullo stage corrente, reindirizzando l'utente alla schermata di login.
     * @param event L'evento del mouse che ha scatenato l'azione.
     */
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

    /**
     * Metodo di supporto per mostrare finestre di alert all'utente.
     * @param type Il tipo di alert (es. {@code AlertType.ERROR}, {@code AlertType.INFORMATION}).
     * @param message Il messaggio da visualizzare nel corpo dell'alert.
     */
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Registrazione");
        alert.setHeaderText(null); // Nessun header per un alert semplice
        alert.setContentText(message);
        alert.showAndWait(); // Attende che l'utente chiuda l'alert
    }
}