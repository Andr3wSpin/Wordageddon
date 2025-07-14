package controller.game_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Game;
import model.questions_management.Question;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class ResultMenuController implements Initializable {

    // --- Elementi UI iniettati dal FXML ---
    @FXML
    private Label correctAnswersLabel;

    @FXML
    private Label wrongAnswersLabel;

    @FXML
    private Label noAnswersLabel;

    @FXML
    private Label pointsLabel;

    @FXML
    private TableView<Question> resultsTableView;

    @FXML
    private TableColumn<Question, String> questionColumn;

    @FXML
    private TableColumn<Question, String> resultColumn;

    @FXML
    private TableColumn<Question, String> correctAnswer;

    @FXML
    private Button mainMenuButton;


    private int correctAnswersCount;
    private int wrongAnswersCount;
    private int noAnswersCount;
    private int totalPoints;
    private Set<Question> questions;
    private Game game;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("displayResult"));
        correctAnswer.setCellValueFactory(new PropertyValueFactory<>("displayCorrectAnswerForTable"));
        mainMenuButton.setOnAction(event -> handleMainMenuButton());

    }



    public void setGame(Game game){
        this.game = game;
    }

    /**
     * @brief scorre la lista di Question ricevuta e conta il numero di risposte giuste, sbagliate o non date
     * poi chiama showResults() per mostrare a video il risultato
     */
    public void setVariables(){
        questions = game.getQuestions();
        game.calculateScore();
        this.totalPoints = game.getScore();
        this.correctAnswersCount = 0;
        this.wrongAnswersCount = 0;
        this.noAnswersCount = 0;

        for (Question currentQuestion : questions) {
            if (!currentQuestion.isGiven()) {
                this.noAnswersCount += 1;
            } else {

                if (currentQuestion.isCorrect()) {
                    this.correctAnswersCount += 1;
                } else {
                    this.wrongAnswersCount += 1;
                }
            }
        }
        showResults();
    }
    private void showResults() {
        correctAnswersLabel.setText(String.valueOf(correctAnswersCount));
        wrongAnswersLabel.setText(String.valueOf(wrongAnswersCount));
        noAnswersLabel.setText(String.valueOf(noAnswersCount));
        pointsLabel.setText(String.valueOf(totalPoints));

        if (questions != null) {
            resultsTableView.getItems().setAll(new ArrayList<>(questions)); //mette tutte le domande del set nella lista
        }
    }

    private void handleMainMenuButton () {

    }


}
