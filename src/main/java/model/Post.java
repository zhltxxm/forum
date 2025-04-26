package model;

import java.sql.Timestamp;

public class Post {
    private int id;
    private String title;
    private String content;
    private int userId;
    private int sectionId;
    private int views;
    private int likes;
    private Timestamp createdAt;

    public Post() {}

    public Post(String title, String content, int userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}