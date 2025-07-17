package model.db;

import model.User;
import model.enums.Difficulty;
import java.util.List;

public interface WordageddonDAO {

    /**
     * Recupera un utente dal database tramite il suo ID.
     *
     * @param Id L'ID dell'utente da cercare.
     * @return L'oggetto User corrispondente o null se non trovato.
     */
    public User getUser(int Id);

    /**
     * Verifica le credenziali di un utente confrontando userName e password.
     *
     * @param userName Il nome utente da verificare.
     * @param password La password da verificare.
     * @return L'oggetto User se le credenziali sono valide, altrimenti null.
     */
    public User checkCredentials(String userName, String password);

    /**
     * Inserisce un nuovo utente nel database.
     *
     * @param userName Il nome utente del nuovo utente.
     * @param password La password del nuovo utente.
     * @param isAdmin  True se l'utente è un amministratore, false altrimenti.
     * @return L'oggetto User se l'inserimento è avvenuto con successo, null altrimenti.
     */
    public User insertUser(String userName, String password, boolean isAdmin);

    /**
     * Elimina l'utente specificato dal database.
     *
     * @param ID L'ID dell'utente da eliminare.
     */
    public void deleteUser(int ID);

    /**
     * Aggiorna un attributo (username o password) di un utente specifico.
     *
     * @param attribute L'attributo da aggiornare ("username" o "password").
     * @param ID        L'ID dell'utente da aggiornare.
     * @param newValue  Il nuovo valore da assegnare all'attributo.
     * @return True se l'aggiornamento è avvenuto con successo, false altrimenti.
     */
    public boolean updateUser(String attribute, int ID, String newValue);

    /**
     * Recupera la classifica dei migliori punteggi per la difficoltà specificata.
     *
     * @param diff La difficoltà selezionata.
     * @return Una lista di stringhe rappresentanti i record ordinati per punteggio decrescente.
     */
    public List<String> leaderBoard(Difficulty diff);

    /**
     * Recupera tutti i punteggi registrati da un determinato giocatore per una data difficoltà.
     *
     * @param ID         L'ID del giocatore.
     * @param difficulty La difficoltà delle partite da considerare.
     * @return Una lista di stringhe contenenti i punteggi.
     */
    public List<String> playerScores(int ID, Difficulty difficulty);

    /**
     * Recupera una lista di tutti i nomi utente presenti nel database.
     *
     * @return Una lista di stringhe contenente i nomi utente.
     */
    public List<String> playersList();

    /**
     * Calcola il punteggio medio ottenuto da un giocatore in tutte le partite.
     *
     * @param playerId L'ID del giocatore.
     * @return Il punteggio medio come valore float, o 0 se nessun punteggio è presente.
     */
    public float avgScore(int playerId);

    /**
     * Inserisce un nuovo punteggio per una partita giocata da uno specifico giocatore.
     *
     * @param playerId   L'ID del giocatore.
     * @param date       La data in cui è stata giocata la partita.
     * @param score      Il punteggio ottenuto.
     * @param difficulty La difficoltà della partita.
     * @return True se l'inserimento è avvenuto con successo, false altrimenti.
     */
    public boolean insertScore(int playerId, String date, int score, String difficulty);
}
