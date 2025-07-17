package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;
import model.enums.Difficulty;
import model.enums.UserType;
import model.files_management.FileAnalysis;
import model.files_management.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller principale dell'applicazione.
 * Gestisce la navigazione tra le pagine, l'interazione con i file,
 * e le operazioni disponibili per utenti admin e normali.
 */
public class MainController implements Initializable {


    @FXML private HBox NavigationBar;

    @FXML private Button NavigationBarItem_Admin;

    @FXML private Button NavigationBarItem_Gioca;

    @FXML private Button NavigationBarItem_Profile;

    @FXML private Button NavigationBarItem_Scores;

    @FXML private BorderPane root;

    @FXML private HBox ScoreNavigationButton;

    @FXML private Button button_GlobalScorres;

    @FXML private Button button_yoursScore;

    @FXML private Button Button_RemoveFiles;

    @FXML private Button Button_StartAnalisys;

    @FXML private Button button_LoadFiles;

    @FXML private Label LableButton_RemoveFiles;

    @FXML private Label LableButton_StartAnalisys;

    @FXML private Label LableButton_loadFiles;


    @FXML private VBox NavigationBarAdmin;


    @FXML private TextArea areaStopWords;


    @FXML private Button button_loadStopwords;


    @FXML private VBox stopWordsView;

    private ScoreController controllerScore;
    private AdminController controllerAdmin;
    private User user;
    private FileAnalysis fa;

    /**
     * Metodo chiamato all'inizializzazione del controller.
     * Carica l'analisi file salvata e imposta lo stile CSS dinamico.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fa = new FileAnalysis();


        try {
            fa = fa.readAnalysis();

            if(!fa.getStopwords().isEmpty())
                areaStopWords.setText( fa.getStopwords().toString()  );
        } catch (IOException e) {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }

        setCSS();
    }

    /**
     * Inizializza il controller con l'utente autenticato.
     * Imposta la visibilità della sezione admin e carica la pagina di gioco.
     *
     * @param user Utente autenticato
     */
    public void init(User user) {
        this.user = user;

        if (user.getType() != UserType.ADMIN) {
            NavigationBarItem_Admin.setManaged(false);
            NavigationBarItem_Admin.setVisible(false);
            NavigationBarItem_Admin.setDisable(true);
        }

        loadGamePage();
    }

    /**
     * Applica gli stili CSS dinamici ai bottoni della barra di navigazione.
     */
    private void setCSS() {
        for (Node node : NavigationBar.getChildren()) {
            Button button = (Button) node;
            button.setOnMouseEntered(e -> node.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 17px; -fx-font-family: 'Microsoft YaHei';"));
            button.setOnMouseExited(e -> node.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 17px; -fx-font-family: 'Microsoft YaHei';"));
        }
        for (Node node : ScoreNavigationButton.getChildren()) {
            Button button = (Button) node;
            button.setOnMouseEntered(e -> node.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Microsoft YaHei';"));
            button.setOnMouseExited(e -> node.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: transparent; -fx-font-size: 25px; -fx-font-family: 'Microsoft YaHei';"));
        }
    }

    /**
     * Carica la pagina del profilo utente.
     */
    @FXML
    void setProfilePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProfileView.fxml"));
            Parent profilePage = loader.load();

            ProfileViewController profileController = loader.getController();
            profileController.setUser(user);

            root.setCenter(profilePage);
            button_yoursScore.setVisible(false);
            button_GlobalScorres.setVisible(false);
            button_LoadFiles.setVisible(false);
            Button_RemoveFiles.setVisible(false);
            Button_StartAnalisys.setVisible(false);

            root.setLeft(null);
            root.setRight(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carica la pagina del gioco.
     */
    @FXML
    void setGamePage(ActionEvent event) {
        loadGamePage();
    }

    /**
     * Carica la pagina dei punteggi.
     */
    @FXML
    void Button_SetScoresPage(ActionEvent event) {
        loadScorePage();
    }

    /**
     * Carica la pagina admin se l'utente è autorizzato.
     */
    @FXML
    void Button_SetAdmin(ActionEvent event) {
        loadAdminPage();
    }

    /**
     * Mostra i punteggi globali.
     */
    @FXML
    void ShowGlobalScores(ActionEvent event) {
        controllerScore.showLeaderboard(Difficulty.EASY);
    }

    /**
     * Mostra i punteggi dell'utente corrente.
     */
    @FXML
    void showYourScore(ActionEvent event) {
        controllerScore.setUser(user);
        controllerScore.showPlayerScores(Difficulty.EASY);
    }

    /**
     * Carica nuovi file da analizzare tramite FileChooser.
     */
    @FXML
    void LoadFilesAdminPage(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di testo (*.txt)", "*.txt"));

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles == null) return;

        try {
            FileManager.addFiles(selectedFiles);
            loadAdminPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rimuove i file selezionati nella sezione admin.
     */
    @FXML
    void button_RemoveFiles(ActionEvent event) {
        controllerAdmin.RemoveSelectedFile();
        loadAdminPage();
    }

    /**
     * Avvia l'analisi dei file caricati.
     */
    @FXML
    void button_StartAnalisys(ActionEvent event) {
        startAnalysis();
    }

    /**
     * Carica la pagina admin e aggiorna i controlli visibili.
     */
    private void loadAdminPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
            Parent adminPage = loader.load();

            controllerAdmin = loader.getController();

            root.setCenter(adminPage);
            button_yoursScore.setVisible(false);
            button_GlobalScorres.setVisible(false);
            button_LoadFiles.setVisible(true);
            Button_RemoveFiles.setVisible(true);
            Button_StartAnalisys.setVisible(true);
            root.setRight(NavigationBarAdmin);
            root.setLeft(stopWordsView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carica la pagina dei punteggi.
     */
    private void loadScorePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ScoreView.fxml"));
            Parent scorePage = loader.load();

            controllerScore = loader.getController();

            root.setCenter(scorePage);
            button_yoursScore.setVisible(true);
            button_GlobalScorres.setVisible(true);
            button_LoadFiles.setVisible(false);
            Button_RemoveFiles.setVisible(false);
            Button_StartAnalisys.setVisible(false);
            root.setBottom(ScoreNavigationButton);
            root.setLeft(null);
            root.setRight(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carica la pagina del gioco e inizializza i dati utente.
     */
    private void loadGamePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StartGameView.fxml"));
            Parent gamePage = loader.load();

            StartGameMenuController sgController = loader.getController();
            sgController.setUser(user);
            sgController.setFileAnalysis(fa.getAnalysis());

            root.setCenter(gamePage);
            button_yoursScore.setVisible(false);
            button_GlobalScorres.setVisible(false);
            button_LoadFiles.setVisible(false);
            Button_RemoveFiles.setVisible(false);
            Button_StartAnalisys.setVisible(false);
            root.setLeft(null);
            root.setRight(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carica le stopword inserite manualmente e avvia una nuova analisi.
     */
    @FXML
    void loadStopWords(ActionEvent event) {
        String area = areaStopWords.getText();
        List<String> stopwords = Arrays.stream(area.split("[\\s,\\.\\[\\];\\-]++")).filter( f-> !f.trim().isEmpty()  ).collect(Collectors.toList());
        fa.getStopwords().clear();
        fa.getStopwords().addAll(stopwords);
        areaStopWords.setText( fa.getStopwords().toString() );
        startAnalysis();
    }

    /**
     * Esegue l'analisi sui file, gestendo il risultato e mostrando feedback all'utente.
     */
    private void startAnalysis() {

        if (fa.isRunning()) {
            showMessage("Analisi già in corso, attendere...", Alert.AlertType.INFORMATION);
            return;
        }

        fa.setOnSucceeded(e -> {
            showMessage("Analisi completata.", Alert.AlertType.INFORMATION);
            System.out.println("MAPPA:\n" + fa.getValue());
            fa.reset();
        });

        fa.setOnFailed(e -> {
            showMessage("Impossibile analizzare i file!", Alert.AlertType.ERROR);
            fa.reset();
        });

        fa.start();
    }


    /**
     * Mostra un messaggio all'utente tramite un alert.
     *
     * @param msg  Messaggio da mostrare
     * @param type Tipo di alert (INFORMATION, ERROR, ecc.)
     */
    private void showMessage(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
