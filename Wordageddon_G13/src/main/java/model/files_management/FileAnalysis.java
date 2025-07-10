package model.files_management;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileAnalysis extends Service<Map<String, Map<String, Integer>>> {

    /**
     * Percorso della cartella contenente il file analysis.dat
     */
    private final static Path ANALYSIS_DIR_PATH = Paths.get("/data/analysis/");

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
        return null;
    }

    /**
     * Salva in memoria la mappa contenente l'analisi dei file
     */
    private void saveAnalysis() {

    }

    /**
     * Legge la mappa salvata in memoria
     * @return la mappa contenente l'analisi dei documenti
     * @throws IOException se non riesce ad ottenere il file di analisi dalla memoria
     */
    public static Map<String, Map<String, Integer>> readAnalysis() throws IOException {

        return null;
    }

    /**
     * Ritorna il set di stopwords
     * @return set di stopwords
     */
    public Set<String> getStopwords() { return stopwords; }
}
