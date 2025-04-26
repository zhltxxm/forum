package dao;

import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDao {
    // 关注用户
    public boolean followUser(int followerId, int followedUserId) throws SQLException {
        String sql = "INSERT INTO user_follows VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, followerId);
            pstmt.setInt(2, followedUserId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // 获取关注列表
    public List<Integer> getFollowingIds(int userId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT followed_user_id FROM user_follows WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        }
        return ids;
    }
}