package controller.game_controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Game;
import javafx.event.ActionEvent;
import model.Timer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controller per la visualizzazione sequenziale di testi letti da file.
 * Gestisce la visualizzazione del testo, la navigazione tra i testi e un timer complessivo.
 */
public class TextMenuController {

    /**
     * Area di testo dove viene mostrato il contenuto del file corrente.
     */
    @FXML
    private TextArea textArea;

    /**
     * Label che mostra il numero del testo attualmente visualizzato.
     */
    @FXML
    private Label numberOfFileLabel;

    /**
     * Label che mostra il tempo rimanente del timer complessivo.
     */
    @FXML
    private Label timerLabel;

    /**
     * Pulsante per passare al testo successivo o alla schermata delle domande.
     */
    @FXML
    private Button nextButton;

    /**
     * Riferimento all'istanza di gioco contenente i file e le impostazioni.
     */
    private Game game;

    /**
     * Lista dei file di testo da mostrare.
     */
    private List<File> files;

    /**
     * Indice del file attualmente visualizzato.
     */
    private int index;

    /**
     * Timer che gestisce il conto alla rovescia totale.
     */
    private Timer timer;

    /**
     * Metodo di inizializzazione chiamato automaticamente da JavaFX dopo il caricamento del FXML.
     * Imposta lo stato iniziale dei componenti UI.
     */
    @FXML
    private void initialize() {
        index = 0;
        nextButton.setText("Show next text");
        textArea.setEditable(false);
        nextButton.setDisable(true);
    }

    /**
     * Avvia il controller con una specifica istanza di gioco.
     * Inizializza la lista dei file, crea il timer e mostra il primo testo.
     *
     * @param game istanza di gioco con configurazioni e file da leggere
     */
    public void start(Game game){
        numberOfFileLabel.setText("Loading...");
        this.game = game;

        files = game.getChoosenFiles();

        timer = new Timer(game.getDifficuty().getReadTimePerFile() * 60 * files.size());

        timer.setOnTick(remainingSeconds -> {
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            String text = String.format("%02d:%02d", minutes, seconds);

            javafx.application.Platform.runLater(() -> timerLabel.setText(text));
        });

        if (files == null || files.isEmpty()) {
            textArea.setText("Nessun file disponibile.");
            numberOfFileLabel.setText("Text No. -");
            nextButton.setDisable(true);
        } else {
            timer.start();
            nextButton.setDisable(false);
            index = 0;
            showText();
        }
    }

    /**
     * Mostra il contenuto del file corrente nell'area di testo.
     * Aggiorna anche l'etichetta del numero di file visualizzato.
     */
    public void showText() {
        numberOfFileLabel.setText("Text N°" + (index + 1));

        if (files != null && !files.isEmpty() && index < files.size()) {
            File currentFile = files.get(index);
            try {
                String text = new String(Files.readAllBytes(currentFile.toPath()), StandardCharsets.UTF_8);
                textArea.setText(text);
            } catch (IOException e) {
                textArea.setText("Errore nella lettura del file: " + e.getMessage());
            }
        } else {
            textArea.setText("Nessun file disponibile.");
        }
    }

    /**
     * Gestisce l'evento di click sul pulsante "next".
     * Passa al testo successivo o, se è l'ultimo, cambia scena per mostrare le domande.
     *
     * @param event evento di azione generato dal pulsante
     */
    @FXML
    private void handleNextButton(ActionEvent event) {
        index += 1;
        if (files == null || files.isEmpty()) {
            nextButton.setDisable(true);
            return;
        }
        if (index < files.size()) {
            if (index == files.size() - 1) {
                nextButton.setText("Go to questions");
            }
            showText();
        } else if (index == files.size()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionView.fxml"));
                Parent root = loader.load();

                QuestionMenuController questionController = loader.getController();
                questionController.start(game);

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
