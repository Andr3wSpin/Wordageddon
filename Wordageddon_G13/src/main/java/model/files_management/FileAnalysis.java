package model.files_management;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Servizio per l'analisi dei file di testo presenti in una cartella.
 * Conta la frequenza delle parole in ciascun file escludendo le stopwords.
 * L'analisi viene salvata e può essere ricaricata in memoria.
 */
public class FileAnalysis extends Service<Map<String, Map<String, Integer>>> {

    /**
     * Regex usata per dividere il testo in parole.
     * Divide su punteggiatura, spazi, underscore e trattini.
     */
    private String regex = "[\\.:,;?! _-]+";

    /**
     * Mappa che contiene per ogni file (key) come value una mappa di parole (key) e loro frequenze (value).
     */
    private Map<String, Map<String, Integer>> analysis;

    /**
     * Set di parole da ignorare durante l'analisi (stopwords).
     */
    private Set<String> stopwords;

    /**
     * Costruttore che inizializza le strutture dati.
     */
    public FileAnalysis() {
        analysis = new HashMap<>();
        stopwords = new HashSet<>();
    }

    /**
     * Crea il task eseguito dal Service.
     * Analizza tutti i file restituiti da FileManager.getFiles(),
     * aggiorna la mappa analysis e salva i dati in memoria.
     *
     * @return Task che restituisce la mappa di analisi completa.
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
                    throw new Exception("Errore durante il recupero dei file: " + e.getMessage());
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

    /**
     * Analizza un singolo file contando la frequenza delle parole,
     * ignorando quelle presenti in stopwords.
     *
     * @param file File da analizzare.
     * @throws IOException Se c'è un errore nella lettura del file.
     */
    private void analyzeFile(File file) throws IOException {
        Files.lines(file.toPath())
                .flatMap(line -> Arrays.stream(line.split(regex)))
                .map(String::toLowerCase)
                .forEach(word -> {
                    if (stopwords.contains(word)) return;

                    analysis.putIfAbsent(file.getName(), new HashMap<>());

                    Map<String, Integer> innerMap = analysis.get(file.getName());

                    innerMap.put(word, innerMap.getOrDefault(word, 0) + 1);
                });
    }

    /**
     * Salva in memoria l'analisi attuale e le stopwords usando FileAnalysisData.
     *
     * @throws IOException Se si verifica un errore durante il salvataggio.
     */
    private void saveAnalysis() throws IOException {
        FileAnalysisData savedData = new FileAnalysisData(analysis, stopwords);
        savedData.saveAnalysis();
    }

    /**
     * Carica da memoria l'analisi precedentemente salvata.
     * Se presente, aggiorna le mappe di analisi e le stopwords.
     *
     * @return L'istanza di FileAnalysis aggiornata con i dati caricati.
     * @throws IOException Se non è possibile leggere il file di analisi salvato.
     */
    public FileAnalysis readAnalysis() throws IOException {
        FileAnalysisData savedData = FileAnalysisData.readAnalysis();

        if (savedData != null) {
            this.analysis = savedData.getAnalysis();
            this.stopwords = savedData.getStopwords();
        }

        return this;
    }

    /**
     * Restituisce la mappa con l'analisi dei file.
     *
     * @return Mappa contenente per ogni file la mappa delle parole e frequenze.
     */
    public Map<String, Map<String, Integer>> getAnalysis() {
        return analysis;
    }

    /**
     * Restituisce il set di parole da ignorare nell'analisi.
     *
     * @return Set di stopwords.
     */
    public Set<String> getStopwords() {
        return stopwords;
    }
}
