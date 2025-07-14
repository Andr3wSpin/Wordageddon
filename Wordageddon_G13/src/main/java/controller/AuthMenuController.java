package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;
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
    private RadioButton signInBtn;

    @FXML
    private Label wordaggedonLabel;

    @FXML
    private Button loginButton;

    private ToggleGroup loginToggleGroup;

    @FXML
    public void initialize() {
        // Inizializza il gruppo Toggle per i radio button
        loginToggleGroup = new ToggleGroup();
        signInBtn.setToggleGroup(loginToggleGroup);

        // Imposta "Sign-in" come selezionato di default
        signInBtn.setSelected(true);
    }

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

    // Aquí puedes añadir lógica real de login, por ahora mostramos un mensaje:
    if (username.isEmpty() || password.isEmpty()) {
        showAlert("Campi mancanti", "Per favore inserisci username e password.");
    } else {
        // Puedes reemplazar esto con validación real
        showAlert("Login riuscito", "Benvenuto, " + username + "!");

    }
}


}
