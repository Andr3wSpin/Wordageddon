package model.db;

import model.User;
import model.enums.Difficulty;

import java.util.List;

public interface WordageddonDAO {

    /**
     * Verifica le credenziali di un utente confrontando userName e password.
     *
     * @param userName Il nome utente da verificare.
     * @param password La password da verificare.
     * @return true se le credenziali sono valide, false altrimenti.
     */
    public User checkCredentials(String userName, String password);

    /**
     * Inserisce un nuovo utente nel database.
     *
     * @param userName Il nome utente del nuovo utente.
     * @param password La password del nuovo utente.
     * @return true se l'inserimento è avvenuto con successo, false altrimenti.
     */
    public User insertUser(String userName, String password, boolean isAdmin);

    /**
     * Aggiorna un attributo (username o password) di un utente specificato tramite ID.
     *
     * @param attribute L'attributo da aggiornare ("username" o "password").
     * @param ID L'ID dell'utente da aggiornare.
     * @param newValue Il nuovo valore per l'attributo specificato.
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti.
     */
    public boolean updateUser(String attribute, int ID, String newValue);

    /**
     * Recupera la classifica dei migliori punteggi, mostrando il miglior punteggio per ogni utente.
     *
     * @return Una lista di stringhe, dove ogni stringa rappresenta un record della classifica formattato con i vari attributi.
     */
    public List<String> leaderBoard(Difficulty diff);

    /**
     * Recupera tutti i punteggi di un determinato giocatore.
     *
     * @param ID l'id del giocatore di cui recuperare i punteggi.
     * @return Una lista di stringhe contenente i punteggi del giocatore.
     */
    public List<String> playerScores(int ID, Difficulty difficulty);

    /**
     * Recupera una lista di tutti i nomi utente presenti nel database.
     *
     * @return Una lista di stringhe contenente i nomi di tutti i giocatori.
     */
    public List<String> playersList();

    /**
     * Calcola il punteggio medio di un giocatore.
     *
     * @param playerId L'id del giocatore di cui calcolare il punteggio medio.
     * @return Il punteggio medio del giocatore.
     */
    public float avgScore(int playerId);

    /**
     * Inserisce un nuovo punteggio per una partita di un giocatore specifico.
     *
     * @param playerId   l'id del giocatore a cui associare il punteggio
     * @param date       data e ora in cui è stata giocata la partita
     * @param score      il punteggio ottenuto dal giocatore
     * @param difficulty la difficoltà scelta per la partita
     * @return           true se l'inserimento del punteggio è avvenuto con successo, false altrimenti
     */
    public boolean insertScore(String playerId, String date, int score, String difficulty);
}