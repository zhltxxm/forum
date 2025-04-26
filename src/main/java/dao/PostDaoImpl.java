package dao;

import model.Post;
import util.DBUtil;
import dao.PostDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDaoImpl implements PostDao {

    @Override
    public boolean create(Connection conn, Post post) throws SQLException {
        String sql = "INSERT INTO posts(title, content, user_id, section_id) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getUserId());
            pstmt.setInt(4, post.getSectionId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        post.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public Post getById(Connection conn, int postId) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setUserId(rs.getInt("user_id"));
                post.setSectionId(rs.getInt("section_id"));
                post.setViews(rs.getInt("views"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                return post;
            }
            return null;
        }
    }

    @Override
    public List<Post> getList(Connection conn, int sectionId, int page, int pageSize) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = sectionId > 0 ?
                "SELECT * FROM posts WHERE section_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?" :
                "SELECT * FROM posts ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int offset = (page - 1) * pageSize;
            if (sectionId > 0) {
                pstmt.setInt(1, sectionId);
                pstmt.setInt(2, pageSize);
                pstmt.setInt(3, offset);
            } else {
                pstmt.setInt(1, pageSize);
                pstmt.setInt(2, offset);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setUserId(rs.getInt("user_id"));
                post.setCreatedAt(rs.getTimestamp("created_at"));
                posts.add(post);
            }
        }
        return posts;
    }

    @Override
    public boolean delete(Connection conn, int postId) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public void incrementViews(Connection conn, int postId) throws SQLException {
        String sql = "UPDATE posts SET views = views + 1 WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        }
    }
}