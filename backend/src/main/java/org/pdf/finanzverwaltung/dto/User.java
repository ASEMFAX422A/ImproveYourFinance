package org.pdf.finanzverwaltung.dto;

import org.pdf.finanzverwaltung.models.UserRole;

public class User {

    private long id;
    private String username;
    private String password;
    private UserRole userRole;

    public User(String username, String password, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public User(long id, String username, String password, UserRole userRole) {
        this(username, password, userRole);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
