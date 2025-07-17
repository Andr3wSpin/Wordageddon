package model.questions_management;

import java.util.Set;

/**
 * Rappresenta una domanda con il suo testo, la risposta corretta,
 * le possibili risposte e la risposta data dall'utente.
 */
public class Question {

    /**
     * Testo della domanda da porre all'utente.
     */
    private String question;

    /**
     * Risposta corretta associata alla domanda.
     */
    private String correctAnswer;

    /**
     * Risposta fornita dall'utente.
     */
    private String givenAnswer;

    /**
     * Insieme delle possibili risposte, incluse quella corretta e quelle errate.
     */
    private Set<String> answers;

    /**
     * Costruisce una nuova domanda con testo, risposta corretta e risposte possibili.
     *
     * @param question testo della domanda
     * @param correctAnswer risposta corretta
     * @param answers insieme delle risposte possibili (incluse quelle errate)
     */
    public Question(String question, String correctAnswer, Set<String> answers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    /**
     * Verifica se due oggetti {@code Question} sono uguali confrontando
     * il testo della domanda.
     *
     * @param obj oggetto da confrontare
     * @return true se i testi delle domande sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(!(obj instanceof Question)) return false;
        Question q = (Question) obj;
        return this.question.equals(q.question);
    }

    /**
     * Calcola l'hashcode basandosi sul testo della domanda.
     *
     * @return hashcode della domanda
     */
    @Override
    public int hashCode() {
        return this.question.hashCode();
    }

    /**
     * Controlla se la risposta data è corretta.
     *
     * @return true se la risposta data coincide con quella corretta
     */
    public boolean isCorrect() {
        return correctAnswer.equals(givenAnswer);
    }

    /**
     * Controlla se è stata data una risposta.
     *
     * @return true se la risposta data non è vuota
     */
    public boolean isGiven() {
        return givenAnswer != null && !givenAnswer.isEmpty();
    }

    /**
     * Restituisce il testo della domanda.
     *
     * @return testo della domanda
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Restituisce la risposta corretta.
     *
     * @return risposta corretta
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Restituisce l'insieme delle possibili risposte.
     *
     * @return set di risposte possibili
     */
    public Set<String> getAnswers() {
        return answers;
    }

    /**
     * Restituisce la risposta data dall'utente.
     *
     * @return risposta data dall'utente
     */
    public String getGivenAnswer() {
        return givenAnswer;
    }

    /**
     * Imposta la risposta data dall'utente.
     *
     * @param givenAnswer risposta fornita dall'utente
     */
    public void setGivenAnswer(String givenAnswer) {
        this.givenAnswer = givenAnswer;
    }

    /**
     * Restituisce l'esito della domanda da mostrare nella colonna "Results" di una tabella.
     *
     * @return "Non data" se non è stata fornita risposta,
     *         "Corretta" se la risposta è corretta,
     *         "Errata" se la risposta è sbagliata
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
     * Restituisce la risposta corretta da mostrare nella colonna "Risposta corretta" di una tabella.
     * Se la risposta data è corretta, restituisce una stringa vuota.
     *
     * @return risposta corretta oppure stringa vuota se la risposta data è corretta
     */
    public String getDisplayCorrectAnswerForTable() {
        if (isGiven() && isCorrect()) {
            return "";
        } else {
            return correctAnswer;
        }
    }
}
