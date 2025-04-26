package servlet;

import dao.PostDao;
import dao.PostDaoImpl;
import model.Post;
import util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/api/posts")
public class PostServlet extends HttpServlet {
    private final PostDao postDao = new PostDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 验证用户登录状态
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            response.getWriter().write("{\"success\":false, \"message\":\"请先登录\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 参数校验
        if (title == null || content == null || title.isEmpty() || content.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"success\":false, \"message\":\"标题和内容不能为空\"}");
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setUserId(userId);

            if (postDao.create(conn, post)) {
                response.getWriter().write("{\"success\":true, \"postId\":" + post.getId() + "}");
            } else {
                response.getWriter().write("{\"success\":false, \"message\":\"发帖失败\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"success\":false, \"message\":\"服务器错误\"}");
        } finally {
            DBUtil.close(conn, null, null);
        }
    }
}