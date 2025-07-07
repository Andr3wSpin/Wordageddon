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
     * @brief crea un set di domande
     * @return set di domande da mostrare
     */
    public Set<Question> createQuestions() { return null; }

    private Question createQuestionType1() { return null; }

    private Question createQuestionType2() { return null; }

    private Question createQuestionType3() { return null; }

    private Question createQuestionType4() { return null; }

    /**
     *
     * @return 
     */
    private String getRandomWord() { return null; }

    /**
     *
     * @brief ottiene dalla mappa il numero di occorrenze della parola nel file
     * @param word parola scelta casualmente dal sistema
     * @param file file scelto casualmente dal sistema
     * @return risposta corretta di una domanda di Type1
     */
    private int getCorrectAnswer(String word, String file) { return 0; }
}
