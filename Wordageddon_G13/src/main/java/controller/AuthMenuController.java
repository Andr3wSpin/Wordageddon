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


    // Metodo per mostrare un messaggio di avviso sullo schermo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
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
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText("Errore nel caricamento della pagina di registrazione.");
        alert.showAndWait();
    }
}

    /*
     * Hugo, si añades un objeto WordageddonDAOSqlite,
     * puedes enviar todas las query que necesites.
     * Dentro hay un método llamado checkCredentials
     * que verifica si existe un usuario con el nombre
     * y la contraseña que le pasas (los que están en los textField).
     *
     * Si tienes alguna pregunta sobre la clase WordageddonDAOSqlite,
     * puedes preguntarme a mí (Paolo).
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

    if (user != null && password != null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));
            Parent mainMenuRoot = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile caricare la schermata principale.");
        }

    } else {
        showAlert("Login fallito", "Username o password errati.");
        // Puedes reemplazar esto con validación real
        showAlert("Login riuscito", "Benvenuto, " + username + "!");

    }
  }
}
