package model.files_management;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileAnalysis extends Service<Map<String, Map<String, Integer>>> implements Serializable {

    /**
     * Percorso della cartella contenente il file analysis.dat
     */
    private final static Path ANALYSIS_DIR_PATH = Paths.get("/data/analysis/");
    private final static Path FULL_PATH = ANALYSIS_DIR_PATH.resolve("analysis.dat");
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

                for (File file : files) {
                    analyzeFile(file);
                }

                saveAnalysis();

                return analysis;
            }
        };
    }


    public  void analyzeFile(File file) throws IOException {

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
