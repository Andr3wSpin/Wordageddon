package model;

import model.enums.UserType;

public class User {
    private final int ID;
    private String username;
    private String password;
    private final UserType type;

    public User(int ID, String username, String password, UserType type) {

        this.ID = ID;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public int getID() { return ID; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public UserType getType() { return type; }
}
