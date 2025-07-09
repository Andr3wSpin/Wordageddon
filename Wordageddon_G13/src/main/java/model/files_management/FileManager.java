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
 * Gestisce la logica per aggiungere o eliminare i file utilizzati dal sistema e permette di ottenere una
 * lista di tutti i documenti presenti nella cartella "files"
 */
public  class FileManager {

    private final static Path FILES_DIR_PATH = Paths.get("data/files/");

    /**
     * Aggiunge alla cartella "files" i documenti selezionati dall'admin sostituendo quelli già presenti aventi
     * lo stesso nome dei nuovi
     * @param files lista dei documenti da aggiungere
     * @throws IOException se non riesce a creare l'albero di cartelle previste o se non riesce a copiare
     * i file nella cartella di destinazione
     */
    public static void addFiles(List<File> files) throws IOException {

       if(!Files.isDirectory(FILES_DIR_PATH)) Files.createDirectories(FILES_DIR_PATH);

       for(File file : files) {

           Path target = FILES_DIR_PATH.resolve(file.getName());
           Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
       }
    }

    /**
     * Elimina dalla cartella "files" i documenti selezionati dall'admin
     * @param files lista dei file da eliminare
     * @throws IOException se non riesce ad eliminare i file
     */
    public static void deleteFiles(List<File> files) throws IOException {

        if (!Files.isDirectory(FILES_DIR_PATH)) return;

        for (File file : files) {

            Path target = file.toPath();

            if (target.startsWith(FILES_DIR_PATH)) {

                Files.deleteIfExists(target);
            }
        }
    }

    /**
     * Ottiene una lista contenente tutti i file salvati nella cartella "files"
     * @return lista di file salvati in memoria
     * @throws IOException se non riesce ad ottenere i documenti presenti nella cartella
     */
    public static List<File> getFiles() throws IOException {

        return Files.walk(FILES_DIR_PATH)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }
}
