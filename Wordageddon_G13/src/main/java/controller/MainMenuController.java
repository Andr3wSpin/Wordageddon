package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button Button_StartGame;

    @FXML
    private Label LableButton_StartGame;

    @FXML
    private MediaView MediaViewWallPaper;

    @FXML
    private HBox NavigationBar;

    @FXML
    private Button NavigationBarItem_Admin;

    @FXML
    private Button NavigationBarItem_Gioca;

    @FXML
    private Button NavigationBarItem_Scores;

    @FXML
    private RadioButton RadioButton_Easy;

    @FXML
    private RadioButton RadioButton_Hard;

    @FXML
    private RadioButton RadioButton_Medium;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());


        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }




    /**
     * @brief al click questo metodo cambia pagina e ti porta alla pagina per inizare a giocare
     * @param event
     */

    @FXML
    void Button_SetGamePage(ActionEvent event) {

    }
    /**
     * @brief al click questo metodo cambia pagina e ti porta alla pagina di gestione dell admin
     * @param event
     */
    @FXML
    void Button_SetPageAdmin(ActionEvent event) {

    }
    /**
     * @brief al click questo metodo cambia pagina e ti porta alla pagina per visualizzare gli score
     * @param event
     */
    @FXML
    void Button_SetScoresPage(ActionEvent event) {

    }


    /**
     * @brief al click fa inizare la partita solo dopo aver selezionato la difficolta
     * @param event
     */
    @FXML
    void Button_StartGame(ActionEvent event) {

    }




















    /**
     * tutti questi metodi servono per gestire le animazionid ei vari pulsanti in hover
     */

    @FXML
    void HoverButtonEntred_AdminPageButton(MouseEvent event) {

    }

    @FXML
    void HoverButtonEntred_GamePageButton(MouseEvent event) {

    }

    @FXML
    void HoverButtonEntred_ScoresPageButton(MouseEvent event) {

    }

    @FXML
    void HoverButtonEntred_startButton(MouseEvent event) {

    }

    @FXML
    void HoverButtonExit_AdminPageButton(MouseEvent event) {

    }

    @FXML
    void HoverButtonExit_GamePageButton(MouseEvent event) {

    }

    @FXML
    void HoverButtonExit_ScoresPageButton(MouseEvent event) {

    }

    @FXML
    void HoverButtonExit_startButton(MouseEvent event) {

    }


}
