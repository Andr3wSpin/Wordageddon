package model;

import model.enums.UserType;

public class User {
    private final int ID;
    private String username;
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
