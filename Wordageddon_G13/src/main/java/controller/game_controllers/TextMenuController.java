package controller.game_controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Game;

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
    private Button nextButton;

    private Game game;
    private List<File> files;
    private int index;

    public void setGame(Game game) {
        this.game = game;
    }

    @FXML
    private void initialize() {
        index = 0;
        textArea.setEditable(false);
    }

    public void showText() {
        files = game.getChoosenFiles();

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
            numberOfFileLabel.setText("Text No. -");
        }
    }

    @FXML
    private void handleNextButton() {
        if (index < files.size()-1) {
            index += 1;
            numberOfFileLabel.setText("Text N°"+(index+1));
            if (index < files.size()-1)
                nextButton.setText("Go to questions");
            showText();
        } else if (index == files.size()-1) {
            //cambio schermata
        }
    }

}
