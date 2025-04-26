package dao;

import model.Post;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface PostDao {
    // 创建帖子
    boolean create(Connection conn, Post post) throws SQLException;

    // 根据ID获取帖子
    Post getById(Connection conn, int postId) throws SQLException;

    // 分页获取帖子列表
    List<Post> getList(Connection conn, int sectionId, int page, int pageSize) throws SQLException;

    // 删除帖子
    boolean delete(Connection conn, int postId) throws SQLException;

    // 增加浏览量
    void incrementViews(Connection conn, int postId) throws SQLException;
}