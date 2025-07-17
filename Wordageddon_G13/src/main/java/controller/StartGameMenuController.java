package controller;

import controller.game_controllers.TextMenuController;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.User;
import model.enums.Difficulty;
import model.enums.QuestionType;
import model.files_management.FileManager;
import model.questions_management.CreateQuestions;
import model.questions_management.Question;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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

    @FXML
    private ProgressBar loadingBar;
    @FXML
    private ImageView imgButtonStart;

    private User user;
    private Map<String, Map<String, Integer>> fileAnalysis;
    private Task<Game> currentGameTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBackground();
        initRadioButtons();
        loadingBar.setVisible(false);
    }

    @FXML
    private void startGame(ActionEvent event) {

        if(currentGameTask != null && currentGameTask.isRunning()) {
            showMessage("La partita è già in fase di creazione, attendere...", Alert.AlertType.INFORMATION);
            event.consume();
            return;
        }

        Difficulty difficulty = takeDifficulty();

        if (difficulty == null) {
            showMessage("Selezionare una difficoltà.", Alert.AlertType.INFORMATION);
            event.consume();
            return;
        }

        currentGameTask = new Task<Game>() {
            @Override
            protected Game call() throws Exception {
                List<File> filesList = FileManager.getFiles();

                if (filesList.isEmpty())
                    throw new IOException("Nessun file disponibile per creare domande.");

                Collections.shuffle(filesList);
                int maxTextsToChoose = Math.min(filesList.size(), difficulty.getMaxTexts());
                List<File> chosenFiles = filesList.subList(0, maxTextsToChoose);

                Set<QuestionType> questionTypeSet = new HashSet<>();
                if (difficulty == Difficulty.EASY || maxTextsToChoose == 1) {
                    questionTypeSet.add(QuestionType.TYPE1);
                    questionTypeSet.add(QuestionType.TYPE2);
                } else {
                    questionTypeSet.add(QuestionType.TYPE1);
                    questionTypeSet.add(QuestionType.TYPE2);
                    questionTypeSet.add(QuestionType.TYPE3);
                    questionTypeSet.add(QuestionType.TYPE4);
                }

                List<String> fileNames = chosenFiles.stream()
                        .map(File::getName)
                        .collect(Collectors.toList());

                CreateQuestions cq = new CreateQuestions(questionTypeSet, fileNames, fileAnalysis, difficulty.getMaxQuestions());
                Set<Question> questions = cq.createQuestions(progress -> {
                    updateProgress(progress, difficulty.getMaxQuestions());
                });

                return new Game(difficulty, user.getID(), questions, chosenFiles);
            }
        };

        loadingBar.visibleProperty().bind(currentGameTask.runningProperty());
        loadingBar.progressProperty().bind(currentGameTask.progressProperty());

        currentGameTask.setOnSucceeded(e -> {
            showMessage("La partita è stata creata con successo.", Alert.AlertType.INFORMATION);
            Game g = currentGameTask.getValue();
            loadingBar.visibleProperty().unbind();
            loadingBar.setVisible(false);
            changeScene(g);
        });

        currentGameTask.setOnFailed(e -> {
            loadingBar.visibleProperty().unbind();
            loadingBar.setVisible(false);
            Throwable ex = currentGameTask.getException();
            String msg = "Errore durante la creazione della partita:\n" + ex.getMessage();
            showMessage(msg, Alert.AlertType.ERROR);
        });

        new Thread(currentGameTask).start();
    }

    private void changeScene(Game g){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TextMenuView.fxml"));
            Scene textPage = new Scene(loader.load());
            Stage stage = (Stage) Button_StartGame.getScene().getWindow();
            stage.setScene(textPage);
            TextMenuController tmController = loader.getController();
            tmController.start(g);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare la vista del menu di testo.", e);
        }
    }

    private Difficulty takeDifficulty(){
        Difficulty difficulty = null;
        RadioButton selectedRadio = (RadioButton) toggleGroupDifficulty.getSelectedToggle();
        if(selectedRadio == null) return null;
        switch(selectedRadio.getText()) {
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

    private void initBackground() {
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        MediaViewWallPaper.setPreserveRatio(false);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    private void initRadioButtons() {
        toggleGroupDifficulty = new ToggleGroup();
        RadioButton_Easy.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Medium.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Hard.setToggleGroup(toggleGroupDifficulty);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFileAnalysis(Map<String, Map<String, Integer>> fileAnalysis) {
        this.fileAnalysis = fileAnalysis;
    }

    private void showMessage(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    void btnStartHoverEntred(MouseEvent event) {
        TranslateTransition textSlide = new TranslateTransition(Duration.millis(500),LableButton_StartGame);
        textSlide.setFromX(0);
        textSlide.setToX(100);
        textSlide.play();

        FadeTransition textOpacity = new FadeTransition(Duration.millis(500),LableButton_StartGame);
        textOpacity.setFromValue(1.0);
        textOpacity.setToValue(0.0);
        textOpacity.play();

        TranslateTransition imageSlide = new TranslateTransition(Duration.millis(300),imgButtonStart);
        imageSlide.setFromX(0);
        imageSlide.setToX(35);
        imageSlide.play();
    }

    @FXML
    void btnStartHoverExit(MouseEvent event) {
        TranslateTransition textSlide = new TranslateTransition(Duration.millis(500),LableButton_StartGame);
        textSlide.setFromX(100);
        textSlide.setToX(0);
        textSlide.play();

        TranslateTransition imageSlide = new TranslateTransition(Duration.millis(500),imgButtonStart);
        imageSlide.setFromX(40);
        imageSlide.setToX(0);
        imageSlide.play();

        FadeTransition textOpacity = new FadeTransition(Duration.millis(500),LableButton_StartGame);
        textOpacity.setFromValue(0.0);
        textOpacity.setToValue(1.0);
        textOpacity.play();
    }
}
