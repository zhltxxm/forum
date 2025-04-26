package dao;

import model.User;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {
    User getById(Connection conn, int id) throws SQLException;
    User getByUsername(Connection conn, String username) throws SQLException;
    boolean createUser(Connection conn, User user) throws SQLException;
    boolean updateUser(Connection conn, User user) throws SQLException;
    boolean banUser(Connection conn, int userId, boolean banned) throws SQLException;
}