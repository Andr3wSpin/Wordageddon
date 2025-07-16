package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.db.WordageddonDAOSQLite;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.layout.StackPane;

import javax.swing.plaf.metal.MetalBorders;

/**
 * Controller per la vista del profilo utente.
 * Gestisce le interazioni dell'utente nella schermata del profilo,
 * incluse le modifiche dei dati utente e il logout.
 */
public class ProfileViewController implements Initializable {

    @FXML
    private Button Button_LogOut;

    @FXML
    private Button ChangeDataButton;

    @FXML
    private Label Label_ChangeData;

    @FXML
    private MediaView MediaViewWallPaper;

    @FXML
    private Text textField_NomeUser;

    @FXML
    private TextField newAttributeTextField;

    @FXML
    private Button confirmButton;

    @FXML
    private Label newAttributeLabel;

    @FXML
    private RadioButton passwordToggle;

    @FXML
    private RadioButton usernameToggle;


    @FXML
    private ImageView image_Logout;

    @FXML
    private Label labelLogOut;

    private ToggleGroup dataToggles;

    private User user;

    private WordageddonDAOSQLite accessDB;
    @FXML
    private Text Welcome;
    @FXML
    private Label Label_ChangeData1;
    @FXML
    private Button ChangeDataButton1;
    @FXML
    private StackPane deleteAccountButton;
    @FXML
    private Label Label_ChangeData11;

    /**
     * Gestisce l'evento di click sul bottone "Cambia Dati".
     * Rende visibili i campi e i pulsanti per la modifica dei dati utente.
     * Preseleziona l'opzione di modifica della password.
     * @param event L'evento di azione che ha scatenato il metodo.
     */
    @FXML
    void ChangeDataButton(ActionEvent event) {
        confirmButton.setVisible(true);
        newAttributeLabel.setVisible(true);
        newAttributeTextField.setVisible(true);
        usernameToggle.setVisible(true);
        passwordToggle.setVisible(true);
        dataToggles.selectToggle(passwordToggle);
        passwordOnLabel();
    }

    /**
     * Imposta il testo dell'etichetta del nuovo attributo su "Nuova Password:".
     */
    @FXML
    private void passwordOnLabel(){
        newAttributeLabel.setText("New Password:");
    }

    /**
     * Imposta il testo dell'etichetta del nuovo attributo su "Nuovo Username:".
     */
    @FXML
    private void usernameOnLabel(){
        newAttributeLabel.setText("New username:");
    }

    /**
     * Gestisce la conferma della modifica dei dati utente.
     * Verifica che il campo di input non sia vuoto e procede con l'aggiornamento
     * dello username o della password nel database in base alla selezione.
     * Mostra un alert di successo o di errore.
     */
    @FXML
    private void confirmData(){
        String newValue = newAttributeTextField.getText().trim();
        if (newValue.isEmpty()) {
            showAlert("Errore di input", "Il campo non può essere vuoto.");
            return;
        }

        if(dataToggles.getSelectedToggle() == passwordToggle){
            if(!accessDB.updateUser("password",user.getID(),newValue))
                showAlert("Errore di aggiornamento","Inserisci una password valida");
            else
                showAlert("Successo", "Password aggiornata con successo.");
        } else if (dataToggles.getSelectedToggle() == usernameToggle) {
            if(!accessDB.updateUser("username",user.getID(),newValue))
                showAlert("Errore di aggiornamento","Inserisci uno username valido");
            else {
                textField_NomeUser.setText(newValue);
                showAlert("Successo", "Username aggiornato con successo.");
            }
        }else {
            showAlert("Errore", "Seleziona un'opzione.");
        }
    }

    /**
     * Gestisce l'evento di click sul bottone "Log Out".
     * Carica la vista di autenticazione e cambia la scena corrente,
     * effettuando il logout dell'utente.
     * @param event L'evento di azione che ha scatenato il metodo.
     * @throws IOException Se si verifica un errore durante il caricamento della vista.
     */
    @FXML
    private void LogOut_Button(ActionEvent event) throws IOException {

        logOut();
    }

    private void logOut() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
        Parent loginPage = null;
        try {
            loginPage = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) Button_LogOut.getScene().getWindow();
        Scene newScene = new Scene(loginPage);
        stage.setScene(newScene);
        stage.show();
    }


    /**
     * Imposta l'utente corrente per la vista del profilo.
     * Aggiorna il campo di testo con il nome utente.
     * @param user L'oggetto User da impostare.
     */
    public void setUser(User user){
        this.user=user;
        textField_NomeUser.setText(user.getUsername());
    }

    /**
     * Inizializza il controller.
     * Inizializza l'accesso al database, riproduce il video di sfondo,
     * configura i toggle button e nasconde i campi di modifica dati iniziali.
     * @param location L'URL della posizione del file FXML.
     * @param resources Le risorse specifiche della localizzazione.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accessDB = new WordageddonDAOSQLite();


        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Riproduzione in loop
        mediaPlayer.play();


        dataToggles = new ToggleGroup();
        usernameToggle.setToggleGroup(dataToggles);
        passwordToggle.setToggleGroup(dataToggles);


        confirmButton.setVisible(false);
        newAttributeLabel.setVisible(false);
        newAttributeTextField.setVisible(false);
        usernameToggle.setVisible(false);
        passwordToggle.setVisible(false);
    }



    /**
     * Mostra una finestra di alert all'utente.
     * @param title Il titolo dell'alert.
     * @param message Il messaggio da visualizzare nell'alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Nessun header per un alert semplice
        alert.setContentText(message);
        alert.showAndWait(); // Attende che l'utente chiuda l'alert
    }

    @FXML
    private void deleteAccount(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elimiazione account");
        alert.setContentText("Sei sicuro di voler eliminare l'account?");

        Optional<ButtonType> choice = alert.showAndWait();

        if(choice.isPresent() && choice.get() == ButtonType.OK) {

            accessDB.deleteUser(user.getID());
            logOut();
        }
        else event.consume();
    }
}