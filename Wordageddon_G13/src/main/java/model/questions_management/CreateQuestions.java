package model.questions_management;

import model.enums.QuestionType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateQuestions {

    private Set<QuestionType> questionTypes;
    private List<String> choosenFiles;
    private Map<String, Map<String, Integer>> fileAnalysis;
    private int questionsNumber;

    public CreateQuestions(Set<QuestionType> questionTypes, List<String> choosenFiles,
                           Map<String, Map<String, Integer>> fileAnalysis, int questionsNumber) {

        this.questionTypes = questionTypes;
        this.choosenFiles = choosenFiles;
        this.fileAnalysis = fileAnalysis;
        this.questionsNumber = questionsNumber;
    }

    /**
     * Crea un set di domande chiamando il metodo relativo al tipo specifico
     * @return set di domande da mostrare
     */
    public Set<Question> createQuestions() { return null; }

    /**
     *
     * Ottiene una parola casuale dal testo scelto, ottiene la risposta corretta e
     * genera 3 numeri random per le risposte sbagliate
     * @return la domanda completa di risposta esatta, testo domanda e risposte sbagliate
     */
    private Question createQuestionType1() { return null; }

    /**
     *
     * Prende la parola corretta chiamndo correctAnswerType_1_2_3() formula la domanda poi chiama getRandomWord per crerare anche le risposte sbagliate e creare la question
     * @return la domanda completa di  risposta esatta testo domanda e risposte sbagliata
     */
    private Question createQuestionType2() { return null; }

    private Question createQuestionType3() { return null; }

    private Question createQuestionType4() { return null; }

    private String correctAnswerType_1_2_3() { return null;}


    /**
     * Prende parole a caso dalla mappa per mostrare le risposte sbagliate
     * @param correctWord parola corretta da aggiungere come prima al set in modo da escluderla dalle risposte sbagliate
     * @return un set di 3 stringhe
     */
    private Set<String> getRandomWord(String correctWord) { return null; }

    /**
     *
     * Ottiene dalla mappa il numero di occorrenze della parola nel file
     * @param word parola scelta casualmente dal sistema
     * @param file file scelto casualmente dal sistema
     * @return risposta corretta di una domanda di Type1
     */
    private int getCorrectAnswer(String word, String file) { return 0; }
}
