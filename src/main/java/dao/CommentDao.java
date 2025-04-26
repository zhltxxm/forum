package dao;

import model.Comment;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CommentDao {
    boolean create(Connection conn, Comment comment) throws SQLException;
    List<Comment> getByPostId(Connection conn, int postId) throws SQLException;
    int getCountByPostId(Connection conn, int postId) throws SQLException;
    boolean delete(Connection conn, int commentId) throws SQLException;
}