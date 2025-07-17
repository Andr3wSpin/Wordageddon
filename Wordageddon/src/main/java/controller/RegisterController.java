package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.User;
import model.db.WordageddonDAOSQLite;

import java.io.IOException;

/**
 * Controller per la vista di registrazione utente.
 */
public class RegisterController {

    @FXML
    private TextField nomeUtenteTF;

    @FXML
    private PasswordField passwordPF;

    @FXML
    private TextField passwordTF;

    @FXML
    private ToggleButton showPasswordBtn;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private Button registerButton;

    private final WordageddonDAOSQLite dao = new WordageddonDAOSQLite();

    @FXML
    private void initialize() {
        // Sincronizza visibilità tra passwordPF e passwordTF
        passwordTF.managedProperty().bind(showPasswordBtn.selectedProperty());
        passwordTF.visibleProperty().bind(showPasswordBtn.selectedProperty());

        passwordPF.managedProperty().bind(showPasswordBtn.selectedProperty().not());
        passwordPF.visibleProperty().bind(showPasswordBtn.selectedProperty().not());

        // Sincronizza il testo tra entrambi
        passwordTF.textProperty().bindBidirectional(passwordPF.textProperty());
    }

    @FXML
    private void handleRegisterButtonAction(javafx.event.ActionEvent event) {
        String username = nomeUtenteTF.getText();
        String password = passwordPF.getText(); // passwordTF è sincronizzato, quindi vale lo stesso
        boolean isAdmin = adminCheckBox.isSelected();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Per favore compila tutti i campi.");
            return;
        }

        try {
            User newUser = dao.insertUser(username, password, isAdmin);
            if (newUser == null) {
                showAlert(Alert.AlertType.ERROR, "Errore: l'utente esiste già o non è stato possibile registrarsi.");
            } else {
                switchToLoginView();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore durante la registrazione: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLoginClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore nel caricamento del login: " + e.getMessage());
        }
    }

    private void switchToLoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) nomeUtenteTF.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Registrazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
