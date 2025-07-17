package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;
import model.db.WordageddonDAO;
import model.db.WordageddonDAOSQLite;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller per il menu di autenticazione.
 * Gestisce le operazioni di login e il reindirizzamento alla schermata di registrazione.
 */
public class AuthMenuController {


    @FXML
    private Pane loginBtn;


    @FXML
    private TextField nomeUtenteTF;


    @FXML
    private TextField passwordTF;


    @FXML
    private Label wordaggedonLabel;


    @FXML
    private Button loginButton;

    /**
     * Mostra una finestra di dialogo con un messaggio informativo.
     *
     * @param title   Titolo della finestra di dialogo.
     * @param message Contenuto del messaggio da mostrare.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gestisce il click sul bottone "Registrati".
     * Carica e visualizza la schermata di registrazione.
     */
    @FXML
    private void handleRegisterClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegisterView.fxml"));
            Parent registerPage = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene newScene = new Scene(registerPage);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Errore nel caricamento della pagina di registrazione.");
            alert.showAndWait();
        }
    }

    /**
     * Gestisce l'evento di login dell'utente.
     * Verifica le credenziali tramite il DAO e in caso di successo carica il menu principale.
     *
     * @param event Evento generato dal click sul bottone di login.
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = nomeUtenteTF.getText();
        String password = passwordTF.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Campi mancanti", "Per favore inserisci username e password.");
            return;
        }

        WordageddonDAO dao = new WordageddonDAOSQLite();
        User user = dao.checkCredentials(username, password);

        if (user != null) {
            showAlert("Login Successful", "Welcome, " + user.getUsername() + "!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));
                Parent mainMenuRoot = loader.load();

                MainController mainMenuController = loader.getController();
                mainMenuController.init(user);

                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(mainMenuRoot));
                stage.show();

            } catch (IOException e) {
                System.err.println(e.getMessage());
                showAlert("Errore", "Impossibile caricare la schermata principale.");
            }
        } else {
            showAlert("Login fallito", "Username o password errati.");
        }
    }
}
