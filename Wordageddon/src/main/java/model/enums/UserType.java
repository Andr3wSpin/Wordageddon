package model.enums;

/**
 * Enum che rappresenta i tipi di utenti dell'applicazione.
 * Può essere un giocatore normale (PLAYER) o un amministratore (ADMIN).
 */
public enum UserType {

    /**
     * Utente standard che gioca al gioco.
     */
    PLAYER,

    /**
     * Utente con privilegi amministrativi.
     */
    ADMIN;
}
