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

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    private ToggleGroup diffToggleGroup;

    private User user;
    private final WordageddonDAOSQLite accesDB = new WordageddonDAOSQLite();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        diffToggleGroup = new ToggleGroup();
        easyButton.setToggleGroup(diffToggleGroup);
        mediumButton.setToggleGroup(diffToggleGroup);
        hardButton.setToggleGroup(diffToggleGroup);
    }


    //BISOGNA PRIMA SETTARE USER ALTRIMENTI NON SA DI CHI CARICARE I PUNTEGGI
    public void setUser(User user){
        this.user = user;
    }

    public void loadLeaderBoard(ActionEvent event) {
        if(easyButton.isSelected())
            showLeaderboard(Difficulty.EASY);
        else
        if (mediumButton.isSelected())
            showLeaderboard(Difficulty.MEDIUM);
        else
        if (hardButton.isSelected())
            showLeaderboard(Difficulty.HARD);

    }


    public void loadScores(ActionEvent event) {
        if(easyButton.isSelected())
            showPlayerScores(Difficulty.EASY);
        else
            if (mediumButton.isSelected())
                showPlayerScores(Difficulty.MEDIUM);
            else
                if (hardButton.isSelected())
                    showPlayerScores(Difficulty.HARD);

    }

    public void showLeaderboard(Difficulty chosenDiff) {
        Difficulty diff;
        if (chosenDiff == null)
            diff = Difficulty.EASY;
        else
            diff = chosenDiff;
        leaderBoardButton.setVisible(true);
        scoresButton.setVisible(false);
        scoreTableView.getColumns().clear();

        TableColumn<ObservableList<String>, String> userNameCol = new TableColumn<>("Username");
        userNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(0)));

        TableColumn<ObservableList<String>, String> maxScoreCol = new TableColumn<>("Max Score");
        maxScoreCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(1)));

        scoreTableView.getColumns().addAll(userNameCol, maxScoreCol);


        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        List<String> leaderboardEntries = accesDB.leaderBoard(diff);

        for (String string : leaderboardEntries) {
            String[] parts = string.split("-");
            ObservableList<String> row = FXCollections.observableArrayList(parts);
            data.add(row);
        }

        scoreTableView.setItems(data);
        titleLabel.setText("Global Leaderboard");
        avgScoreLabel.setText("");
    }

    public void showPlayerScores(Difficulty chosenDiff) {

        scoresButton.setVisible(true);
        leaderBoardButton.setVisible(false);
        Difficulty diff;
        if (chosenDiff == null)
            diff = Difficulty.EASY;
        else
            diff = chosenDiff;

        scoreTableView.getColumns().clear();

        TableColumn<ObservableList<String>, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(0)));

        scoreTableView.getColumns().add(scoreCol);

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        List<String> scores = accesDB.playerScores(user.getID(),diff);

        for (String score : scores) {
            ObservableList<String> row = FXCollections.observableArrayList(score);
            data.add(row);
        }

        scoreTableView.setItems(data);
        titleLabel.setText("Your Scores ");
        avgScoreLabel.setText("Your average score: " + accesDB.avgScore(user.getID()));
    }
}
