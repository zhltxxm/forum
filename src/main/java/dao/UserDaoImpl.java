package dao;

import model.User;
import util.DBUtil;
import dao.UserDao;
import model.User;

import java.sql.*;

public class UserDaoImpl implements UserDao {

    @Override
    public User getById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
            return null;
        }
    }

    @Override
    public User getByUsername(Connection conn, String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
            return null;
        }
    }





    @Override
    public boolean createUser(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO users(username, password, avatar, role) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getAvatar());
            pstmt.setString(4, user.getRole());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean updateUser(Connection conn, User user) throws SQLException {
        String sql = "UPDATE users SET username=?, avatar=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getAvatar());
            pstmt.setInt(3, user.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean banUser(Connection conn, int userId, boolean banned) throws SQLException {
        String sql = "UPDATE users SET banned=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, banned);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setAvatar(rs.getString("avatar"));
        user.setRole(rs.getString("role"));
        user.setBanned(rs.getBoolean("banned"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}