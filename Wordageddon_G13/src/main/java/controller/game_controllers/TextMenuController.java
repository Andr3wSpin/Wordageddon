package controller.game_controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Game;

import java.awt.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TextMenuController {

    @FXML
    private TextArea textArea;

    @FXML
    private Label numberOfFileLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Button nextButton;

    private Game game;
    private List<File> files;
    private int index;

    @FXML
    private void initialize() {
        index = 0;
        nextButton.setText("Show next text");
        textArea.setEditable(false);
        nextButton.setDisable(true);  // Disabilito fino a caricamento
    }

    public void start(Game game){
        numberOfFileLabel.setText("Loading...");
        this.game = game;
        files = game.getChoosenFiles();

        if (files == null || files.isEmpty()) {
            textArea.setText("Nessun file disponibile.");
            numberOfFileLabel.setText("Text No. -");
            nextButton.setDisable(true);
        } else {
            nextButton.setDisable(false);
            index = 0;
            showText();
        }
    }

    public void showText() {
        numberOfFileLabel.setText("Text N°" + (index + 1));
        if (files != null && !files.isEmpty() && index < files.size()) {
            File currentFile = files.get(index);
            try {
                String text = new String(Files.readAllBytes(currentFile.toPath()), StandardCharsets.UTF_8);
                textArea.setText(text);
            } catch (IOException e) {
                textArea.setText("Errore nella lettura del file: " + e.getMessage());
            }
        } else {
            textArea.setText("Nessun file disponibile.");
        }
    }

    @FXML
    private void handleNextButton(ActionEvent event) {
        index += 1;
        if (files == null || files.isEmpty()) {
            nextButton.setDisable(true);
            return;
        }
        if (index < files.size()) {
            if (index == files.size() - 1) {
                nextButton.setText("Go to questions");
            }
            showText();
        } else if (index == files.size()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionView.fxml"));
                Parent root = loader.load();


                QuestionMenuController questionController = loader.getController();
                questionController.start(game);


                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                System.err.println(e.getMessage());;
            }
        }
    }
}
