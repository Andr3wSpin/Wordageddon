package model.files_management;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileAnalysis extends Service<Map<String, Map<String, Integer>>> implements Serializable {

    /**
     * Percorso della cartella contenente il file analysis.dat
     */
    private final static Path ANALYSIS_DIR_PATH = Paths.get("/data/analysis/");
    private final static Path FULL_PATH = ANALYSIS_DIR_PATH.resolve("analysis.dat");

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
    private void saveAnalysis() throws IOException {

        if(!Files.exists(ANALYSIS_DIR_PATH) || !(Files.isDirectory(ANALYSIS_DIR_PATH)))
            Files.createDirectories(ANALYSIS_DIR_PATH);

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FULL_PATH.toString()))) {
            oos.writeObject(this);
        } catch(IOException e) {
            throw new IOException("Si è verificato un errore durante il salvataggio del file di analisi!");
        }
    }

    /**
     * Legge la mappa salvata in memoria
     * @return la mappa contenente l'analisi dei documenti
     * @throws IOException se non riesce ad ottenere il file di analisi dalla memoria
     */
    public static Map<String, Map<String, Integer>> readAnalysis() throws IOException {

        FileAnalysis fa = null;

        if(!Files.exists(ANALYSIS_DIR_PATH) || !(Files.isDirectory(ANALYSIS_DIR_PATH))) return null;

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FULL_PATH.toString()))) {
            fa = (FileAnalysis) ois.readObject();
        } catch(Exception e) {
            throw new IOException("Si è verificato un errore durante la lettura del file di analisi!");
        }

        return fa.analysis;
    }

    /**
     * Ritorna il set di stopwords
     * @return set di stopwords
     */
    public Set<String> getStopwords() { return stopwords; }
}
