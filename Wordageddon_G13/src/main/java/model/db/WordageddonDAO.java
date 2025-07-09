package model.db;

import java.util.List;

public interface WordageddonDAO {

    /**
     * Verifica le credenziali di un utente confrontando userName e password.
     *
     * @param userName Il nome utente da verificare.
     * @param password La password da verificare.
     * @return true se le credenziali sono valide, false altrimenti.
     */
    boolean checkCredentials(String userName, String password);

    /**
     * Inserisce un nuovo utente nel database.
     *
     * @param userName Il nome utente del nuovo utente.
     * @param password La password del nuovo utente.
     * @return true se l'inserimento è avvenuto con successo, false altrimenti.
     */
    boolean insertUser(String userName, String password);

    /**
     * Aggiorna un attributo (username o password) di un utente specificato tramite ID.
     *
     * @param attribute L'attributo da aggiornare ("username" o "password").
     * @param ID L'ID dell'utente da aggiornare.
     * @param newValue Il nuovo valore per l'attributo specificato.
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti.
     */
    boolean updateUser(String attribute, String ID, String newValue);

    /**
     * Recupera la classifica dei migliori punteggi, mostrando il miglior punteggio per ogni utente.
     *
     * @return Una lista di stringhe, dove ogni stringa rappresenta un record della classifica formattato con i vari attributi.
     */
    List<String> leaderBoard();

    /**
     * Recupera tutti i punteggi di un determinato giocatore.
     *
     * @param ID l'id del giocatore di cui recuperare i punteggi.
     * @return Una lista di stringhe contenente i punteggi del giocatore.
     */
    List<String> playerScores(String ID);

    /**
     * Recupera una lista di tutti i nomi utente presenti nel database.
     *
     * @return Una lista di stringhe contenente i nomi di tutti i giocatori.
     */
    List<String> playersName();

    /**
     * Calcola il punteggio medio di un giocatore.
     *
     * @param player Il nome del giocatore di cui calcolare il punteggio medio.
     * @return Il punteggio medio del giocatore.
     */
    float avgScore(String player);

    /**
     * Inserisce un nuovo punteggio per una partita.
     * Vanno aggiunti ancora i parametri*
     * @return true se l'inserimento del punteggio è avvenuto con successo, false altrimenti.
     */
    boolean insertScore();
}