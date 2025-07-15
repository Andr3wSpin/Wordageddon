package controller.game_controllers;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Game;
import model.db.WordageddonDAOSQLite;
import model.questions_management.Question;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Controller per la schermata in cui viene mostrato l'esito di una partita.
 * Mostra le statistiche del giocatore (risposte corrette, errate, non date, punteggio)
 * e una tabella riepilogativa con l’esito per ogni domanda con eventuali risposte corrette in caso di errore.
 */
public class ResultMenuController implements Initializable {


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
    private WordageddonDAOSQLite accesDb;

    /**
     * Inizializza il controller e configura le colonne della tabella in cui verranno
     * mostrati i risultati del quiz.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accesDb = new WordageddonDAOSQLite();
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("displayResult"));
        correctAnswer.setCellValueFactory(new PropertyValueFactory<>("displayCorrectAnswerForTable"));
    }


    /**
     * Avvia il controller con l'istanza {@link Game} ricevuta.
     * Fa partire il calcolo dei dati riguardanti la partita (domande corrette, sbagliate, non date e punteggio).
     *
     * @param game la partita per cui mostrare i risultati
     */
    public void start(Game game) {
        this.game = game;
        setVariables();
    }

    /**
     * Calcola le statistiche relative alle risposte dell'utente.
     * Conta il numero di risposte corrette, sbagliate e non date.
     * Chiama {@link #showResults()} per aggiornare l'interfaccia.
     * Scorre la lista di Question ricevuta e conta il numero di risposte giuste, sbagliate o non date
     * poi chiama showResults() per mostrare a video il risultato
     */
    private void setVariables() {
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

    /**
     * Aggiorna l’interfaccia con i risultati calcolati in precedenza.
     * Popola poi la tabella con l'elenco delle domande e relativi esiti.
     */
    private void showResults() {
        correctAnswersLabel.setText(String.valueOf(correctAnswersCount));
        wrongAnswersLabel.setText(String.valueOf(wrongAnswersCount));
        noAnswersLabel.setText(String.valueOf(noAnswersCount));
        pointsLabel.setText(String.valueOf(totalPoints));

        if (questions != null) {
            resultsTableView.getItems().setAll(new ArrayList<>(questions)); //mette tutte le domande del set nella lista
        }
    }

    /**
     * Gestisce il click sul pulsante "Main Menu" rimandando l'utente al Menu Principale
     * Carica il risultato della partita nel dataBase.
     */
    @FXML
    public void goToMenu(ActionEvent event) {

        accesDb.insertScore(game.getPLAYER_ID(), game.getDate().toString(), game.getScore(), game.getDifficuty().toString());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenuView.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setUser(accesDb.getUser(game.getPLAYER_ID()));

            Stage stage = (Stage) pointsLabel.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}



