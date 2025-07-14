package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
    private FileAnalysis fa = new FileAnalysis();


    public void setUser (User user){
        this.user = user;
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
   try{
       Parent gamePage = FXMLLoader.load(getClass().getResource("/view/StartGameView.fxml"));
       root.setCenter(gamePage);
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
    * @brief al click questo metodo cambia pagina e ti porta alla pagina per visualizzare gli score
    * @param event
    */
   @FXML
  void Button_SetScoresPage(ActionEvent event) {
       loadScorePage();
   }

    @FXML
    void Button_SetAdmin(ActionEvent event) {
       loadAdminPage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

       loadGamePage();

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
    void ShowGlobalScores(ActionEvent event) {

        controllerScore.showLeaderboard(Difficulty.EASY);
    }
    @FXML
    void showYourScore(ActionEvent event) {
        controllerScore.showPlayerScores(Difficulty.EASY);
        controllerScore.setUser(user);
    }

    @FXML
    void LoadFilesAdminPage(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        try {
            FileManager.addFiles(fileChooser.showOpenMultipleDialog(stage));
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

    @FXML
    void button_StartAnalisys(ActionEvent event) {


            fa.setOnSucceeded(e -> {
                try {
                    List<File> totFile = FileManager.getFiles();
                    for (File f : totFile) {
                        fa.analyzeFile(f);
                    }
                    loadAdminPage();
                } catch (IOException ex) {
                    System.out.println("errore in start Analysis");

                    throw new RuntimeException(ex);
                }

            });

            fa.setOnFailed( e->{


                System.out.println("erore imposisbile analizzare");
                fa.reset();

            });
            fa.start();


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
            Parent gamePage = FXMLLoader.load(getClass().getResource("/view/StartGameView.fxml"));
            root.setCenter(gamePage);
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
}
