package dao;

import model.Section;
import util.DBUtil;
import dao.SectionDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SectionDaoImpl implements SectionDao {

    @Override
    public boolean createApplication(Connection conn, int userId, String name, String description) throws SQLException {
        String sql = "INSERT INTO section_applications(user_id, section_name, description) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Section> getPendingSections(Connection conn) throws SQLException {
        List<Section> sections = new ArrayList<>();
        String sql = "SELECT sa.*, u.username FROM section_applications sa " +
                "JOIN users u ON sa.user_id = u.id WHERE sa.status='PENDING'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sections.add(mapToSection(rs));
            }
        }
        return sections;
    }

    @Override
    public boolean approveSection(Connection conn, int applicationId, boolean approved) throws SQLException {
        // 更新申请状态
        String updateSql = "UPDATE section_applications SET status=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setString(1, approved ? "APPROVED" : "REJECTED");
            pstmt.setInt(2, applicationId);
            if (pstmt.executeUpdate() == 0) return false;
        }

        if (approved) {
            // 创建正式板块
            String createSql = "INSERT INTO sections(name, description, creator_id) " +
                    "SELECT section_name, description, user_id " +
                    "FROM section_applications WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(createSql)) {
                pstmt.setInt(1, applicationId);
                return pstmt.executeUpdate() > 0;
            }
        }
        return true;
    }

    @Override
    public boolean isModerator(Connection conn, int sectionId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM sections WHERE id = ? AND creator_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sectionId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Section getById(Connection conn, int id) throws SQLException {
        String sql = "SELECT s.*, u.username as creator_name FROM sections s " +
                "JOIN users u ON s.creator_id = u.id WHERE s.id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToSection(rs);
            }
            return null;
        }
    }

    @Override
    public List<Section> getUserSections(Connection conn, int userId) throws SQLException {
        List<Section> sections = new ArrayList<>();
        String sql = "SELECT s.* FROM sections s WHERE s.creator_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sections.add(mapToSection(rs));
            }
        }
        return sections;
    }

    private Section mapToSection(ResultSet rs) throws SQLException {
        Section section = new Section();
        section.setId(rs.getInt("id"));
        section.setName(rs.getString("name"));
        section.setDescription(rs.getString("description"));
        section.setCreatorId(rs.getInt("creator_id"));
        if (hasColumn(rs, "creator_name")) {
            section.setCreatorName(rs.getString("creator_name"));
        }
        return section;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}