package model.files_management;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private final String file1 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file1.txt";
    private final String file2 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file2.txt";
    private final String file3 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file3.txt";
    private final String file4 = "/C:/Users/andre/OneDrive/Desktop/Test_Progetto_Java/file4.txt";

    private List<File> files;

    @BeforeEach
    public void setUp() {

        files = new ArrayList<>();
        files.add(new File(file1));
        files.add(new File(file2));
        files.add(new File(file3));
        files.add(new File(file4));
    }

    @Test
    void addFiles() {
        try {
            FileManager.addFiles(files);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteFiles() {
    }

    @Test
    void getFiles() {
    }
}