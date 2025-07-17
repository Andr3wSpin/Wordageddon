package controller.game_controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.questions_management.Question;

import java.io.IOException;
import java.util.*;

/**
 * Controller per la gestione della schermata delle domande del quiz.
 * Mostra le domande, gestisce la selezione delle risposte e la transizione alla schermata dei risultati.
 */
public class QuestionMenuController {

    @FXML
    private Label questionLabel;

    @FXML
    private Label numberQuestionLabel;

    @FXML
    private RadioButton option1Btn;

    @FXML
    private RadioButton option2Btn;

    @FXML
    private RadioButton option3Btn;

    @FXML
    private RadioButton option4Btn;

    @FXML
    private Button nextQuestionBtn;

    @FXML
    private Rectangle rectangleForAnimation;

    private ToggleGroup optionsGroup;
    private RadioButton[] optionButtons;

    private final int MAX_ANSWERS = 4;
    private int index;
    private Game game;
    private List<Question> questions;

    /**
     * Inizializza i componenti dell'interfaccia.
     * Configura il gruppo di toggle per le risposte e imposta i testi iniziali.
     * Viene automaticamente chiamato da JavaFX dopo il caricamento dell'FXML.
     */
    @FXML
    private void initialize() {
        optionButtons = new RadioButton[MAX_ANSWERS];
        index = 0;

        nextQuestionBtn.setText("Next question");

        optionsGroup = new ToggleGroup();
        option1Btn.setToggleGroup(optionsGroup);
        option2Btn.setToggleGroup(optionsGroup);
        option3Btn.setToggleGroup(optionsGroup);
        option4Btn.setToggleGroup(optionsGroup);

        int i = 0;

        optionButtons[i++] = option1Btn;
        optionButtons[i++] = option2Btn;
        optionButtons[i++] = option3Btn;
        optionButtons[i++] = option4Btn;

        option1Btn.setText("Option 1");
        option2Btn.setText("Option 2");
        option3Btn.setText("Option 3");
        option4Btn.setText("Option 4");
    }

    /**
     * Avvia la visualizzazione del quiz con le domande fornite dal gioco.
     * Inizializza la lista delle domande e mostra la prima.
     *
     * @param game l'oggetto Game contenente le domande
     */
    public void start(Game game) {
        this.questions = new ArrayList<>(game.getQuestions());
        this.game = game;
        showQuestion();
    }

    /**
     * Mostra la domanda corrente e le possibili risposte selezionabili dall'utente.
     * Aggiorna il testo della domanda e i testi dei pulsanti di risposta.
     */
    private void showQuestion() {
        Set<String> answers = questions.get(index).getAnswers();
        numberQuestionLabel.setText("Question N°" + (index + 1));
        questionLabel.setText(questions.get(index).getQuestion());

        int answIndex = 0;
        for (String answer : answers) {
            optionButtons[answIndex++].setText(answer);
        }
    }

    /**
     * Applica un'animazione visiva all'interazione con il rettangolo.
     * Animazione usata per effetti di transizione tra le domande.
     */
    private void AnimationWave() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), rectangleForAnimation);
        scaleTransition.setToX(2.5);
        scaleTransition.setToY(2.5);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.3), rectangleForAnimation);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(0.0);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setOnFinished(e -> {
            rectangleForAnimation.setScaleX(0.0);
            rectangleForAnimation.setScaleY(0.0);
            rectangleForAnimation.setOpacity(0.0);
        });

        parallelTransition.play();
    }

    /**
     * Gestisce il click sul bottone "Next question" o "Go to summary".
     * Salva la risposta selezionata, passa alla prossima domanda oppure carica
     * la schermata dei risultati se il quiz è terminato.
     *
     * @param event evento generato dal click sul pulsante next Question
     */
    @FXML
    void nextQuestion(ActionEvent event) {
        AnimationWave();
        RadioButton selectedRadioButton = (RadioButton) optionsGroup.getSelectedToggle();
        optionsGroup.selectToggle(null);

        if (selectedRadioButton == null) {
            questions.get(index).setGivenAnswer("");
        } else {
            questions.get(index).setGivenAnswer(selectedRadioButton.getText());
        }

        if (index < questions.size() - 1) {
            index++;
            if (index == questions.size() - 1) {
                nextQuestionBtn.setText("Go to summary");
            }
            showQuestion();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ResultMenuView.fxml"));
                Parent root = loader.load();

                ResultMenuController controller = loader.getController();
                controller.start(game);

                Stage stage = (Stage) nextQuestionBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
