package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import model.Game;
//import model.User;  questa riga è commentata perché al momento user è null, quindi non si può ottenere l'id
import model.enums.Difficulty;
import model.enums.QuestionType;
import model.files_management.FileAnalysis;
import model.files_management.FileManager;
import model.questions_management.CreateQuestions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Platform;

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

   // private User user;
    private Map<String, Map<String, Integer>> fileAnalysis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        StringBuilder msg = new StringBuilder("Impossibile aprire il file di analisi dei documenti!");
        try {
            fileAnalysis = FileAnalysis.readAnalysis();
//            NON DECOMMENTATE QUESTO IF ALTRIMENTI L'APP SI CHIUDE!!
//            if(fileAnalysis == null) {
//                showMessage(msg.toString(), Alert.AlertType.ERROR);
//                Platform.exit();
//            }
        } catch (IOException e) {
            showMessage(msg.toString(), Alert.AlertType.ERROR);
            Platform.exit();;
        }

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
     * Avvia una nuova partita
     * @param event click del relativo pulsante
     */
    @FXML
    private void startGame(ActionEvent event) {

        Difficulty difficulty = takeDifficulty();

        if(difficulty == null) {

            showMessage("Selezionare una difficoltà.", Alert.AlertType.INFORMATION);
            event.consume();
            return;
        }

        List<File> filesList = null;
        try {
            filesList = FileManager.getFiles();
        } catch (IOException e) {

            String msg = "Errore nella lettura dei file di gioco!\nImpossibile avviare la partita.";
            showMessage(msg, Alert.AlertType.ERROR);
            event.consume();
            return;
        }

        int listLength = filesList.size();
        Set<Integer> randomFileIndexes = new HashSet<>();
        int max = listLength < difficulty.getMaxTexts() ? listLength : difficulty.getMaxTexts();

        Random random = new Random();

        /*
        genera indici randomici per selezionare i file fino a quando non c'è il numero di indici
        richiesto evitando duplicati
        */
        while(randomFileIndexes.size() < max) randomFileIndexes.add(random.nextInt(listLength));

        List<File> choosenFiles = randomFileIndexes.stream()
                .map(filesList::get)
                .collect(Collectors.toList());

        Set<QuestionType> questionTypeSet = new HashSet<>();
         //creo l oggetto createQuestion
        if ((difficulty == Difficulty.EASY) || (max == 1)) {
            questionTypeSet.add(QuestionType.TYPE1);
            questionTypeSet.add(QuestionType.TYPE4);
        }else {
            questionTypeSet.add(QuestionType.TYPE1);
            questionTypeSet.add(QuestionType.TYPE2);
            questionTypeSet.add(QuestionType.TYPE3);
            questionTypeSet.add(QuestionType.TYPE4);
        }

        List<String> fileNames = choosenFiles.stream().map(File::getName).collect(Collectors.toList());

        CreateQuestions cq = new CreateQuestions(questionTypeSet, fileNames, fileAnalysis,difficulty.getMaxQuestions());
                                     //TODO
        Game g = new Game(difficulty,1,cq.createQuestions(),choosenFiles);
        changeScene(g);
    }

    private void  changeScene(Game g){
        // change scene
    }

    /**
     * Ottiene la difficoltà scelta dall'utente
     * @return Difficoltà scelta
     */
    private Difficulty takeDifficulty(){

        String select;
        Difficulty difficulty = null;
        RadioButton selectedRadio = (RadioButton) toggleGroupDifficulty.getSelectedToggle();

        if(selectedRadio == null) return difficulty;

        select = selectedRadio.getText();

        switch(select) {
            case "EASY":
                difficulty = Difficulty.EASY;
                break;
            case "MEDIUM":
                difficulty = Difficulty.MEDIUM;
                break;
            case "HARD":
                difficulty = Difficulty.HARD;
                break;
        }

        return difficulty;
    }

    /**
     * @brief al click questo metodo cambia pagina e ti porta alla pagina di gestione dell admin
     * @param event
     */
    void Button_SetPageAdmin(ActionEvent event) {

    }
    /**
     * @brief al click questo metodo cambia pagina e ti porta alla pagina per visualizzare gli score
     * @param event
     */
    void Button_SetScoresPage(ActionEvent event) {

    }

    /**
     * Mostra all'utente il messaggio ricevuto
     * @param msg Messaggio da mostrare all'utente
     * @param type Tipo di messaggio
     */
    private void showMessage(String msg, Alert.AlertType type) {

        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Riceve l'oggetto user contenente le informazioni dell'utente corrente
     * @param user L'utente autenticato corrente
     */
    //il metodo è commentato perché non viene invocato ancora da nessuna parte -> user = null
    //public void setUser(User user) { this.user = user; }
















    /**
     * tutti questi metodi servono per gestire le animazioni dei vari pulsanti in hover
     */

    void HoverButtonEntred_AdminPageButton(MouseEvent event) {

    }

    void HoverButtonEntred_GamePageButton(MouseEvent event) {

    }

    void HoverButtonEntred_ScoresPageButton(MouseEvent event) {

    }

    void HoverButtonEntred_startButton(MouseEvent event) {

    }

    void HoverButtonExit_AdminPageButton(MouseEvent event) {

    }

    void HoverButtonExit_GamePageButton(MouseEvent event) {

    }

    void HoverButtonExit_ScoresPageButton(MouseEvent event) {

    }

    void HoverButtonExit_startButton(MouseEvent event) {

    }

}
