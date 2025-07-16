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
 * Controller per la schermata dei risultati al termine di una partita.
 * Mostra le statistiche delle risposte dell'utente e una tabella riepilogativa
 * con le domande, l'esito della risposta e, in caso di errore, la risposta corretta.
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

    /** Oggetto Game relativo alla partita appena conclusa. */
    private Game game;

    /** Oggetto per accedere al database locale SQLite. */
    private WordageddonDAOSQLite accesDb;

    /**
     * Inizializza il controller e configura le colonne della tabella
     * in cui verranno mostrati i risultati del quiz.
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
     * Calcola e mostra le statistiche di fine partita.
     *
     * @param game la partita per cui mostrare i risultati
     */
    public void start(Game game) {
        this.game = game;
        setVariables();
    }

    /**
     * Calcola le statistiche relative alle risposte dell'utente:
     * numero di risposte corrette, sbagliate e non date.
     * Richiama {@link #showResults()} per aggiornare l'interfaccia.
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
                this.noAnswersCount++;
            } else {
                if (currentQuestion.isCorrect()) {
                    this.correctAnswersCount++;
                } else {
                    this.wrongAnswersCount++;
                }
            }
        }
        showResults();
    }

    /**
     * Aggiorna l’interfaccia utente con i risultati calcolati
     * e popola la tabella con tutte le domande e relativi esiti.
     */
    private void showResults() {
        correctAnswersLabel.setText(String.valueOf(correctAnswersCount));
        wrongAnswersLabel.setText(String.valueOf(wrongAnswersCount));
        noAnswersLabel.setText(String.valueOf(noAnswersCount));
        pointsLabel.setText(String.valueOf(totalPoints));

        if (questions != null) {
            resultsTableView.getItems().setAll(new ArrayList<>(questions));
        }
    }

    /**
     * Gestisce il click sul pulsante "Main Menu", salvando il risultato
     * nel database e caricando il menu principale.
     *
     * @param event l'evento generato dal click
     */
    @FXML
    public void goToMenu(ActionEvent event) {
        accesDb.insertScore(
                game.getPLAYER_ID(),
                game.getDate().toString(),
                game.getScore(),
                game.getDifficuty().toString()
        );

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
