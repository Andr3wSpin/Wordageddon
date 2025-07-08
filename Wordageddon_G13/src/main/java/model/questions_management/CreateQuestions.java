package model.questions_management;

import model.enums.QuestionType;

import java.util.Map;
import java.util.Set;

public class CreateQuestions {

    private Set<QuestionType> questionTypes;
    private Set<String> choosenFiles;
    private Map<String, Map<String, Integer>> fileAnalysis;
    private int questionNumber;

    public CreateQuestions(Set<QuestionType> questionTypes, Set<String> choosenFiles,
                           Map<String, Map<String, Integer>> fileAnalysis, int questionNumber) {

        this.questionTypes = questionTypes;
        this.choosenFiles = choosenFiles;
        this.fileAnalysis = fileAnalysis;
        this.questionNumber = questionNumber;
    }

    /**
     * @brief crea un set di domande chiamando i metodi
     * @return set di domande da mostrare
     */
    public Set<Question> createQuestions() { return null; }


    /**
     *
     * @brief si calcola la parola nel testo scelto chiama getcorrectAnswer per calcolare la risposta corretta e si calcola 3 numeri random per le risposte sbagliate
     * @return la domanda completa di risposta esatta testo domanda e risposta sbagliata
     */
    private Question createQuestionType1() { return null; }

    /**
     *
     * @brief prende la parola corretta chiamndo correctAnswerType_1_2_3() formula la domanda poi chiama getRandomWord per crerare anche le risposte sbagliate e creare la question
     * @return la domanda completa di  risposta esatta testo domanda e risposte sbagliata
     */
    private Question createQuestionType2() { return null; }

    private Question createQuestionType3() { return null; }

    private Question createQuestionType4() { return null; }

    private String correctAnswerType_1_2_3() { return null;}


    /**
     * @param correctWord parola corretta da aggiungere come prima al set in modo da escluderla dalle risposte sbagliate
     * @brief prende parole a caso dalla mappa per mostrare le risposte sbagliate
     * @return un set di 3 stringhe
     */
    private Set<String> getRandomWord(String correctWord) { return null; }

    /**
     *
     * @brief ottiene dalla mappa il numero di occorrenze della parola nel file
     * @param word parola scelta casualmente dal sistema
     * @param file file scelto casualmente dal sistema
     * @return risposta corretta di una domanda di Type1
     */
    private int getCorrectAnswer(String word, String file) { return 0; }
}
