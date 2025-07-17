package model.files_management;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * Classe di supporto per la serializzazione e deserializzazione dell'analisi dei file.
 * Contiene la mappa dell'analisi delle parole e il set di stopwords.
 * I dati vengono salvati in un file binario "analysis.dat" all'interno della cartella "data/analysis/".
 */
class FileAnalysisData implements Serializable {

    /**
     * Percorso della cartella contenente il file di analisi.
     */
    private final static Path ANALYSIS_DIR_PATH = Paths.get("data/analysis/");

    /**
     * Percorso completo del file di analisi.
     */
    private final static Path FULL_PATH = ANALYSIS_DIR_PATH.resolve("analysis.dat");

    /**
     * Mappa che contiene per ogni file (key) come value una mappa di parole (key) e loro frequenze (value).
     */
    private Map<String, Map<String, Integer>> analysis;

    /**
     * Set di parole da escludere nell'analisi.
     */
    private Set<String> stopwords;

    /**
     * Costruttore che riceve l'analisi e le stopwords da salvare.
     *
     * @param analysis  Mappa contenente l'analisi delle parole per file.
     * @param stopwords Set di parole da escludere nell'analisi.
     */
    public FileAnalysisData(Map<String, Map<String, Integer>> analysis, Set<String> stopwords) {
        this.analysis = analysis;
        this.stopwords = stopwords;
    }

    /**
     * Salva l'istanza corrente su disco nel file "analysis.dat".
     * Crea la cartella se non esiste.
     *
     * @throws IOException Se si verifica un errore durante la creazione della cartella o il salvataggio del file.
     */
    public void saveAnalysis() throws IOException {
        if (!Files.exists(ANALYSIS_DIR_PATH) || !Files.isDirectory(ANALYSIS_DIR_PATH)) {
            Files.createDirectories(ANALYSIS_DIR_PATH);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FULL_PATH.toString()))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new IOException("Si è verificato un errore durante il salvataggio del file di analisi!", e);
        }
    }

    /**
     * Legge e ritorna un'istanza di FileAnalysisData dal file "analysis.dat".
     * Ritorna null se la cartella o il file non esistono.
     *
     * @return FileAnalysisData caricata da disco, oppure null se non presente.
     * @throws IOException Se si verifica un errore durante la lettura del file.
     */
    public static FileAnalysisData readAnalysis() throws IOException {
        FileAnalysisData savedData = null;

        if (!Files.exists(ANALYSIS_DIR_PATH) || !Files.isDirectory(ANALYSIS_DIR_PATH)) {
            return savedData;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FULL_PATH.toString()))) {
            savedData = (FileAnalysisData) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IOException("Errore durante la lettura del file di analisi: " + e.getMessage(), e);
        }

        return savedData;
    }

    /**
     * Getter per la mappa dell'analisi.
     *
     * @return Mappa con l'analisi delle parole per file.
     */
    public Map<String, Map<String, Integer>> getAnalysis() {
        return analysis;
    }

    /**
     * Getter per il set di stopwords.
     *
     * @return Set di parole da ignorare nell'analisi.
     */
    public Set<String> getStopwords() {
        return stopwords;
    }
}
