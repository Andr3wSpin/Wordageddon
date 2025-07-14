package model.files_management;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileAnalysis extends Service<Map<String, Map<String, Integer>>> {

    private String regex = "[\\.:,;?! _-]+";

    /**
     * La mappa contiene l'analisi dei files
     */
    private Map<String, Map<String, Integer>> analysis;

    /**
     * Set di parole da escludere durante l'analisi dei documenti
     */
    private Set<String> stopwords;

    public FileAnalysis() {

        analysis = new HashMap<>();
        stopwords = new HashSet<>();
    }

    /**
     * Esegue l'analisi dei testi presenti nella cartella files
     * @return analisi dei testi
     */
    @Override
    protected Task<Map<String, Map<String, Integer>>> createTask() {
        return new Task<Map<String, Map<String, Integer>>>() {
            @Override
            protected Map<String, Map<String, Integer>> call() throws Exception {
                List<File> files;
                try {
                    files = FileManager.getFiles();
                } catch (IOException e) {
                    throw new Exception(e.getMessage());
                }

                analysis.clear();

                for (File file : files) {
                    analyzeFile(file);
                }

                saveAnalysis();

                return analysis;
            }
        };
    }


    private void analyzeFile(File file) throws IOException {

        Files.lines(file.toPath()).flatMap(line -> Arrays.stream(line.split(regex)))
                .map(String::toLowerCase)
                .forEach(word -> {
                    //System.out.println("sto analizzando parola per parola");
                    if(stopwords.contains(word)) return;

                    if(!analysis.containsKey(word)) {

                        Map<String, Integer> innerMap = new HashMap<>();
                        innerMap.put(file.getName(), 1);
                        analysis.put(word, innerMap);
                    }
                    else {
                        Map<String, Integer> innerMap = analysis.get(word);

                        if(!innerMap.containsKey(file.getName()))
                            innerMap.put(file.getName(), 1);
                        else {
                            int count = innerMap.get(file.getName());

                            count++;

                            innerMap.put(file.getName(), count);
                        }
                    }

                });
    }

    /**
     * Salva in memoria la mappa contenente l'analisi dei file
     */
    private void saveAnalysis() throws IOException {

        FileAnalysisData savedData = new FileAnalysisData(analysis, stopwords);
        savedData.saveAnalysis();
    }

    /**
     * Legge la mappa salvata in memoria
     * @return la mappa contenente l'analisi dei documenti
     * @throws IOException se non riesce ad ottenere il file di analisi dalla memoria
     */
    public FileAnalysis readAnalysis() throws IOException {

        FileAnalysisData savedData = FileAnalysisData.readAnalysis();

        if(savedData != null) {

            this.analysis = savedData.getAnalysis();
            this.stopwords = savedData.getStopwords();
        }

        return this;
    }

    public Map<String, Map<String, Integer>> getAnalysis() {
        return analysis;
    }

    /**
     * Ritorna il set di stopwords
     * @return set di stopwords
     */
    public Set<String> getStopwords() { return stopwords; }
}
