package model;

import model.enums.Difficulty;
import model.questions_management.Question;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Racchiude le informazioni della singola partita
 */
public class Game {
    /**
     * Punteggio risposta corretta
     */
    private static final int CORRECT_ANSWER = 3;
    /**
     * Punteggio risposta sbagliata
     */
    private static final int WRONG_ANSWER = -1;
    /**
     * Punteggio risposta non data
     */
    private static final int NO_ANSWER = 0;

    private final Difficulty difficulty;
    private final int PLAYER_ID;
    private Set<Question> questions;
    private int score;
    private List<File> choosenFiles;
    private final LocalDateTime date;

    public Game(Difficulty difficulty, int PLAYER_ID, Set<Question> questions, List<File> choosenFiles) {

        this.difficulty = difficulty;
        this.PLAYER_ID = PLAYER_ID;
        this.questions = questions;
        this.choosenFiles = choosenFiles;
        this.date = LocalDateTime.now();

    }

    /**
     * Calcola il punteggio in base alle risposte del giocatore
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

    public Difficulty getDifficuty() { return difficulty; }

    public int getPLAYER_ID() { return PLAYER_ID; }

    public LocalDateTime getDate(){return date;}

    public Set<Question> getQuestions() { return questions; }

    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

    public List<File> getChoosenFiles() { return choosenFiles; }
}
