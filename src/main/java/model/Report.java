package model;

import java.sql.Timestamp;

public class Report {
    private int id;
    private int reporterId;
    private String reporterName;
    private String targetType; // POST/USER
    private int targetId;
    private String reason;
    private String status; // PENDING/PROCESSED
    private String adminRemark;
    private Timestamp createdAt;

    public Report(int id, int reporterId, String reporterName, String targetType, int targetId, String reason, Timestamp createdAt) {}

    public Report(int reporterId, String targetType, int targetId, String reason) {
        this.reporterId = reporterId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.reason = reason;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReporterId() { return reporterId; }
    public void setReporterId(int reporterId) { this.reporterId = reporterId; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public int getTargetId() { return targetId; }
    public void setTargetId(int targetId) { this.targetId = targetId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminRemark() { return adminRemark; }
    public void setAdminRemark(String adminRemark) { this.adminRemark = adminRemark; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}