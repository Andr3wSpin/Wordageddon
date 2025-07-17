package model;

import model.enums.UserType;

/**
 * Rappresenta un utente del sistema, può essere un normale player o admin.
 */
    public class User {

    private final int ID;

    private String username;

    /**
     * Tipo di utente  player o admin
     * Viene rappresentato tramite l'enum {@link UserType}.
     */
    private final UserType type;

    public User(int ID, String username, UserType type) {
        this.ID = ID;
        this.username = username;
        this.type = type;
    }

    public int getID() { return ID; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public UserType getType() { return type; }
}
