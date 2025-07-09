package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

public class AuthMenuController {

    @FXML
    private Pane loginBtn;

    @FXML
    private TextField nomeUtenteTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private RadioButton signInBtn;

    @FXML
    private RadioButton signUpBtn;

    @FXML
    private Label wordaggedonLabel;

    @FXML
    private Button loginButton;

    private ToggleGroup loginToggleGroup;

    @FXML
    public void initialize() {
        loginToggleGroup = new ToggleGroup();
        signInBtn.setToggleGroup(loginToggleGroup);
        signUpBtn.setToggleGroup(loginToggleGroup);

        signInBtn.setSelected(true);
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = nomeUtenteTF.getText();
        String password = passwordTF.getText();

        if (signInBtn.isSelected()) {
            //verificare credenziali nel db
        } else if (signUpBtn.isSelected()) {
            //verificare credenziali nel db e se non esistono aggiungere
        }
    }

}