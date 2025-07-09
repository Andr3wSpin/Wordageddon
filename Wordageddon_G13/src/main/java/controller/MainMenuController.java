package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import model.Game;
import model.enums.Difficulty;
import model.enums.QuestionType;
import model.files_management.FileAnalysis;
import model.files_management.FileManager;
import model.questions_management.CreateQuestions;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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

    private ToggleGroup toggleGroupDifficulty;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

         toggleGroupDifficulty = new ToggleGroup();
        RadioButton_Easy.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Medium.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Hard.setToggleGroup(toggleGroupDifficulty);




    }








    /**
     * @brief al click questo metodo cambia pagina e ti porta alla pagina per inizare a giocare
     * @param event
     */

    @FXML
    void Button_SetGamePage(ActionEvent event) {

        Difficulty difficulty=takeDifficulty();

        List<File> listFile = FileManager.getFiles();
        List<String> fileName = listFile.stream().map(file -> file.getName()).collect(Collectors.toList());

        int lenghtList = listFile.size();
        int[] randomNumber = new int[0];
        int max =  lenghtList < difficulty.getMaxTexts() ? lenghtList : difficulty.getMaxTexts();
        //generare n numeri random per scegliere file random dalla lista
        for (int i = 0 ; i < max; i++){
            Random random = new Random();
            randomNumber[i]= random.nextInt(lenghtList+1);
         }
    Set<String> effFile = new HashSet<>();  //prendo i file che mi servono max dalla lista di tutti i file
         for (int i = 0 ; i < max ;i++){
          effFile.add(fileName.get(randomNumber[i]));
          }
        Set<QuestionType> questionTypeSet = new HashSet<>();
         //creo l oggetto createQuestion
        if (difficulty.compareTo(Difficulty.EASY) == 0 || max==1) {

            questionTypeSet.add(QuestionType.TYPE1);
            questionTypeSet.add(QuestionType.TYPE4);
        }else {
            questionTypeSet.add(QuestionType.TYPE1);
            questionTypeSet.add(QuestionType.TYPE2);
            questionTypeSet.add(QuestionType.TYPE3);
            questionTypeSet.add(QuestionType.TYPE4);
        }

        CreateQuestions cq = new CreateQuestions(questionTypeSet,effFile, FileAnalysis.readAnalysis(),difficulty.getMaxQuestions());
                                     //TODO
        Game g=  new Game(difficulty,1,cq.createQuestions(),effFile);
        ChangeScene(g);

    }

    private void  ChangeScene(Game g){
        // change scene
    }
    private Difficulty takeDifficulty(){
        String select=null;
        Difficulty difficulty;
        RadioButton selectedRadio=(RadioButton) toggleGroupDifficulty.getSelectedToggle();
        if(selectedRadio==null){
            //gestire erroe nell ui
        }else {
            select =  selectedRadio.getText();
        }
        switch (select){
            case "EASY" : difficulty=Difficulty.EASY;
            break;
            case "MEDIUM" : difficulty=Difficulty.MEDIUM;
                break;
            case "HARD" : difficulty=Difficulty.HARD;
                break;
            default: difficulty=Difficulty.EASY;  //da rivedere
        }
        return difficulty;
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
