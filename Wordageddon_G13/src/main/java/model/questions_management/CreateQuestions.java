package model.questions_management;

import model.enums.QuestionType;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Classe responsabile della creazione di un insieme di domande di vari tipi
 * basate sull'analisi dei file testuali forniti.
 * <p>
 * Supporta la generazione di domande di quattro tipi differenti definiti in
 * {@link QuestionType}. La creazione delle domande si basa sulle parole e
 * sulle frequenze presenti nei file analizzati.
 * </p>
 */
public class CreateQuestions {

    private List<String> choosenFiles;
    private Map<String, Map<String, Integer>> fileAnalysis;
    private int questionsNumber;
    private final List<QuestionType> questionTypes;

    /**
     * Costruttore che inizializza il generatore di domande.
     *
     * @param questionTypes  insieme dei tipi di domanda da generare
     * @param choosenFiles   lista dei nomi dei file da cui estrarre i dati
     * @param fileAnalysis   mappa contenente per ogni file una mappa di parole e frequenze
     * @param questionsNumber numero totale di domande da generare
     */
    public CreateQuestions(Set<QuestionType> questionTypes, List<String> choosenFiles,
                           Map<String, Map<String, Integer>> fileAnalysis, int questionsNumber) {
        this.questionTypes = questionTypes.stream().collect(Collectors.toList());
        this.choosenFiles = choosenFiles;
        this.fileAnalysis = fileAnalysis;
        this.questionsNumber = questionsNumber;
    }

    /**
     * Genera un set di domande fino a raggiungere il numero richiesto.
     * Per ogni domanda sceglie casualmente un tipo tra quelli disponibili
     * e richiama il metodo specifico di creazione per quel tipo.
     *
     * @param progressUpdater funzione callback che riceve il progresso corrente (numero domanda creata)
     * @return un set di domande uniche generate
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
     * Crea una domanda di tipo 1:
     * Estrae una parola casuale da un file scelto a caso,
     * ottiene la sua frequenza come risposta corretta e genera
     * tre risposte errate numeriche casuali.
     *
     * @return una {@link Question} completa con testo, risposta corretta e risposte sbagliate
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
            Integer randomResult = new Random().nextInt(10);
            randomAnswer.add(randomResult.toString());
        }

        String domanda = QuestionType.TYPE1.getText()
                .replace("<parola>", word)
                .replace("<nome_documento>", fileName.replace(".txt", ""));

        return new Question(domanda, correctAnswer.toString(), randomAnswer);
    }

    /**
     * Crea una domanda di tipo 2:
     * Identifica le parole più frequenti in un file scelto,
     * seleziona una come risposta corretta e genera risposte errate
     * pescate casualmente dalle parole del file.
     *
     * @return una {@link Question} con testo, risposta corretta e risposte sbagliate
     */
    private Question createQuestionType2() {
        Random random = new Random();

        int randomFile = random.nextInt(choosenFiles.size());
        String fileName = choosenFiles.get(randomFile);

        Map<String, Integer> wordCounts = fileAnalysis.get(fileName);
        if (wordCounts == null || wordCounts.isEmpty()) return null;

        int max = wordCounts.values().stream()
                .max(Integer::compareTo)
                .orElse(0);

        List<String> mostFrequentWords = wordCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (mostFrequentWords.isEmpty()) return null;

        String correctAnswer = mostFrequentWords.get(random.nextInt(mostFrequentWords.size()));
        String questionText = QuestionType.TYPE2.getText()
                .replace("<nome_documento>", fileName.replace(".txt", ""));

        Set<String> randomAnswer = new HashSet<>();
        randomAnswer.add(correctAnswer);

        List<String> words = new ArrayList<>(wordCounts.keySet());

        while(randomAnswer.size() < 4) {
            String word = words.get(random.nextInt(words.size()));
            randomAnswer.add(word);
        }

        return new Question(questionText, correctAnswer, randomAnswer);
    }

    /**
     * Crea una domanda di tipo 3:
     * Trova le parole comuni a tutti i file scelti,
     * seleziona una parola corretta casuale tra queste e genera risposte errate.
     *
     * @return una {@link Question} con testo, risposta corretta e risposte sbagliate
     */
    private Question createQuestionType3() {
        List<Set<String>> wordSets = choosenFiles.stream()
                .map(f -> fileAnalysis.get(f).keySet())
                .collect(Collectors.toList());

        Set<String> commonWords = new HashSet<>(wordSets.get(0));
        wordSets.stream().skip(1).forEach(commonWords::retainAll);

        if (commonWords.isEmpty()) return null;

        Random rand = new Random();
        List<String> parole = new ArrayList<>(commonWords);
        String correctAnswer = parole.get(rand.nextInt(parole.size()));

        Set<String> answers = new HashSet<>();
        answers.add(correctAnswer);

        randomAnswerType3_4(answers);

        return new Question(QuestionType.TYPE3.getText(), correctAnswer, answers);
    }

    /**
     * Crea una domanda di tipo 4:
     * Tra le parole comuni a tutti i file, seleziona quella con la frequenza massima
     * in uno qualsiasi dei file come risposta corretta e genera risposte errate.
     *
     * @return una {@link Question} con testo, risposta corretta e risposte sbagliate
     */
    private Question createQuestionType4() {
        List<Set<String>> wordSets = choosenFiles.stream()
                .map(f -> fileAnalysis.get(f).keySet())
                .collect(Collectors.toList());

        Set<String> commonWords = new HashSet<>(wordSets.get(0));
        wordSets.stream().skip(1).forEach(commonWords::retainAll);

        Map<String,Integer> maxForCommonWords = commonWords.stream().collect(
                Collectors.toMap(
                        parola -> parola,
                        parola -> fileAnalysis.values().stream()
                                .mapToInt(mappa -> mappa.getOrDefault(parola, 0))
                                .max()
                                .orElse(0)
                )
        );

        Optional<String> optionalCorrectAnswer = maxForCommonWords.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey);

        if (optionalCorrectAnswer.isPresent()){
            String correctAnswer = optionalCorrectAnswer.get();

            Set<String> answer = new HashSet<>();
            answer.add(correctAnswer);
            randomAnswerType3_4(answer);
            return new Question(QuestionType.TYPE4.getText(), correctAnswer, answer);
        }
        return null;
    }

    /**
     * Genera risposte errate per le domande di tipo 3 e 4,
     * pescando parole casuali da un file scelto a caso.
     *
     * @param answers set delle risposte già presenti (inclusa quella corretta)
     * @return il set di risposte aggiornato con risposte errate
     */
    private Set<String> randomAnswerType3_4(Set<String> answers) {
        int random = new Random().nextInt(choosenFiles.size());
        List<String> allWords = new ArrayList<>(fileAnalysis.get(choosenFiles.get(random)).keySet());
        Random rand = new Random();
        while (answers.size() < 4 && allWords.size() > 0) {
            answers.add(allWords.get(rand.nextInt(allWords.size())));
        }
        return answers;
    }

    /**
     * Metodo placeholder per ottenere parole casuali escluse quelle corrette.
     * Da implementare se necessario.
     *
     * @param correctWord parola corretta da escludere
     * @return un set di 3 parole casuali (attualmente null)
     */
    private Set<String> getRandomWord(String correctWord) {
        return null;
    }

    /**
     * Restituisce il numero di occorrenze di una parola in un file.
     *
     * @param word parola da cercare
     * @param file nome del file in cui cercare
     * @return numero di occorrenze della parola nel file, 0 se non presente
     */
    private int getCorrectAnswer(String word, String file) {
        return fileAnalysis.getOrDefault(file, Collections.emptyMap())
                .getOrDefault(word, 0);
    }
}
