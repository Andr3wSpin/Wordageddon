package controller;

import controller.game_controllers.TextMenuController;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.User;
import model.enums.Difficulty;
import model.enums.QuestionType;
import model.files_management.FileManager;
import model.questions_management.CreateQuestions;
import model.questions_management.Question;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller per il menu di avvio di una nuova partita.
 * Gestisce l'interfaccia utente per la selezione della difficoltà,
 * l'avvio del processo di creazione della partita e la navigazione verso
 * la schermata di gioco.
 */
public class StartGameMenuController implements Initializable {

    @FXML
    private Button Button_StartGame;

    @FXML
    private Label LableButton_StartGame;

    @FXML
    private MediaView MediaViewWallPaper;

    @FXML
    private RadioButton RadioButton_Easy;

    @FXML
    private RadioButton RadioButton_Hard;

    @FXML
    private RadioButton RadioButton_Medium;

    private ToggleGroup toggleGroupDifficulty;

    @FXML
    private ProgressBar loadingBar;
    @FXML
    private ImageView imgButtonStart;

    private User user;
    private Map<String, Map<String, Integer>> fileAnalysis;

    /**
     * Inizializza il controller dopo che il suo elemento radice è stato completamente processato.
     * Configura lo sfondo e i radio button per la selezione della difficoltà.
     * @param location L'URL della posizione del file FXML, o {@code null} se sconosciuto.
     * @param resources Le risorse specifiche della localizzazione, o {@code null} se non usate.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBackground();
        initRadioButtons();
        loadingBar.setVisible(false);
    }

    /**
     * Avvia una nuova partita in base alla difficoltà selezionata.
     * Questo metodo gestisce la logica di creazione della partita in un thread separato
     * per non bloccare l'interfaccia utente, mostrando una barra di caricamento.
     * Se nessuna difficoltà è selezionata, mostra un messaggio di avviso.
     * In caso di successo, carica la scena del gioco; in caso di errore, mostra un alert.
     * @param event L'evento di azione (click del pulsante) che ha scatenato il metodo.
     */
    @FXML
    private void startGame(ActionEvent event) {
        Difficulty difficulty = takeDifficulty();

        if (difficulty == null) {
            showMessage("Selezionare una difficoltà.", Alert.AlertType.INFORMATION);
            event.consume(); // Consuma l'evento per evitare ulteriori elaborazioni
            return;
        }

        // Task per l'elaborazione della creazione del gioco in background
        Task<Game> gameTask = new Task<Game>() {
            @Override
            protected Game call() throws Exception {
                List<File> filesList = FileManager.getFiles();

                if (filesList.isEmpty())
                    throw new IOException("Nessun file disponibile per creare domande.");

                Collections.shuffle(filesList); // Mescola la lista dei file

                // Determina il numero massimo di testi da scegliere in base alla difficoltà
                int maxTextsToChoose = Math.min(filesList.size(), difficulty.getMaxTexts());

                // Seleziona un sottoinsieme di file
                List<File> chosenFiles = filesList.subList(0, maxTextsToChoose);

                Set<QuestionType> questionTypeSet = new HashSet<>();
                // Definisce i tipi di domande in base alla difficoltà e al numero di testi
                if (difficulty == Difficulty.EASY || maxTextsToChoose == 1) {
                    questionTypeSet.add(QuestionType.TYPE1);
                    questionTypeSet.add(QuestionType.TYPE2);
                } else {
                    questionTypeSet.add(QuestionType.TYPE1);
                    questionTypeSet.add(QuestionType.TYPE2);
                    questionTypeSet.add(QuestionType.TYPE3);
                    questionTypeSet.add(QuestionType.TYPE4);
                }

                System.out.println("Tipi di domande selezionati: " + questionTypeSet);

                List<String> fileNames = chosenFiles.stream()
                        .map(File::getName)
                        .collect(Collectors.toList());

                // Creazione delle domande, con aggiornamento della progress bar
                CreateQuestions cq = new CreateQuestions(questionTypeSet, fileNames, fileAnalysis, difficulty.getMaxQuestions());
                Set<Question> questions = cq.createQuestions(progress -> {
                    updateProgress(progress, difficulty.getMaxQuestions()); // Callback per aggiornare la progress bar
                });

                // Restituisce l'oggetto Game completato
                return new Game(difficulty, user.getID(), questions, chosenFiles);
            }
        };

        // Collega la visibilità e il progresso della barra di caricamento al task
        loadingBar.visibleProperty().bind(gameTask.runningProperty());
        loadingBar.progressProperty().bind(gameTask.progressProperty());

        // Azione da eseguire al successo del task
        gameTask.setOnSucceeded(e -> {
            showMessage("La partita è stata creata con successo.", Alert.AlertType.INFORMATION);
            Game g = gameTask.getValue();
            loadingBar.visibleProperty().unbind(); // Scollega la proprietà di visibilità
            loadingBar.setVisible(false); // Nasconde la barra di caricamento
            changeScene(g); // Cambia scena al gioco
        });

        // Azione da eseguire in caso di fallimento del task
        gameTask.setOnFailed(e -> {
            loadingBar.visibleProperty().unbind(); // Scollega la proprietà di visibilità
            loadingBar.setVisible(false); // Nasconde la barra di caricamento
            Throwable ex = gameTask.getException();
            String msg = "Errore durante la creazione della partita:\n" + ex.getMessage();
            showMessage(msg, Alert.AlertType.ERROR);
        });

        // Avvia il task in un nuovo thread
        new Thread(gameTask).start();
    }

    /**
     * Cambia la scena corrente alla schermata di gioco (TextMenuView).
     * Passa l'oggetto {@link Game} al controller della nuova scena.
     * @param g L'oggetto {@link Game} appena creato da passare al prossimo controller.
     */
    private void changeScene(Game g){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TextMenuView.fxml"));
            Scene textPage = new Scene(loader.load());

            Stage stage = (Stage) Button_StartGame.getScene().getWindow();
            stage.setScene(textPage);

            TextMenuController tmController = loader.getController();
            tmController.start(g); // Inizializza il controller del gioco con l'oggetto Game
        } catch (IOException e) {
            // Gestisce l'eccezione in caso di errore nel caricamento del FXML
            throw new RuntimeException("Impossibile caricare la vista del menu di testo.", e);
        }
    }

    /**
     * Recupera la difficoltà selezionata dall'utente tramite i RadioButton.
     * @return L'enumerazione {@link Difficulty} corrispondente alla selezione, o {@code null} se nessuna è selezionata.
     */
    private Difficulty takeDifficulty(){
        String select;
        Difficulty difficulty = null;
        RadioButton selectedRadio = (RadioButton) toggleGroupDifficulty.getSelectedToggle();

        if(selectedRadio == null) return difficulty; // Nessun radio button selezionato

        select = selectedRadio.getText();

        switch(select) {
            case "EASY":
                difficulty = Difficulty.EASY;
                break;
            case "MEDIUM":
                difficulty = Difficulty.MEDIUM;
                break;
            case "HARD":
                difficulty = Difficulty.HARD;
                break;
        }
        return difficulty;
    }

    /**
     * Inizializza il background della schermata con un video in loop.
     */
    private void initBackground() {
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/assets/sfondo.mp4")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaViewWallPaper.setMediaPlayer(mediaPlayer);
        MediaViewWallPaper.setPreserveRatio(false); // Non preserva il rapporto d'aspetto per riempire la vista
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Riproduzione in loop
        mediaPlayer.play();
    }

    /**
     * Inizializza i RadioButton per la selezione della difficoltà,
     * raggruppandoli in un {@link ToggleGroup}.
     */
    private void initRadioButtons() {
        toggleGroupDifficulty = new ToggleGroup();
        RadioButton_Easy.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Medium.setToggleGroup(toggleGroupDifficulty);
        RadioButton_Hard.setToggleGroup(toggleGroupDifficulty);
    }

    /**
     * Imposta l'utente corrente per il controller.
     * Questo è tipicamente chiamato da un controller precedente che passa l'oggetto {@link User} loggato.
     * @param user L'oggetto {@link User} dell'utente corrente.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Imposta la mappa di analisi dei file per il controller.
     * Questa mappa contiene informazioni sui file di testo necessarie per la creazione delle domande.
     * @param fileAnalysis Una mappa dove la chiave è il nome del file e il valore è un'altra mappa
     * contenente statistiche sul testo (es. frequenza parole).
     */
    public void setFileAnalysis(Map<String, Map<String, Integer>> fileAnalysis) {
        this.fileAnalysis = fileAnalysis;
    }

    /**
     * Mostra un messaggio all'utente tramite una finestra di alert.
     * @param msg Il messaggio da visualizzare.
     * @param type Il tipo di alert (es. {@code Alert.AlertType.INFORMATION}, {@code Alert.AlertType.ERROR}).
     */
    private void showMessage(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Gestisce l'evento di entrata del mouse sul pulsante "Start Game".
     * Avvia animazioni di traslazione e dissolvenza per il testo e l'immagine del pulsante,
     * creando un effetto di hover dinamico.
     * @param event L'evento del mouse che ha scatenato il metodo.
     */
    @FXML
    void btnStartHoverEntred(MouseEvent event) {
        TranslateTransition textSlide = new TranslateTransition(Duration.millis(500),LableButton_StartGame);
        textSlide.setFromX(0);
        textSlide.setToX(100);
        textSlide.play();

        FadeTransition textOpacity = new FadeTransition(Duration.millis(500),LableButton_StartGame);
        textOpacity.setFromValue(1.0);
        textOpacity.setToValue(0.0);
        textOpacity.play();

        TranslateTransition imageSlide = new TranslateTransition(Duration.millis(300),imgButtonStart);
        imageSlide.setFromX(0);
        imageSlide.setToX(35);
        imageSlide.play();
    }

    /**
     * Gestisce l'evento di uscita del mouse dal pulsante "Start Game".
     * Ripristina le animazioni di traslazione e dissolvenza per il testo e l'immagine del pulsante
     * al loro stato originale, annullando l'effetto di hover.
     * @param event L'evento del mouse che ha scatenato il metodo.
     */
    @FXML
    void btnStartHoverExit(MouseEvent event) {
        TranslateTransition textSlide = new TranslateTransition(Duration.millis(500),LableButton_StartGame);
        textSlide.setFromX(100);
        textSlide.setToX(0);
        textSlide.play();

        TranslateTransition imageSlide = new TranslateTransition(Duration.millis(500),imgButtonStart);
        imageSlide.setFromX(40);
        imageSlide.setToX(0);
        imageSlide.play();

        FadeTransition textOpacity = new FadeTransition(Duration.millis(500),LableButton_StartGame);
        textOpacity.setFromValue(0.0);
        textOpacity.setToValue(1.0);
        textOpacity.play();
    }
}