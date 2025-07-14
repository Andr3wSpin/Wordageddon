package model.questions_management;

import model.enums.QuestionType;

import java.util.*;
import java.util.stream.Collectors;

public class CreateQuestions {

    private Set<QuestionType> questionTypes;
    private List<String> choosenFiles;
    private Map<String, Map<String, Integer>> fileAnalysis;
    private int questionsNumber;
    private final List<QuestionType> orderList;

    public CreateQuestions(Set<QuestionType> questionTypes, List<String> choosenFiles,
                           Map<String, Map<String, Integer>> fileAnalysis, int questionsNumber) {

        this.questionTypes = questionTypes;
        this.orderList = questionTypes.stream().sorted().collect(Collectors.toList());
        this.choosenFiles = choosenFiles;
        this.fileAnalysis = fileAnalysis;
        this.questionsNumber = questionsNumber;
    }

    /**
     * Crea un set di domande chiamando il metodo relativo al tipo specifico
     * @return set di domande da mostrare
     */
    public Set<Question> createQuestions() {
        Set<Question> questionSet = new HashSet<>();

        while (questionSet.size() < questionsNumber-1){
            int r = new Random().nextInt(orderList.size());
            switch (r){

                case 0:
                    questionSet.add(createQuestionType1());
                    break;
                case 1:
                    questionSet.add(createQuestionType2());
                    break;
                case 2:
                    questionSet.add(createQuestionType3());
                    break;
                case 3:
                    questionSet.add(createQuestionType4());
                    break;
            }
        }

        return questionSet;
    }

    /**
     *
     * Ottiene una parola casuale dal testo scelto, ottiene la risposta corretta e
     * genera 3 numeri random per le risposte sbagliate
     * @return la domanda completa di risposta esatta, testo domanda e risposte sbagliate
     */
    private Question createQuestionType1() {

        String testo = QuestionType.TYPE1.getText();
        int wordIndex = new Random().nextInt(fileAnalysis.size());
        Random random = new Random();

        String parola = new ArrayList<>(fileAnalysis.keySet()).get(wordIndex);

        String domanda = testo.replace("'<parola>'", parola);
        domanda = domanda.replace("'<nome_documento>'",choosenFiles.get(random.nextInt(choosenFiles.size())));
        Integer correctAnswer = getCorrectAnswer(parola,choosenFiles.get(random.nextInt(choosenFiles.size())));

        Set<String> randomAnswer = new HashSet<>();
        randomAnswer.add(correctAnswer.toString());

        while (randomAnswer.size() < 4) {
           Integer rr = random.nextInt(8);

            randomAnswer.add(rr.toString());
        }

        Question q1 = new Question(domanda, correctAnswer.toString(), randomAnswer);

        return q1;
    }

    /**
     *
     * Prende la parola corretta chiamndo correctAnswerType_1_2_3() formula la domanda poi chiama getRandomWord per crerare anche le risposte sbagliate e creare la question
     * @return la domanda completa di  risposta esatta testo domanda e risposte sbagliata
     */
    private Question createQuestionType2() {

        String testo = QuestionType.TYPE2.getText();


        Set<Integer> wordIndex = new HashSet<>();

        while (wordIndex.size()<4)
            wordIndex.add(new Random().nextInt(fileAnalysis.size()));

        Map<String,Map <String,Integer>> fileWordCount = new HashMap<>();

        Set<String> randomAnswer = new HashSet<>();

        fileAnalysis.entrySet().stream().flatMap(
          entry -> entry.getValue().entrySet().stream().map(
                  f->
                      new Object[]{
                              f.getKey(),
                              entry.getKey(),
                              f.getValue(),
                          })).forEach(

                arr->{
                        String nomeFile = (String) arr[0];
                        String words = (String) arr[1];
                        Integer count = (Integer) arr[2];

                        fileWordCount.put(nomeFile,new HashMap<>());
                        fileAnalysis.get(nomeFile).put(words,count);
                });








        return null; }




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
    private int getCorrectAnswer(String word, String file) { int correct ;

       correct =  fileAnalysis.get(word).containsKey(file) ?  fileAnalysis.get(word).get(file) : 0;

            return correct;
    }
}
