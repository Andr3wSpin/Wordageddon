package controller;

import controller.game_controllers.TextMenuController;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import model.Game;
//import model.User;  questa riga è commentata perché al momento user è null, quindi non si può ottenere l'id
import model.User;
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
import sun.swing.PrintingStatus;

public class StartGameMenuController implements Initializable {

    @FXML
    private Button Button_StartGame;

    @FXML
    private Label LableButton_StartGame;

    @FXML
    private MediaView MediaViewWallPaper;

    @FXML
    private RadioButton RadioButton_Easy;

    @FXML
    private RadioButton RadioButton_Hard;

    @FXML
    private RadioButton RadioButton_Medium;

    private ToggleGroup toggleGroupDifficulty;

    private User user;
    private Map<String, Map<String, Integer>> fileAnalysis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initBackground();
        initRadioButtons();
    }

    /**
     * Avvia una nuova partita
     * @param event click del relativo pulsante
     */
    @FXML
    private void startGame(ActionEvent event) {
        Difficulty difficulty = takeDifficulty();

        if (difficulty == null) {
            showMessage("Selezionare una difficoltà.", Alert.AlertType.INFORMATION);
            event.consume();
            return;
        }

        Task<Game> gameTask = new Task() {
            @Override
            protected Game call() throws Exception {
                List<File> filesList = FileManager.getFiles();

                if (filesList.isEmpty())
                    throw new IOException("Nessun file disponibile.");

                Collections.shuffle(filesList);

                int max = Math.min(filesList.size(), difficulty.getMaxTexts());

                List<File> choosenFiles = filesList.subList(0, max);

                Set<QuestionType> questionTypeSet = new HashSet<>();

                if ((difficulty == Difficulty.EASY) || (max == 1)) {
                    questionTypeSet.add(QuestionType.TYPE1);
                    questionTypeSet.add(QuestionType.TYPE2);
                } else {
                    questionTypeSet.add(QuestionType.TYPE1);
                    questionTypeSet.add(QuestionType.TYPE2);
                    questionTypeSet.add(QuestionType.TYPE3);
                    questionTypeSet.add(QuestionType.TYPE4);
                }

                List<String> fileNames = choosenFiles.stream()
                        .map(File::getName)
                        .collect(Collectors.toList());

                CreateQuestions cq = new CreateQuestions(questionTypeSet, fileNames, fileAnalysis, difficulty.getMaxQuestions());
                return new Game(difficulty, user.getID(), cq.createQuestions(), choosenFiles);
            }
        };

        gameTask.setOnSucceeded(e -> {
            showMessage("La partita è stata creata con successo.", Alert.AlertType.INFORMATION);
            Game g = gameTask.getValue();
            changeScene(g);
        });

        gameTask.setOnFailed(e -> {
            Throwable ex = gameTask.getException();
            String msg = "Errore durante la creazione della partita:\n" + ex.getMessage();
            showMessage(msg, Alert.AlertType.ERROR);
        });

        new Thread(gameTask).start();
    }


    private void  changeScene(Game g){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TextMenuView.fxml"));
            Scene textPage = new Scene(loader.load());

            Stage stage = (Stage) Button_StartGame.getScene().getWindow();
            stage.setScene(textPage);

            TextMenuController tmController = loader.getController();
            tmController.start(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
     * Inizializza lo sfondo della schermata
     */
    private void initBackground() {

        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        MediaViewWallPaper.setPreserveRatio(false);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    /**
     * Inizializza i radio button
     */
    private void initRadioButtons() {

        toggleGroupDifficulty = new ToggleGroup();
        RadioButton_Easy.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Medium.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Hard.setToggleGroup(toggleGroupDifficulty);
    }

    public void setUser(User user) { this.user = user; }

    public void setFileAnalysis(Map<String, Map<String, Integer>> fileAnalysis) {

        this.fileAnalysis = fileAnalysis;
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
}
