package model;

import model.enums.Difficulty;
import model.questions_management.Question;

import java.util.Set;

public class Game {
    /**
     * @brief punteggio risposta corretta
     */
    private static final int CORRECT_ANSWER = 3;
    /**
     * @brief punteggio risposta sbagliata
     */
    private static final int WRONG_ANSWER = 3;
    /**
     * @brief punteggio risposta non data
     */
    private static final int NO_ANSWER = 3;

    private final Difficulty difficulty;
    private final int PLAYER_ID;
    private Set<Question> questions;
    private int score;

    public Game(Difficulty difficulty, int PLAYER_ID, Set<Question> questions) {
        this.difficulty = difficulty;
        this.PLAYER_ID = PLAYER_ID;
        this.questions = questions;
    }

    public Difficulty getDifficuty() { return difficulty; }

    public int getPLAYER_ID() { return PLAYER_ID; }

    public Set<Question> getQuestions() { return questions; }

    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    public void setScore(int score) { this.score = score; }

    /**
     * @brief calcola il punteggio in base alle risposte date
     * @return null
     */

    public void calculateScore(){}

    public int getScore() { return score; }
}
