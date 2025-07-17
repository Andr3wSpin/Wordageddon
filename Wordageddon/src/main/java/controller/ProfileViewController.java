package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import model.db.WordageddonDAOSQLite;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.layout.StackPane;

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
    private Button ChangeDataButton1;

    @FXML
    private Label LabelDelete;

    @FXML
    private Label Label_ChangeData;

    @FXML
    private MediaView MediaViewWallPaper;

    @FXML
    private Text Welcome;

    @FXML
    private Button confirmButton;

    @FXML
    private StackPane deleteAccountButton;

    @FXML
    private ImageView iconChangeInfo;

    @FXML
    private ImageView iconDelete;

    @FXML
    private ImageView iconLogOut;

    @FXML
    private Label labelLogOut;

    @FXML
    private Label newAttributeLabel;

    @FXML
    private TextField newAttributeTextField;

    @FXML
    private RadioButton passwordToggle;

    @FXML
    private Text textField_NomeUser;

    @FXML
    private RadioButton usernameToggle;

    private ToggleGroup dataToggles;

    private User user;

    private WordageddonDAOSQLite accessDB;



    @FXML
    private Label Label_ChangeData1;



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
        newAttributeLabel.setText("Nuova Password:");
    }

    /**
     * Imposta il testo dell'etichetta del nuovo attributo su "Nuovo Username:".
     */
    @FXML
    private void usernameOnLabel(){
        newAttributeLabel.setText("Nuovo Username:");
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
                showAlert("Errore di aggiornamento","Inserisci una password valida.");
            else
                showAlert("Successo", "Password aggiornata con successo.");
        } else if (dataToggles.getSelectedToggle() == usernameToggle) {
            if(!accessDB.updateUser("username",user.getID(),newValue))
                showAlert("Errore di aggiornamento","Inserisci uno username valido.");
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
     * Chiama il metodo {@link #logOut()} per effettuare il logout.
     * @param event L'evento di azione che ha scatenato il metodo.
     */
    @FXML
    private void LogOut_Button(ActionEvent event) {
        logOut();
    }

    /**
     * Effettua il logout dell'utente.
     * Carica la vista di autenticazione (`AuthView.fxml`) e imposta la nuova scena,
     * reindirizzando l'utente alla schermata di login.
     * Gestisce eventuali {@link IOException} che possono verificarsi durante il caricamento del FXML.
     */
    private void logOut() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
        Parent loginPage;
        try {
            loginPage = loader.load();
        } catch (IOException e) {

            throw new RuntimeException("Impossibile caricare la vista di autenticazione.", e);
        }

        Stage stage = (Stage) Button_LogOut.getScene().getWindow();
        Scene newScene = new Scene(loginPage);
        stage.setScene(newScene);
        stage.show();
    }

    /**
     * Imposta l'utente corrente per la vista del profilo.
     * Aggiorna il campo di testo `textField_NomeUser` con lo username dell'utente.
     * @param user L'oggetto {@link User} da impostare.
     */
    public void setUser(User user){
        this.user = user;
        textField_NomeUser.setText(user.getUsername());
    }

    /**
     * Inizializza il controller dopo che il suo elemento radice è stato completamente processato.
     * Inizializza l'accesso al database, riproduce il video di sfondo,
     * configura i toggle button per la selezione dei dati da modificare e nasconde
     * inizialmente i campi di modifica dati.
     * @param location L'URL della posizione del file FXML, o {@code null} se sconosciuto.
     * @param resources Le risorse specifiche della localizzazione, o {@code null} se non usate.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accessDB = new WordageddonDAOSQLite();

        // Configura e riproduce il video di sfondo
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Riproduzione in loop
        mediaPlayer.play();

        // Inizializza il ToggleGroup per i RadioButton di selezione dati
        dataToggles = new ToggleGroup();
        usernameToggle.setToggleGroup(dataToggles);
        passwordToggle.setToggleGroup(dataToggles);

        // Nasconde inizialmente i campi e i pulsanti per la modifica dei dati
        confirmButton.setVisible(false);
        newAttributeLabel.setVisible(false);
        newAttributeTextField.setVisible(false);
        usernameToggle.setVisible(false);
        passwordToggle.setVisible(false);
    }

    /**
     * Mostra una finestra di alert all'utente con un messaggio informativo.
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

    /**
     * Gestisce l'evento di click sul pulsante di eliminazione dell'account.
     * Mostra una finestra di conferma all'utente prima di procedere con l'eliminazione.
     * Se l'utente conferma, l'account viene eliminato dal database e l'utente viene disconnesso.
     * @param event L'evento di azione che ha scatenato il metodo.
     */
    @FXML
    private void deleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminazione account");
        alert.setContentText("Sei sicuro di voler eliminare l'account? Questa operazione è irreversibile.");

        Optional<ButtonType> choice = alert.showAndWait();

        if(choice.isPresent() && choice.get() == ButtonType.OK) {
            // Elimina l'utente dal database
            accessDB.deleteUser(user.getID());
            // Effettua il logout dopo l'eliminazione
            logOut();
        }
        else {
            // Se l'utente annulla, consuma l'evento per evitare ulteriori azioni
            event.consume();
        }
    }


    /**
     * gestione animazioni pulsanti entred
     * @param x icona da animare
     * @param y label da animare
     *
     */
    private void buttonHoverEntredAnimator(ImageView x,Label y){

        TranslateTransition textSlide = new TranslateTransition(Duration.millis(500),y);
        textSlide.setFromX(0);
        textSlide.setToX(100);
        textSlide.play();

        FadeTransition textOpacity = new FadeTransition(Duration.millis(500),y);
        textOpacity.setFromValue(1.0);
        textOpacity.setToValue(0.0);
        textOpacity.play();

        TranslateTransition imageSlide = new TranslateTransition(Duration.millis(300),x);
        imageSlide.setFromX(0);
        imageSlide.setToX(35);
        imageSlide.play();



    }

    /**
     * gestione animazioni pulsanti exit
     * @param x icona da animare
     * @param y label da animare
     *
     */
    private void buttonHoverExitAnimator(ImageView x,Label y){

        TranslateTransition textSlide = new TranslateTransition(Duration.millis(500),y);
        textSlide.setFromX(100);
        textSlide.setToX(0);
        textSlide.play();

        FadeTransition textOpacity = new FadeTransition(Duration.millis(500),y);
        textOpacity.setFromValue(0);
        textOpacity.setToValue(1);
        textOpacity.play();

        TranslateTransition imageSlide = new TranslateTransition(Duration.millis(300),x);
        imageSlide.setFromX(35);
        imageSlide.setToX(0);
        imageSlide.play();



    }



    @FXML
    void hoverButtonChangeInfoEntred(MouseEvent event) {
                buttonHoverEntredAnimator(iconChangeInfo,Label_ChangeData);
    }

    @FXML
    void hoverButtonChangeInfoExit(MouseEvent event) {
        buttonHoverExitAnimator(iconChangeInfo,Label_ChangeData);
    }

    @FXML
    void hoverButtonDeleteEntred(MouseEvent event) {
        buttonHoverEntredAnimator(iconDelete,LabelDelete);
    }

    @FXML
    void hoverButtonDeleteExit(MouseEvent event) {
        buttonHoverExitAnimator(iconDelete,LabelDelete);
    }

    @FXML
    void hoverButtonLogOutEntred(MouseEvent event) {
          buttonHoverEntredAnimator(iconLogOut,labelLogOut);
    }

    @FXML
    void hoverButtonLogOutExit(MouseEvent event) {
        buttonHoverExitAnimator(iconLogOut,labelLogOut);
    }
}