package model.files_management;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public  class FileManager {

    private final static Path FILES_DIR_PATH = Paths.get("/data/files/");

    /**
     * Aggiunge alla cartella "files" i file selezionati dall'admin
     * @param files lista dei file da aggiungere
     */
    public static void addFiles(List<File> files) {

    }

    /**
     * Elimina dalla cartella "files" i file selezionati dall'admin
     * @param files lista dei file da eliminare
     */
    public static void deleteFiles(List<File> files) {

    }

    /**
     * Ottiene una lista contenente tutti i file salvati nella cartella "files"
     * @return lista di file salvati in memoria
     */
    public static List<File> getFiles() { return null; }
}
