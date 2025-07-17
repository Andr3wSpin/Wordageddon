package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;
import model.User;
import model.db.WordageddonDAOSQLite;
import model.enums.Difficulty;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller per la schermata di visualizzazione punteggi e leaderboard.
 * Permette all'utente di vedere i propri punteggi per difficoltà o la classifica globale.
 * Richiede un oggetto {@link model.User} impostato tramite {@link #setUser(User)}.
 */
public class ScoreController implements Initializable {

    @FXML
    private TableView<ObservableList<String>> scoreTableView;

    @FXML
    private Label avgScoreLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private RadioButton easyButton;

    @FXML
    private RadioButton mediumButton;

    @FXML
    private RadioButton hardButton;

    @FXML
    private Button scoresButton;

    @FXML
    private Button leaderBoardButton;

    @FXML
    private Button yourScoreButton;

    private ToggleGroup diffToggleGroup;

    private User user;

    private final WordageddonDAOSQLite accesDB = new WordageddonDAOSQLite();

    /**
     * Inizializza il controller e imposta il comportamento dei radio button di difficoltà.
     * Al primo avvio mostra la leaderboard della difficoltà EASY.
     *
     * @param location  Non utilizzato.
     * @param resources Non utilizzato.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        diffToggleGroup = new ToggleGroup();
        easyButton.setToggleGroup(diffToggleGroup);
        mediumButton.setToggleGroup(diffToggleGroup);
        hardButton.setToggleGroup(diffToggleGroup);
        diffToggleGroup.selectToggle(null);
        showLeaderboard(Difficulty.EASY);
        diffToggleGroup.selectToggle(easyButton);
    }

    /**
     * Imposta l'utente corrente per il quale visualizzare i punteggi personali.
     *
     * @param user Oggetto {@link User} autenticato.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Carica la leaderboard globale in base alla difficoltà selezionata.
     * Chiamato al click del pulsante "Leaderboard".
     *
     * @param event Evento di azione generato dal pulsante.
     */
    @FXML
    public void loadLeaderBoard(ActionEvent event) {
        if (easyButton.isSelected())
            showLeaderboard(Difficulty.EASY);
        else if (mediumButton.isSelected())
            showLeaderboard(Difficulty.MEDIUM);
        else if (hardButton.isSelected())
            showLeaderboard(Difficulty.HARD);
    }

    /**
     * Carica i punteggi personali dell'utente in base alla difficoltà selezionata.
     * Chiamato al click del pulsante "Your Scores".
     *
     * @param event Evento di azione generato dal pulsante.
     */
    @FXML
    public void loadYourScore(ActionEvent event) {
        if (easyButton.isSelected())
            showPlayerScores(Difficulty.EASY);
        else if (mediumButton.isSelected())
            showPlayerScores(Difficulty.MEDIUM);
        else if (hardButton.isSelected())
            showPlayerScores(Difficulty.HARD);
    }

    /**
     * Mostra la classifica globale per la difficoltà specificata.
     * Popola la tabella con le coppie Username - Max Score.
     * Al primo caricamento imposta di default la difficoltà EASY.
     *
     * @param chosenDiff La difficoltà selezionata (EASY, MEDIUM o HARD).
     */
    public void showLeaderboard(Difficulty chosenDiff) {
        Difficulty diff = (chosenDiff != null) ? chosenDiff : Difficulty.EASY;

        leaderBoardButton.setVisible(true);
        yourScoreButton.setVisible(false);
        scoreTableView.getColumns().clear();

        TableColumn<ObservableList<String>, String> userNameCol = new TableColumn<>("Username");
        userNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(0)));

        TableColumn<ObservableList<String>, String> maxScoreCol = new TableColumn<>("Max Score");
        maxScoreCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(1)));

        scoreTableView.getColumns().addAll(userNameCol, maxScoreCol);

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        List<String> leaderboardEntries = accesDB.leaderBoard(diff);

        for (String string : leaderboardEntries) {
            String[] parts = string.split(";");
            ObservableList<String> row = FXCollections.observableArrayList(parts);
            data.add(row);
        }

        scoreTableView.setItems(data);
        titleLabel.setText("Global Leaderboard");
        avgScoreLabel.setText("");
    }

    /**
     * Mostra i punteggi personali dell'utente per la difficoltà selezionata.
     * Popola la tabella con i punteggi registrati e calcola la media.
     *
     * @param chosenDiff La difficoltà selezionata (EASY, MEDIUM o HARD).
     */
    public void showPlayerScores(Difficulty chosenDiff) {
        titleLabel.setText("Your Scores");

        leaderBoardButton.setVisible(false);
        yourScoreButton.setVisible(true);
        Difficulty diff = (chosenDiff != null) ? chosenDiff : Difficulty.EASY;

        scoreTableView.getColumns().clear();

        TableColumn<ObservableList<String>, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(0)));

        scoreTableView.getColumns().add(scoreCol);

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        List<String> scores = accesDB.playerScores(user.getID(), diff);

        for (String score : scores) {
            ObservableList<String> row = FXCollections.observableArrayList(score);
            data.add(row);
        }

        scoreTableView.setItems(data);
        avgScoreLabel.setText("Your average score: " + accesDB.avgScore(user.getID()));
    }
}
