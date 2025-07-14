package model.files_management;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

class FileAnalysisData implements Serializable {

    /**
     * Percorso della cartella contenente il file analysis.dat
     */
    private final static Path ANALYSIS_DIR_PATH = Paths.get("data/analysis/");
    /**
     * Percorso completo del file
     */
    private final static Path FULL_PATH = ANALYSIS_DIR_PATH.resolve("analysis.dat");

    private Map<String, Map<String, Integer>> analysis;
    private Set<String> stopwords;

    public FileAnalysisData(Map<String, Map<String, Integer>> analysis, Set<String> stopwords) {

        this.analysis = analysis;
        this.stopwords = stopwords;
    }

    public void saveAnalysis() throws IOException {

        if(!Files.exists(ANALYSIS_DIR_PATH) || !(Files.isDirectory(ANALYSIS_DIR_PATH)))
            Files.createDirectories(ANALYSIS_DIR_PATH);

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FULL_PATH.toString()))) {
            oos.writeObject(this);
        } catch(IOException e) {
            throw new IOException("Si è verificato un errore durante il salvataggio del file di analisi!");
        }
    }

    public static FileAnalysisData readAnalysis() throws IOException {

        FileAnalysisData savedData = null;

        if(!Files.exists(ANALYSIS_DIR_PATH) || !(Files.isDirectory(ANALYSIS_DIR_PATH))) return savedData;

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FULL_PATH.toString()))) {
            savedData = (FileAnalysisData) ois.readObject();
        } catch(Exception e) {
            throw new IOException("Si è verificato un errore durante la lettura del file di analisi!");
        }

        return savedData;
    }

    public Map<String, Map<String, Integer>> getAnalysis() { return analysis; }

    public Set<String> getStopwords() { return stopwords; }
}
