package service;

import dao.*;
import model.*;
import util.DBUtil;
import java.sql.*;
import java.util.*;

public class PostService {
    private final PostDao postDao = new PostDaoImpl();
    private final LikeDao likeDao = new LikeDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    // 获取帖子详情（包含作者信息和点赞状态）
    public Map<String, Object> getPostDetail(int postId, int currentUserId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            Post post = postDao.getById(conn, postId);
            if (post == null) return null;

            Map<String, Object> result = new HashMap<>();
            result.put("post", post);
            result.put("author", userDao.getById(conn, post.getUserId()));
            result.put("isLiked", likeDao.checkLike(conn, currentUserId, postId));

            // 增加浏览量
            postDao.incrementViews(conn, postId);
            return result;
        } finally {
            DBUtil.close(conn, null, null);
        }
    }

    // 创建新帖子（返回创建后的帖子ID）
    public int createPost(Post post) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            if (!postDao.create(conn, post)) {
                throw new SQLException("发帖失败");
            }

            conn.commit();
            return post.getId();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            DBUtil.close(conn, null, null);
        }
    }

    // 分页获取帖子列表（简化版）
    public List<Post> getPostList(int sectionId, int page, int pageSize) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            return postDao.getList(conn, sectionId, page, pageSize);
        } finally {
            DBUtil.close(conn, null, null);
        }
    }

    // 删除帖子（包含权限校验）
    public boolean deletePost(int postId, int operatorId) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            Post post = postDao.getById(conn, postId);
            User operator = userDao.getById(conn, operatorId);

            // 校验权限：管理员或版主
            boolean canDelete = "ADMIN".equals(operator.getRole()) ||
                    post.getUserId() == operatorId;

            if (!canDelete) return false;

            boolean success = postDao.delete(conn, postId);
            conn.commit();
            return success;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            DBUtil.close(conn, null, null);
        }
    }
}