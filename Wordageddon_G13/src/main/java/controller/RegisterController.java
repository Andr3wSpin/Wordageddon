package controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class RegisterController {

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Botón de registro pulsado");
        alert.showAndWait();
    }

    @FXML
    private void handleBackToLoginClick(MouseEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Volver al login pulsado");
        alert.showAndWait();
    }
}
