package model.files_management;

import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileAnalysisTest {

    @Test
    void call() throws InterruptedException {
        FileAnalysis fa = new FileAnalysis();

        fa.setOnSucceeded(e -> {
            Map<String, Map<String, Integer>> analysis = fa.getValue();
            System.out.println(analysis);
        });

        fa.setOnFailed(e -> System.out.println("Errore durante la lettura del file di analisi"));

        Thread thread = new Thread(fa);
        thread.setDaemon(true);
        thread.start();

        thread.join();  // aspetta che finisca il task per evitare che il test termini prima
    }

}