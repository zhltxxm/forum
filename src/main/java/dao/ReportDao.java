package dao;

import model.Report;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    // 创建举报记录
    public boolean createReport(int reporterId, String targetType,
                                int targetId, String reason) throws SQLException {
        String sql = "INSERT INTO reports(reporter_id, target_type, target_id, reason) " +
                "VALUES(?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reporterId);
            pstmt.setString(2, targetType);
            pstmt.setInt(3, targetId);
            pstmt.setString(4, reason);
            return pstmt.executeUpdate() > 0;
        }
    }

    // 获取待处理举报列表
    public List<Report> getPendingReports() throws SQLException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT r.*, u.username as reporter_name " +
                "FROM reports r JOIN users u ON r.reporter_id = u.id " +
                "WHERE r.status='PENDING'";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reports.add(new Report(
                        rs.getInt("id"),
                        rs.getInt("reporter_id"),
                        rs.getString("reporter_name"),
                        rs.getString("target_type"),
                        rs.getInt("target_id"),
                        rs.getString("reason"),
                        rs.getTimestamp("created_at")
                ));
            }
        }
        return reports;
    }

    // 处理举报
    public boolean processReport(int reportId, boolean approved, String remark)
            throws SQLException {
        String sql = "UPDATE reports SET status=?, admin_remark=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, approved ? "PROCESSED" : "REJECTED");
            pstmt.setString(2, remark);
            pstmt.setInt(3, reportId);
            return pstmt.executeUpdate() > 0;
        }
    }
}