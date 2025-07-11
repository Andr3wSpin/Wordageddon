package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
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
    private RadioButton signUpBtn;

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
        signUpBtn.setToggleGroup(loginToggleGroup);

        // Imposta "Sign-in" come selezionato di default
        signInBtn.setSelected(true);
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = nomeUtenteTF.getText();
        String password = passwordTF.getText();

        // Verifica che i campi non siano vuoti
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Errore", "Username e password non possono essere vuoti.");
            return;
        }

        if (signInBtn.isSelected()) {
            try {
                // Logica per il login
                // Qui dovresti aggiungere la logica per autenticare l'utente
                showAlert("Login", "Login effettuato con successo!");
                // Esempio di redirect:
                // if(type.equals(UserType.ADMIN)) {
                //     Wordageddon.setRoot("AdminPageView");
                // }
            } catch (Exception e) {
                showAlert("Errore", "Login fallito: " + e.getMessage());
            }
        } else if (signUpBtn.isSelected()) {
            try {
                // Logica per la registrazione
                // Qui dovresti aggiungere la logica per registrare un nuovo utente
                showAlert("Registrazione", "Registrazione effettuata con successo!");
            } catch (Exception e) {
                showAlert("Errore", "Registrazione fallita: " + e.getMessage());
            }
        }
    }

    // Metodo per mostrare un messaggio di avviso sullo schermo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
