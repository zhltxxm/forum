package dao;

import model.Comment;
import util.DBUtil;
import dao.CommentDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {

    @Override
    public boolean create(Connection conn, Comment comment) throws SQLException {
        String sql = "INSERT INTO comments(content, user_id, post_id) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, comment.getContent());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setInt(3, comment.getPostId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        comment.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public List<Comment> getByPostId(Connection conn, int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.username FROM comments c " +
                "JOIN users u ON c.user_id = u.id WHERE c.post_id = ? " +
                "ORDER BY c.created_at DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setContent(rs.getString("content"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setPostId(rs.getInt("post_id"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comment.setAuthorName(rs.getString("username"));
                comments.add(comment);
            }
        }
        return comments;
    }

    @Override
    public int getCountByPostId(Connection conn, int postId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public boolean delete(Connection conn, int commentId) throws SQLException {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);
            return pstmt.executeUpdate() > 0;
        }
    }
}