package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private String avatar;
    private String role; // USER/MODERATOR/ADMIN
    private boolean banned;
    private Timestamp createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isBanned() { return banned; }
    public void setBanned(boolean banned) { this.banned = banned; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}