package model.files_management;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

/**
 * Gestisce la logica per aggiungere, eliminare e ottenere i file utilizzati dal sistema.
 * I file sono memorizzati nella cartella "data/files/".
 */
public class FileManager {

    /**
     * Percorso della cartella contenente i file gestiti dal sistema.
     */
    private final static Path FILES_DIR_PATH = Paths.get("data/files/");

    /**
     * Aggiunge una lista di file alla cartella "files".
     * Se un file con lo stesso nome esiste già, viene sovrascritto.
     *
     * @param files lista dei file da aggiungere
     * @throws IOException se non riesce a creare la cartella o a copiare i file
     */
    public static void addFiles(List<File> files) throws IOException {
        if (!Files.isDirectory(FILES_DIR_PATH)) {
            Files.createDirectories(FILES_DIR_PATH);
        }

        for (File file : files) {
            Path target = FILES_DIR_PATH.resolve(file.getName());
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Elimina una lista di file dalla cartella "files".
     * Viene verificato che il file si trovi effettivamente nella cartella "files" prima dell'eliminazione.
     *
     * @param files lista dei file da eliminare
     * @throws IOException se non riesce a eliminare i file
     */
    public static void deleteFiles(List<File> files) throws IOException {
        if (!Files.isDirectory(FILES_DIR_PATH)) {
            return;
        }

        for (File file : files) {
            Path target = file.toPath();

            // Protezione per eliminare solo file nella cartella "files"
            if (target.startsWith(FILES_DIR_PATH)) {
                Files.deleteIfExists(target);
            }
        }
    }

    /**
     * Recupera la lista di tutti i file presenti nella cartella "files".
     *
     * @return lista di file presenti nella cartella
     * @throws IOException se non riesce a leggere il contenuto della cartella
     */
    public static List<File> getFiles() throws IOException {
        return Files.walk(FILES_DIR_PATH)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }
}
