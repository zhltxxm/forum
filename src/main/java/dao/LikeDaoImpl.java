package dao;

import util.DBUtil;
import dao.LikeDao;

import java.sql.*;

public class LikeDaoImpl implements LikeDao {

    @Override
    public boolean toggleLike(Connection conn, int userId, int postId) throws SQLException {
        // 检查是否已点赞
        if (checkLike(conn, userId, postId)) {
            // 取消点赞
            String deleteSql = "DELETE FROM post_likes WHERE user_id = ? AND post_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, postId);
                boolean success = pstmt.executeUpdate() > 0;
                if (success) {
                    updatePostLikes(conn, postId, -1);
                }
                return success;
            }
        } else {
            // 新增点赞
            String insertSql = "INSERT INTO post_likes(user_id, post_id) VALUES(?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, postId);
                boolean success = pstmt.executeUpdate() > 0;
                if (success) {
                    updatePostLikes(conn, postId, 1);
                }
                return success;
            }
        }
    }

    @Override
    public boolean checkLike(Connection conn, int userId, int postId) throws SQLException {
        String sql = "SELECT 1 FROM post_likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int getCountByPostId(Connection conn, int postId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM post_likes WHERE post_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public boolean toggleLike(int userId, int postId) {
        return false;
    }

    private void updatePostLikes(Connection conn, int postId, int delta) throws SQLException {
        String sql = "UPDATE posts SET likes = likes + ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, delta);
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
        }
    }
}