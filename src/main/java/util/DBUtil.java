package util;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/forum?useSSL=false&characterEncoding=UTF-8&useUnicode=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Zhl060903@";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL驱动加载失败");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}