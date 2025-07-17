package model;

import model.enums.Difficulty;
import model.questions_management.Question;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Rappresenta una singola partita giocata da un utente.
 * Contiene le informazioni sulle domande, i file usati, il punteggio e la difficoltà.
 */
public class Game {

    /**
     * Punteggio assegnato per ogni {@link Question} con risposta corretta.
     */
    private static final int CORRECT_ANSWER = 3;

    /**
     * Punteggio assegnato per ogni {@link Question} con risposta errata.
     */
    private static final int WRONG_ANSWER = -1;

    /**
     * Punteggio assegnato per ogni {@link Question} non risposta.
     */
    private static final int NO_ANSWER = 0;

    private final Difficulty difficulty;

    private final int PLAYER_ID;

    /**
     * Insieme delle domande della partita.
     */
    private Set<Question> questions;

    private int score;

    /**
     * Lista dei file scelti per la generazione delle domande.
     */
    private List<File> choosenFiles;

    private final LocalDateTime date;

    /**
     * Costruttore per creare una nuova partita con le informazioni specificate.
     * @param difficulty difficoltà della partita
     * @param PLAYER_ID identificatore del giocatore
     * @param questions insieme delle domande per la partita
     * @param choosenFiles lista dei file utilizzati nella partita
     */
    public Game(Difficulty difficulty, int PLAYER_ID, Set<Question> questions, List<File> choosenFiles) {
        this.difficulty = difficulty;
        this.PLAYER_ID = PLAYER_ID;
        this.questions = questions;
        this.choosenFiles = choosenFiles;
        this.date = LocalDateTime.now();
    }

    /**
     * Calcola il punteggio totale della partita sommando i punteggi delle singole domande.
     * Le risposte corrette, errate e non date hanno punteggi differenti.
     */
    public void calculateScore(){
        score = 0;
        for(Question question : questions) {
            if(!question.isGiven()) {
                score += NO_ANSWER;
                continue;
            }
            score += question.isCorrect() ? CORRECT_ANSWER : WRONG_ANSWER;
        }
    }

    /**
     * Restituisce la difficoltà della partita.
     * @return difficoltà della partita
     */
    public Difficulty getDifficuty() { return difficulty; }

    /**
     * Restituisce l'identificatore del giocatore.
     * @return ID del giocatore
     */
    public int getPLAYER_ID() { return PLAYER_ID; }

    /**
     * Restituisce la data e ora in cui è stata giocata la partita.
     * @return data e ora della partita
     */
    public LocalDateTime getDate(){return date;}

    /**
     * Restituisce l'insieme delle domande della partita.
     * @return insieme delle domande {@link Question}
     */
    public Set<Question> getQuestions() { return questions; }

    /**
     * Imposta l'insieme delle domande della partita.
     * @param questions insieme di domande da impostare
     */
    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    /**
     * Imposta il punteggio totale della partita.
     * @param score punteggio da impostare
     */
    public void setScore(int score) { this.score = score; }

    /**
     * Restituisce il punteggio totale della partita.
     * @return punteggio totale
     */
    public int getScore() { return score; }

    /**
     * Restituisce la lista dei file utilizzati per la generazione delle domande.
     * @return lista di file {@link File}
     */
    public List<File> getChoosenFiles() { return choosenFiles; }
}
