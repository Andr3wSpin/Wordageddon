package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.files_management.FileAnalysis;
import model.files_management.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AdminController implements Initializable {

    @FXML
    private FlowPane fileContainer;

    private List<File> fileCaricati;
    private final List<File> selectedFiles = new ArrayList<>();







    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            fileCaricati = FileManager.getFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (fileCaricati != null) {
            for (File file : fileCaricati) {
                VBox fileBox = new VBox(10);
                fileBox.setAlignment(Pos.CENTER);

                Label nome = new Label(file.getName());
                nome.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");

                Image image = new Image(
                        getClass().getResource("/assets/fileImage.png").toExternalForm(),
                        150, 280, true, true
                );
                ImageView imageView = new ImageView(image);

                fileBox.getChildren().addAll(imageView, nome);

                fileBox.setOnMousePressed(event -> {
                    boolean selected = fileBox.getStyle().contains("#d3d3d3");

                    Optional<File> fileOptional = fileCaricati.stream()
                            .filter(f -> f.getName().equals(nome.getText()))
                            .findFirst();

                    if (!selected) {
                        fileBox.setStyle("-fx-background-color: #d3d3d3; -fx-background-radius: 8px;");
                        fileOptional.ifPresent(selectedFiles::add);
                    } else {
                        fileBox.setStyle("");
                        fileOptional.ifPresent(selectedFiles::remove);
                    }
                });

                fileContainer.getChildren().add(fileBox);
            }
        }
    }

    public void RemoveSelectedFile() {
        if (selectedFiles.isEmpty()) return;

        try {
            FileManager.deleteFiles(selectedFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        selectedFiles.clear();
    }
}
