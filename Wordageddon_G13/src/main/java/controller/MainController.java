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
import model.User;
import model.enums.Difficulty;
import model.enums.UserType;

import java.io.IOException;
import java.net.URL;
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

    public void setUser (User user){
        this.user = user;
    }

  /**
    * @brief al click questo metodo cambia pagina e ti porta alla pagina per visualizzare gli score
    * @param event
    */
   @FXML
  void Button_SetScoresPage(ActionEvent event) {
       try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ScoreView.fxml"));
           Parent scorePage = loader.load();
           root.setCenter(scorePage);

           controllerScore = loader.getController();//salvo il controller dello score per cambiare la tabella dopo

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

    @FXML
    void Button_SetAdmin(ActionEvent event) {
        try{
            Parent adminPage = FXMLLoader.load(getClass().getResource("/view/AdminView.fxml"));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
       controllerScore.setUser(user);
        controllerScore.showPlayerScores(Difficulty.EASY);
    }
}
