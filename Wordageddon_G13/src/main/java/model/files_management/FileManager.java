package model.files_management;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.nio.file.StandardCopyOption;

public  class FileManager {

    private final static Path FILES_DIR_PATH = Paths.get("data/files/");

    /**
     * Aggiunge alla cartella "files" i file selezionati dall'admin, sostituendo quelli già presenti aventi
     * lo stesso nome dei nuovi
     * @param files lista dei file da aggiungere
     * @throws IOException se non riesce a creare l'albero di cartelle previste
     */
    public static void addFiles(List<File> files) throws IOException {

       if(!Files.isDirectory(FILES_DIR_PATH)) Files.createDirectories(FILES_DIR_PATH);

        for(File file : files) {

            Path target = FILES_DIR_PATH.resolve(file.getName());
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Elimina dalla cartella "files" i file selezionati dall'admin
     * @param files lista dei file da eliminare
     */
    public static void deleteFiles(List<File> files) {
        if(!Files.isDirectory(FILES_DIR_PATH)) return;

        for(File file : files) Files.deleteIfExists(file.toPath());
    }

    /**
     * Ottiene una lista contenente tutti i file salvati nella cartella "files"
     * @return lista di file salvati in memoria
     */
    public static List<File> getFiles() { return null; }
}
