package qgforum;

import java.sql.Timestamp;

public class SectionApplication {
    private int id;
    private int userId;
    private String sectionName;
    private String description;
    private String status; // PENDING/APPROVED/REJECTED
    private Timestamp createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}