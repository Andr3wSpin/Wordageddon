package model.questions_management;

import model.enums.QuestionType;
// import javax.jnlp.IntegrationService;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CreateQuestions {

    private List<String> choosenFiles;
    private Map<String, Map<String, Integer>> fileAnalysis;
    private int questionsNumber;
    private final List<QuestionType> questionTypes;

    public CreateQuestions(Set<QuestionType> questionTypes, List<String> choosenFiles,
                           Map<String, Map<String, Integer>> fileAnalysis, int questionsNumber) {

        this.questionTypes = questionTypes.stream().collect(Collectors.toList());
        this.choosenFiles = choosenFiles;
        this.fileAnalysis = fileAnalysis;
        this.questionsNumber = questionsNumber;
    }

    /**
     * Crea un set di domande chiamando il metodo relativo al tipo specifico.
     * @return set di domande da mostrare
     */
    public Set<Question> createQuestions(Consumer<Integer> progressUpdater) {
        Set<Question> questionSet = new HashSet<>();

        while (questionSet.size() < questionsNumber) {

            progressUpdater.accept(questionSet.size() + 1);

            QuestionType qt = questionTypes.get(new Random().nextInt(questionTypes.size()));
            Question q = null;

            switch (qt) {
                case TYPE1: q = createQuestionType1(); break;
                case TYPE2: q = createQuestionType2(); break;
                case TYPE3: q = createQuestionType3(); break;
                case TYPE4: q = createQuestionType4(); break;
            }

            if (q == null) continue;

            if (questionSet.contains(q)) continue;

            questionSet.add(q);
        }

        return questionSet;
    }

    /**
     * Ottiene una parola casuale dal testo scelto, ottiene la risposta corretta e
     * genera 3 numeri random per le risposte sbagliate.
     * @return la domanda completa di risposta esatta, testo domanda e risposte sbagliate
     */
    private Question createQuestionType1() {

        int randomFile = new Random().nextInt(choosenFiles.size());
        String fileName = choosenFiles.get(randomFile);

        List<String> allWordsInFile = new ArrayList<>(fileAnalysis.get(fileName).keySet());
        int randomWord = new Random().nextInt(allWordsInFile.size());

        String word = allWordsInFile.get(randomWord);
        Integer correctAnswer = fileAnalysis.get(fileName).get(word);

        Set<String> randomAnswer = new HashSet<>();
        randomAnswer.add(correctAnswer.toString());

        while (randomAnswer.size() < 4) {
            System.out.println("while type 1");
            Integer randomResult = new Random().nextInt(10);
            randomAnswer.add(randomResult.toString());
        }

        String domanda = QuestionType.TYPE1.getText()
                .replace("<parola>", word)
                .replace("<nome_documento>", fileName.replace(".txt", ""));

        return new Question(domanda, correctAnswer.toString(), randomAnswer);
    }

    /**
     * Prende la parola corretta chiamando correctAnswerType_1_2_3(), formula la domanda
     * e chiama getRandomWord per creare anche le risposte sbagliate.
     * @return la domanda completa di risposta esatta, testo domanda e risposte sbagliate
     */
    private Question createQuestionType2() {

        Random random = new Random();

        int randomFile = random.nextInt(choosenFiles.size());
        String fileName = choosenFiles.get(randomFile);

        Map<String, Integer> wordCounts = fileAnalysis.get(fileName);

        List<String> mostFrequentWords = new ArrayList<>();

        if (wordCounts != null && !wordCounts.isEmpty()) {

            int max = wordCounts.values().stream()
                    .max(Integer::compareTo)
                    .orElse(0);

            mostFrequentWords = wordCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        if (mostFrequentWords.isEmpty()) return null;

        String correctAnswer = mostFrequentWords.get(random.nextInt(mostFrequentWords.size()));

        String questionText = QuestionType.TYPE2.getText().
                replace("<nome_documento>", fileName.replace(".txt", ""));

        Set<String> randomAnswer = new HashSet<>();
        randomAnswer.add(correctAnswer);

        List<String> words = wordCounts.keySet().stream().collect(Collectors.toList());

        while(randomAnswer.size() < 4) {

            String word = words.get(random.nextInt(words.size()));
            randomAnswer.add(word);
        }

        return new Question(questionText, correctAnswer, randomAnswer);
    }

    private Question createQuestionType3() {
        String parola;
        String nomeFile = choosenFiles.get(0);
         fileAnalysis.get(nomeFile).keySet().stream().filter(
                 words-> fileAnalysis.keySet().stream().forEach(
                   file -> {
                       if (fileAnalysis.get(file).keySet().contains(words)){ parola = words;}

                   });
    }

    private Question createQuestionType4() {
        return null;
    }

    private String correctAnswerType_1_2_3() {
        return null;
    }

    /**
     * Prende parole a caso dalla mappa per mostrare le risposte sbagliate.
     * @param correctWord parola corretta da escludere
     * @return un set di 3 stringhe
     */
    private Set<String> getRandomWord(String correctWord) {
        return null;
    }

    /**
     * Ottiene dalla mappa il numero di occorrenze della parola nel file.
     * @param word parola scelta casualmente dal sistema
     * @param file file scelto casualmente dal sistema
     * @return risposta corretta di una domanda di Type1
     */
    private int getCorrectAnswer(String word, String file) {
        return fileAnalysis.getOrDefault(file, Collections.emptyMap())
                .getOrDefault(word, 0);
    }
}
