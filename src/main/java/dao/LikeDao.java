package dao;

import java.sql.SQLException;
import java.sql.Connection;

public interface LikeDao {
    boolean toggleLike(Connection conn, int userId, int postId) throws SQLException;
    boolean checkLike(Connection conn, int userId, int postId) throws SQLException;
    int getCountByPostId(Connection conn, int postId) throws SQLException;

    boolean toggleLike(int userId, int postId);
}