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

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class  ProfileViewController implements Initializable {

    @FXML
    private CheckBox AdminCheckBox;

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
    private Text welcome;

    @FXML
    private ImageView image_Logout;

    @FXML
    private Label labelLogOut;

    @FXML
    private Text textField_admin;

    @FXML
    private ToggleGroup dataToggles;

    private User user;

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

    @FXML
    private void passwordOnLabel(){
        newAttributeLabel.setText("New Password:");
    }

    @FXML
    private void usernameOnLabel(){
        newAttributeLabel.setText("New username:");
    }

    @FXML
    private void confirmData(){
        if(dataToggles.getSelectedToggle() == passwordToggle){
            //change password
        } else if (dataToggles.getSelectedToggle() == usernameToggle) {
            //change username
        }else {
            showAlert("Error", "Please select an option.");
        }
    }

    @FXML
    private void LogOut_Button(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AuthView.fxml"));
        Parent loginPage = loader.load();

        Stage stage = (Stage) Button_LogOut.getScene().getWindow();
        Scene newScene = new Scene(loginPage);
        stage.setScene(newScene);
        stage.show();
    }

    @FXML
    private void becameAdmin(ActionEvent event) {

    }
    public void setUser(User user){
        this.user=user;
        textField_NomeUser.setText(user.getUsername());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}