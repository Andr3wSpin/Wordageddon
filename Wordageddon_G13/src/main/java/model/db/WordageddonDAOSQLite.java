package model.db;

import model.User;
import model.enums.Difficulty;
import model.enums.UserType;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordageddonDAOSQLite implements WordageddonDAO {

    private static final String DB_URL = "jdbc:sqlite:data/db/wordageddonDB.db";

    @Override
    public User getUser(int id) {

        User user = null;
        String sql = "SELECT username, isAdmin FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                boolean isAdmin = rs.getBoolean("isAdmin");
                UserType type = isAdmin ? UserType.ADMIN : UserType.PLAYER;
                user = new User(id, username, type); // password non caricata per sicurezza
            } else {
                System.out.println("Nessun utente trovato con ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dell'utente con ID " + id, e);
        }

        return user;
    }


    @Override
    public User checkCredentials(String userName, String password) {
        User user = null;
        String sql = "SELECT id, username, isAdmin FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                boolean isAdmin = rs.getBoolean("isAdmin");

                UserType type = isAdmin ? UserType.ADMIN : UserType.PLAYER;

                user = new User(id, username, type);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return user;
    }

    @Override
    public User insertUser(String userName, String password, boolean isAdmin) {
        String insertSql = "INSERT INTO users (username, password, isAdmin) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, userName);
            insertStmt.setString(2, password);
            insertStmt.setBoolean(3, isAdmin);

            int affectedRows = insertStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserimento nuovo utente fallito, nessuna riga aggiunta.");
            }

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    UserType type = isAdmin ? UserType.ADMIN : UserType.PLAYER;
                    return new User(id, userName, type);
                } else {
                    throw new SQLException("Inserimento nuovo utente non riuscito, nessun ID ottenuto");
                }
            }

        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("unique")) {
                return null;
            }
            System.err.println("Errore durante l'inserimento: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void deleteUser(int ID) {
        String query = "DELETE FROM users WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, ID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione dell'utente: " + e.getMessage());
            return;
        }
    }

    @Override
    public boolean updateUser(String attribute, int ID, String newValue) {
        if (!attribute.equals("username") && !attribute.equals("password")) {
            return false;
        }

        String query = "UPDATE users SET " + attribute + " = ? WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newValue);
            stmt.setInt(2, ID);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante la modifica delle credenziali: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> leaderBoard(Difficulty diff) {
        String selectQuery = "SELECT u.username, MAX(g.score) AS max_score " +
                "FROM users u JOIN games g ON u.id = g.user_id " +
                "WHERE g.difficulty = ? " +
                "GROUP BY u.username " +
                "ORDER BY max_score DESC";

        List<String> leaderBoardEntries = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {

            pstmt.setString(1, diff.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    int maxScore = rs.getInt("max_score");
                    leaderBoardEntries.add(username + " - " + maxScore);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore durante l'ottenimento della leaderboard: " + e.getMessage());
        }

        return leaderBoardEntries;
    }

    @Override
    public List<String> playerScores(int playerId, Difficulty difficulty) {
        List<String> scores = new ArrayList<>();
        String query = "SELECT score FROM games WHERE user_id = ? AND difficulty = ? ORDER BY score DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playerId);
            stmt.setString(2, difficulty.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int score = rs.getInt("score");
                    scores.add(String.valueOf(score));
                }
            }
        } catch (SQLException e) {
            System.err.println("Impossibile ottenere i punteggi del giocatore: " + e.getMessage());
        }

        return scores;
    }

    @Override
    public List<String> playersList() {
        List<String> players = new ArrayList<>();
        String query = "SELECT username FROM users ORDER BY username";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                players.add(username);
            }

        } catch (SQLException e) {
            System.out.println("Impossibile ottenere la lista dei giocatori: " + e.getMessage());
        }

        return players;
    }

    @Override
    public float avgScore(int playerId) {
        String query = "SELECT AVG(score) AS average_score FROM games WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble("average_score");
                    if (rs.wasNull()) {
                        return 0f;
                    }
                    return (float) avg;
                }
            }
        } catch (SQLException e) {
            System.err.println("Impossibile calcolare punteggio medio del giocatore: " + e.getMessage());
        }

        return 0f;
    }

    @Override
    public boolean insertScore(int playerId, String date, int score, String difficulty) {
        String insertQuery = "INSERT INTO games (user_id, game_date, difficulty, score) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, playerId);
            stmt.setString(2, date);
            stmt.setString(3, difficulty);
            stmt.setInt(4, score);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento del punteggio: " + e.getMessage());
            return false;
        }
    }
}
