package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.files_management.FileManager;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class AdminController implements Initializable {


    @FXML
    private FlowPane fileContainer;


    private List<File> fileCaricati;

    private List<File> selectedFiles = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        try {
            fileCaricati = FileManager.getFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

   if(fileCaricati!= null) {
       for (File file : fileCaricati) {

           VBox fileBox = new VBox(10);
           fileBox.setAlignment(Pos.CENTER);
           javafx.scene.control.Label nome = new javafx.scene.control.Label(file.getName());
           nome.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");
           ImageView imageView = new ImageView(new Image(getClass().getResource("/assets/fileImage.png").toExternalForm(), 150, 280, true, true));

           fileBox.getChildren().addAll(imageView, nome);
           fileBox.setOnMousePressed(event -> {
               boolean selected =  fileBox.getStyle().contains("#d3d3d3");

               if (!selected) {
                   fileBox.setStyle("-fx-background-color: #d3d3d3; -fx-background-radius: 8px;");

                   Optional<File> fileOptional = fileCaricati.stream().
                           filter(
                                   f -> f.getName().equals(nome.getText())
                           ).findFirst();
                   fileOptional.ifPresent(file1 -> {
                       selectedFiles.add(file1);
                   });
               }else {


                       fileBox.setStyle("");

                       Optional<File> fileOptional = fileCaricati.stream().
                               filter(
                                       f -> f.getName().equals(nome.getText())
                               ).findFirst();
                       fileOptional.ifPresent(file1 -> {
                           selectedFiles.remove(file1);
                       });

                   }
           });
           fileContainer.getChildren().add(fileBox);
       }
   }

    }




    private void RemoveSelectedFile (){
      if (selectedFiles.isEmpty()) return;
       fileCaricati.removeAll(selectedFiles);
        try {
            Parent pageAdmin = FXMLLoader.load(getClass().getResource("/view/AdminView.fxml"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}