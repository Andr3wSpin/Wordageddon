package model.questions_management;

import java.util.Set;

public class Question {

    private String question;
    private String correctAnswer;
    private String givenAnswer;
    private Set<String> answers;

    public Question(String question, String correctAnswer, Set<String> answers) {

        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }


    public boolean isCorrect() { return correctAnswer.equals(givenAnswer); }

    public boolean isGiven() { return !givenAnswer.isEmpty(); }

    public String getQuestion() { return question; }

    public String getCorrectAnswer() { return correctAnswer; }

    public Set<String> getAnswers() { return answers; }

    public String getGivenAnswer() { return givenAnswer; }

    public void setGivenAnswer(String givenAnswer) { this.givenAnswer = givenAnswer; }

    /**
     * Restituisce l'esito della domanda, serve per inserire il risultato nella colonna "Results" della tableView
     */
    public String getDisplayResult() {
        if (!isGiven()) {
            return "Non data";
        } else if (isCorrect()) {
            return "Corretta";
        } else {
            return "Errata";
        }
    }

    /**
     * Restituisce la stringa della risposta corretta per la colonna "Risposta corretta" della TableView.
     * Vuota se la risposta data è corretta, altrimenti la risposta corretta.
     */
    public String getDisplayCorrectAnswerForTable() {
        if (isGiven() && isCorrect()) {
            return "";
        } else {
            return correctAnswer;
        }
    }

}
