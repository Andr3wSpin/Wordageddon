package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.awt.*;
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
    private Button NavigationBarItem_Scores;
    @FXML
    private BorderPane root;
    private   Parent gamePage;
    private  Parent scorePage;
    Parent adminPage;



    @FXML
    void Button_SetPageAdmin(ActionEvent event) {
       try{

            adminPage = FXMLLoader.load(getClass().getResource("/view/AdminView.fxml"));
            root.setCenter(adminPage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void setGamePage(ActionEvent event) {
   try{
       gamePage = FXMLLoader.load(getClass().getResource("/view/StartGameView.fxml"));
       root.setCenter(gamePage);

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
       try{
          scorePage = FXMLLoader.load(getClass().getResource("/view/ScoreView.fxml"));
          root.setCenter(scorePage);

        } catch (IOException e) {
           throw new RuntimeException(e);
      }
   }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Parent gamePage = FXMLLoader.load(getClass().getResource("/view/StartGameView.fxml"));
            root.setCenter(gamePage);

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

    }


}
