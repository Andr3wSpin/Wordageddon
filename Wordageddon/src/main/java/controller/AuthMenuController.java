package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;
import model.db.WordageddonDAO;
import model.db.WordageddonDAOSQLite;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
    private PasswordField passwordPF;

    @FXML
    private ToggleButton showPasswordBtn;

    private TextField passwordVisibleTF;

    @FXML
    private Label wordaggedonLabel;

    @FXML
    private Button loginButton;

    /**
     * Inizializza il controller. Sincronizza i campi password visibile/nascosta.
     */
    @FXML
    public void initialize() {
        passwordVisibleTF = new TextField();
        passwordVisibleTF.setPromptText("Password");
        passwordVisibleTF.setStyle("-fx-background-radius: 10; -fx-background-color: #eeeeee; -fx-padding: 10;");

        // Inserisce il campo visibile accanto a quello nascosto nel layout
        HBox parent = (HBox) passwordPF.getParent();
        parent.getChildren().add(0, passwordVisibleTF);

        // Binding di visibilità e gestione layout
        passwordVisibleTF.managedProperty().bind(showPasswordBtn.selectedProperty());
        passwordVisibleTF.visibleProperty().bind(showPasswordBtn.selectedProperty());

        passwordPF.managedProperty().bind(showPasswordBtn.selectedProperty().not());
        passwordPF.visibleProperty().bind(showPasswordBtn.selectedProperty().not());

        // Sincronizza il testo tra i due campi
        passwordVisibleTF.textProperty().bindBidirectional(passwordPF.textProperty());
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
            System.err.println(e.getMessage());
            showAlert("Errore", "Errore nel caricamento della pagina di registrazione.");
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = nomeUtenteTF.getText();
        String password = showPasswordBtn.isSelected() ? passwordVisibleTF.getText() : passwordPF.getText();

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
