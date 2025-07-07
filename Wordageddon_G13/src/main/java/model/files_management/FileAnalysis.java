package model.files_management;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileAnalysis extends Service<Map<String, Map<String, Integer>>> {

    /**
     * @brief percorso della cartella contenente il file analysis.dat
     */
    private final static Path ANALYSIS_DIR_PATH = Paths.get("/data/analysis/");
    /**
     * @brief la mappa contiene l'analisi dei files
     */
    private Map<String, Map<String, Integer>> analysis = new HashMap<>();

    /**
     * @brief esegue l'analisi dei testi presenti nella cartella files
     * @return analisi dei testi
     */
    @Override
    protected Task<Map<String, Map<String, Integer>>> createTask() {
        return null;
    }

    /**
     *
     * @return la mappa contenente l'analisi dei documenti
     */
    public static Map<String, Map<String, Integer>> readAnalysis() {

        return null;
    }
}
