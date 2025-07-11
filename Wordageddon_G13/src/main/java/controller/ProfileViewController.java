package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class  ProfileViewController implements Initializable {

    @FXML
    private CheckBox AdminCheckBox;

    @FXML
    private Button Button_LogOut;

    @FXML
    private Button CahneDataButton;

    @FXML
    private Label Lable_ChangeData;

    @FXML
    private MediaView MediaViewWallPaper;

    @FXML
    private Text TextField_NomeUser;

    @FXML
    private Text Welcome;

    @FXML
    private ImageView image_Logout;

    @FXML
    private Label labelLogOut;

    @FXML
    private Text textField_admin;

    @FXML
    void CahngeDataButton(ActionEvent event) {

    }

    @FXML
    void LogOut_Buttton(ActionEvent event) {

    }

    @FXML
    void becameAdmin(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

    }
}