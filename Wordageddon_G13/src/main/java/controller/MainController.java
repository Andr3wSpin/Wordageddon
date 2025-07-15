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
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private HBox NavigationBar;

    @FXML
    private Button NavigationBarItem_Admin;

    @FXML
    private Button NavigationBarItem_Gioca;

    @FXML
    private Button NavigationBarItem_Profile;

    @FXML
    private Button NavigationBarItem_Scores;

    @FXML
    private BorderPane root;

    @FXML
    private HBox ScoreNavigationButton;

    @FXML
    private Button button_GlobalScorres;

    @FXML
    private Button button_yoursScore;
    @FXML
    private Button Button_RemoveFiles;

    @FXML
    private Button Button_StartAnalisys;

    @FXML
    private Button button_LoadFiles;
    @FXML
    private Label LableButton_RemoveFiles;

    @FXML
    private Label LableButton_StartAnalisys;

    @FXML
    private Label LableButton_loadFiles;
    @FXML
    private VBox NavigationBarAdmin;

    private ScoreController controllerScore;
    private User user;

    private AdminController controllerAdmin;
    private FileAnalysis fa;


    public void setUser (User user){
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fa = new FileAnalysis();
        try {
            fa = fa.readAnalysis();
            System.out.println("ANALISI LETTA:\n" + fa.getAnalysis());
        } catch (IOException e) {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }

        setCSS();

        loadGamePage();
    }

    /**
     * Imposta l'hover sui pulsanti
     */
    private void setCSS() {

        for (Node node : NavigationBar.getChildren()) {

            Button button = (Button)node;
            button.setOnMouseEntered(e -> node.setStyle("-fx-text-fill: red; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-color: transparent; " +
                    "-fx-font-size: 17px; " +
                    "-fx-font-family: 'Microsoft YaHei';"));
            button.setOnMouseExited(e -> node.setStyle("-fx-text-fill: white; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-color: transparent; " +
                    "-fx-font-size: 17px; " +
                    "-fx-font-family: 'Microsoft YaHei';"));

        }
        for (Node node : ScoreNavigationButton.getChildren()) {

            Button button = (Button)node;
            button.setOnMouseEntered(e -> node.setStyle("-fx-text-fill: red; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-color: transparent; " +
                    "-fx-font-size: 25px; " +
                    "-fx-font-family: 'Microsoft YaHei';"));
            button.setOnMouseExited(e -> node.setStyle("-fx-text-fill: white; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-color: transparent; " +
                    "-fx-font-size: 25px; " +
                    "-fx-font-family: 'Microsoft YaHei';"));
        }
    }

    @FXML
    void setProfilePage(ActionEvent event) {
       try{

           Parent  profilePage= FXMLLoader.load(getClass().getResource("/view/ProfileView.fxml"));
           root.setCenter(profilePage);
           button_yoursScore.setVisible(false);
           button_GlobalScorres.setVisible(false);
           button_LoadFiles.setVisible(false);
           Button_RemoveFiles.setVisible(false);
           Button_StartAnalisys.setVisible(false);

           root.setRight(null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void setGamePage(ActionEvent event) {

        loadGamePage();
    }

  /**
    * Al click questo metodo cambia pagina e ti porta alla pagina per visualizzare gli score
    *
    */
   @FXML
  void Button_SetScoresPage(ActionEvent event) {
       loadScorePage();
   }

    @FXML
    void Button_SetAdmin(ActionEvent event) {
       loadAdminPage();
    }

    @FXML
    void ShowGlobalScores(ActionEvent event) {

        controllerScore.showLeaderboard(Difficulty.EASY);
    }
    @FXML
    void showYourScore(ActionEvent event) {
        controllerScore.setUser(user);
        controllerScore.showPlayerScores(Difficulty.EASY);


    }

    @FXML
    void LoadFilesAdminPage(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("File di testo (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if(selectedFiles == null) return;

        try {
            FileManager.addFiles(selectedFiles);
            loadAdminPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void button_RemoveFiles(ActionEvent event) {
      controllerAdmin.RemoveSelectedFile();
      loadAdminPage();
    }


    private void startAnalysis() {
        fa.setOnSucceeded(e -> {

            showMessage("Analisi completata.", Alert.AlertType.INFORMATION);
            System.out.println("MAPPA:\n" + fa.getValue());
            fa.reset();

        });

        fa.setOnFailed( e->{

            showMessage("Impossibile analizzare i file!", Alert.AlertType.ERROR);
            fa.reset();
        });

        fa.start();
    }
    @FXML
    void button_StartAnalisys(ActionEvent event) {

       startAnalysis();
    }

    private void loadAdminPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
            Parent adminPage = loader.load();


            controllerAdmin = loader.getController();

            // Mostra la pagina admin
            root.setCenter(adminPage);
            button_yoursScore.setVisible(false);
            button_GlobalScorres.setVisible(false);
            button_LoadFiles.setVisible(true);
            Button_RemoveFiles.setVisible(true);
            Button_StartAnalisys.setVisible(true);
            root.setRight(NavigationBarAdmin);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


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
            root.setRight(null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadGamePage(){

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StartGameView.fxml"));
            Parent gamePage = loader.load();
            root.setCenter(gamePage);

            StartGameMenuController sgController = loader.getController();
            sgController.setUser(new User(2, "Andrea", "BB", UserType.PLAYER));
            sgController.setFileAnalysis(fa.getAnalysis());

            button_yoursScore.setVisible(false);
            button_GlobalScorres.setVisible(false);
            button_LoadFiles.setVisible(false);
            Button_RemoveFiles.setVisible(false);
            Button_StartAnalisys.setVisible(false);

            root.setRight(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mostra all'utente il messaggio ricevuto
     * @param msg Messaggio da mostrare all'utente
     * @param type Tipo di messaggio
     */
    private void showMessage(String msg, Alert.AlertType type) {

        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
