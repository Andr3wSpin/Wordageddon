package controller.game_controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.Timer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Controller per la visualizzazione sequenziale dei testi letti da file.
 * Gestisce l’interfaccia utente per mostrare ogni testo e avanzare alla schermata delle domande.
 */
public class TextMenuController {

    @FXML
    private TextArea textArea;

    @FXML
    private Circle graphicTimer;

    @FXML
    private Label numberOfFileLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Button nextButton;

    private Game game;

    private List<File> files;

    private int index;

    private Timer timer;

    private String[] fileText;

    /**
     * Metodo chiamato automaticamente dopo il caricamento del file FXML.
     * Inizializza i componenti dell'interfaccia utente.
     */
    @FXML
    private void initialize() {
        index = 0;
        nextButton.setText("Show next text");
        textArea.setEditable(false);
        nextButton.setDisable(true);
    }

    /**
     * Avvia il controller con una specifica istanza di {@link Game}.
     * Carica i file da leggere, avvia il timer, e mostra il primo testo.
     *
     * @param game l'istanza del gioco da cui recuperare file e impostazioni
     */
    public void start(Game game) {
        numberOfFileLabel.setText("Loading...");
        this.game = game;
        this.files = game.getChoosenFiles();

        int totalSeconds = game.getDifficuty().getReadTimePerFile() * 60 * files.size();

        timer = new Timer(totalSeconds);

        setupCircleTimer(totalSeconds);

        timer.setOnTick(remainingSeconds -> {
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            String text = String.format("%02d:%02d", minutes, seconds);
            javafx.application.Platform.runLater(() -> timerLabel.setText(text));

            if (remainingSeconds == 0) {
                javafx.application.Platform.runLater(() -> {
                    PauseTransition pause = new PauseTransition(Duration.millis(100));
                    pause.setOnFinished(e -> {
                        showMessage("Time's Over", Alert.AlertType.ERROR);
                        loadQuestion();
                    });
                    pause.play();
                });
            }
        });

        if (files == null || files.isEmpty()) {
            textArea.setText("Nessun file disponibile.");
            numberOfFileLabel.setText("Text No. -");
            nextButton.setDisable(true);
        } else {
            fileText = getFileText();
            if (fileText == null) {
                showMessage("Impossibile ottenere il testo dei file!", Alert.AlertType.ERROR);
                return;
            }

            timer.start();
            nextButton.setDisable(false);
            index = 0;
            showText(fileText[index]);
        }

        if (index == (files.size() - 1)) {
            nextButton.setText("Go to questions");
        }
    }

    /**
     * Configura l'animazione circolare del timer grafico in base alla durata totale.
     *
     * @param totalSeconds durata totale in secondi del timer
     */
    private void setupCircleTimer(int totalSeconds) {
        double radius = graphicTimer.getRadius();
        double circ = 2 * Math.PI * radius;

        graphicTimer.getStrokeDashArray().addAll(circ);
        graphicTimer.setStrokeDashOffset(0);

        Timeline timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(graphicTimer.strokeDashOffsetProperty(), 0)),
                new KeyFrame(Duration.seconds(totalSeconds), new KeyValue(graphicTimer.strokeDashOffsetProperty(), circ))
        );
        timeLine.setCycleCount(1);
        timeLine.play();
    }

    /**
     * Mostra il contenuto testuale di un file specifico nell'area di testo.
     * Aggiorna anche l'etichetta con il numero del testo visualizzato.
     *
     * @param fileText il contenuto del file da mostrare
     */
    public void showText(String fileText) {
        numberOfFileLabel.setText("Text N°" + (index + 1));
        textArea.setText(fileText);
    }

    /**
     * Gestisce il click sul pulsante "Next".
     * Mostra il file successivo o, se è l'ultimo, passa alla schermata delle domande.
     *
     * @param event l'evento di click generato dal pulsante
     */
    @FXML
    private void nextText(ActionEvent event) {
        if (files == null || files.isEmpty()) {
            nextButton.setDisable(true);
            return;
        }

        if (index >= files.size() - 1) {
            loadQuestion();
            return;
        }

        index++;

        if (index == files.size() - 1) {
            nextButton.setText("Go to questions");
        }

        showText(fileText[index]);
    }

    /**
     * Carica la schermata delle domande alla fine della lettura dei testi.
     * Cambia scena passando alla vista "QuestionView.fxml".
     */
    private void loadQuestion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuestionView.fxml"));
            Parent root = loader.load();

            QuestionMenuController questionController = loader.getController();
            questionController.start(game);

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Legge il contenuto di ciascun file e lo converte in un array di stringhe.
     * Ogni stringa include il nome del file e il contenuto testuale.
     *
     * @return array di stringhe con il contenuto dei file, o {@code null} in caso di errore
     */
    private String[] getFileText() {
        String[] fileText = new String[files.size()];
        int index = 0;

        if (files == null || files.isEmpty()) return null;

        for (File file : files) {
            String fileName = file.getName().replace(".txt", "");
            StringBuilder sb = new StringBuilder(fileName + "\n");
            try {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                sb.append(content);
                fileText[index++] = sb.toString();
            } catch (IOException e) {
                return null;
            }
        }

        return fileText;
    }

    /**
     * Mostra un messaggio di alert all’utente.
     *
     * @param msg  il messaggio da mostrare
     * @param type il tipo di alert (es. ERROR, INFORMATION)
     */
    private void showMessage(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
    }
}
