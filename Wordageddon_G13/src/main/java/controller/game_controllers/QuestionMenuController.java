package controller.game_controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup; // Importa ToggleGroup
import javafx.stage.Stage;
import model.Game;
import model.questions_management.Question;

import java.io.IOException;
import java.util.*;

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

    private ToggleGroup optionsGroup;
    private List<RadioButton> optionButtons;

    private int index;
    private Game game;
    private List<Question> questions;

    /**
     * Inizializza i componenti dell'interfaccia. Configura il gruppo di bottoni
     * per le risposte e imposta i testi iniziali.
     */
    @FXML
    private void initialize() {
        optionButtons = new ArrayList<>();
        index = 0;

        nextQuestionBtn.setText("Next question");


        optionsGroup = new ToggleGroup();
        option1Btn.setToggleGroup(optionsGroup);
        option2Btn.setToggleGroup(optionsGroup);
        option3Btn.setToggleGroup(optionsGroup);
        option4Btn.setToggleGroup(optionsGroup);

        optionButtons.add(option1Btn);
        optionButtons.add(option2Btn);
        optionButtons.add(option3Btn);
        optionButtons.add(option4Btn);


        option1Btn.setText("Option 1");
        option2Btn.setText("Option 2");
        option3Btn.setText("Option 3");
        option4Btn.setText("Option 4");

    }
    /**
     * Avvia la visualizzazione del quiz con le domande fornite dal gioco.
     *
     * @param game l'oggetto Game contenente le domande
     */
    public void start(Game game){
        this.questions = new ArrayList<>(game.getQuestions());
        this.game = game;
        showQuestion();
    }

    /**
     * Mostra la domanda corrente e le sue possibili risposte.
     */
    private void showQuestion(){
        Set<String> answers = new HashSet<>();
        numberQuestionLabel.setText("Question N°"+(index+1));
        answers = (questions.get(index).getAnswers());
        questionLabel.setText((questions.get(index).getQuestion()));

        int answIndex = 0;

        for (String answer : answers){
            optionButtons.get(answIndex).setText(answer);
            answIndex+=1;
        }

    }

    /**
     * Gestisce il click sul bottone "Next question" o "Go to summary".
     * Salva la risposta selezionata all'interno dell'attributo givenAnswer della risposta corrente,
     * passa alla domanda successiva o mostra la schermata dei risultati se il quiz è finito.
     */
    @FXML
    private void nextQuestion() {

        RadioButton selectedRadioButton = (RadioButton) optionsGroup.getSelectedToggle();
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