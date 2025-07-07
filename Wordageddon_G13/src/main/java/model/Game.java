package model;

import model.enums.Difficulty;
import model.questions_management.Question;

import java.util.Set;

public class Game {

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
     * @return punteggio complessivo della partita
     */
    public int getScore() { return score; }
}
