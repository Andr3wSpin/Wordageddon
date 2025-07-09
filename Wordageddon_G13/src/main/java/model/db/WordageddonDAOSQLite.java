package model.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordageddonDAOSQLite implements WordageddonDAO {


    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\paolo\\OneDrive\\Desktop\\Wordageddon\\Wordageddon_G13\\data\\db\\wordageddon.db";

    //qua crea la connessione
    //la chiusura è automatica nei vari metodi
    //quando metti la connessione nel try() alla fine chiude automaticamente tutto
    public Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean checkCredentials(String userName, String password) {
        String query = "Select password FROM users WHERE userName = ?";
        try(Connection conn = connect();
            java.sql.PreparedStatement stmt =  conn.prepareStatement(query))
        {
            stmt.setString(1,userName);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()) {
                    String storedPassword = rs.getNString("password");
                    return storedPassword.equals(password);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return false;
    }

    public boolean insertUser(String userName, String password) {
        String insertQuery = "INSERT INTO users (userName, password) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            insertStmt.setString(1, userName);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(String attribute, String ID, String newValue) {
        if (!attribute.equals("username") && !attribute.equals("password")) {
            return false;
        }

        String query = "UPDATE users SET " + attribute + " = ? WHERE ID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newValue);
            stmt.setString(2, ID);

            int rowsUpdated = stmt.executeUpdate(); //ritorna il numero di righe aggiornate con update
            return rowsUpdated > 0; //ritorna le righe aggiornate quindi se >0
            // vuol dire che ha aggiornato qualcosa

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> leaderBoard() {
        String query =
                "SELECT U.nome, G.dataGame, G.difficulty, G.score " +
                        "FROM USERS U " +
                        "JOIN GAMES G ON U.ID = G.userid " +
                        "WHERE G.score = (" +
                        "   SELECT MAX(G2.score) FROM GAMES G2 WHERE G2.userid = G.userid" +
                        ") " +
                        "GROUP BY U.ID " +
                        "ORDER BY G.score DESC";

        List<String> leaderboard = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nome = rs.getString("nome");//il parametro qua è il nome della colonna
                String data = rs.getString("dataGame");
                String difficolta = rs.getString("difficulty");
                int punteggio = rs.getInt("score");

                String record = String.format(
                        "%s;%d punti;%s;Difficoltà;%s",
                        nome, punteggio, data, difficolta
                );//riga della tabella formattata poi possiamo cambiarlo

                leaderboard.add(record); //mette nella lista le varie righe formattate
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero della leaderboard: " + e.getMessage());
        }

        return leaderboard;
    }

    @Override
    public List<String> playerScores(String player) {
        return Collections.emptyList();
    }

    @Override
    public List<String> playersName() {
        return Collections.emptyList();
    }

    @Override
    public float avgScore(String player) {
        return 0;
    }

    @Override
    public boolean insertScore() {
        return false;
    }
}