package model.files_management;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private static final String file1 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file1.txt";
    private static final String file2 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file2.txt";
    private static final String file3 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file3.txt";
    private static final String file4 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file4.txt";

    private static final String file5 = "/C:/Users/andre/Documents/GitHub/Wordageddon/Wordageddon_G13/data/files/file1.txt/";
    private static final String file6 = "/C:/Users/andre/Documents/GitHub/Wordageddon/Wordageddon_G13/data/files/file2.txt/";
    private static final String file7 = "/C:/Users/andre/Documents/GitHub/Wordageddon/Wordageddon_G13/data/files/file3.txt/";
    private static final String file8 = "/C:/Users/andre/Documents/GitHub/Wordageddon/Wordageddon_G13/data/files/file4.txt/";

    private static List<File> files1;
    private static List<File> files2;

    @BeforeAll
    public static void setUp() {

        files1 = new ArrayList<>();
        files1.add(new File(file1));
        files1.add(new File(file2));
        files1.add(new File(file3));
        files1.add(new File(file4));

        files2 = new ArrayList<>();
        files2.add(new File(file5));
        files2.add(new File(file6));
        files2.add(new File(file7));
        files2.add(new File(file8));
    }

    @Test
    void addFiles() {
        try {
            FileManager.addFiles(files1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

//    @Test
//    void deleteFiles() {
//        try {
//            FileManager.deleteFiles(files2);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    @Test
    void getFiles() {

        List<File> fileList = null;
        try {
            fileList = FileManager.getFiles();
        } catch (IOException e) {
            System.out.println("Errore durante l'ottenimento dei file");
        }

        if(fileList == null) return;

        StringBuilder sb = new StringBuilder("FILES PRESENTI NELLA CARTELLA\n");
        for(File file : fileList) {
            sb.append(file.getName() + "\n");
        }

        System.out.println(sb);
    }
}