package servlet;

import dao.LikeDao;
import com.alibaba.fastjson.JSON;
import dao.LikeDaoImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/api/like")
public class LikeServlet extends HttpServlet {
    private final LikeDao likeDao = new LikeDaoImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 从会话中获取当前用户ID
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未登录");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        int postId = Integer.parseInt(req.getParameter("postId"));

        resp.setContentType("application/json");
        Map<String, Object> result = new HashMap<>();

        try {
            boolean success = likeDao.toggleLike(userId, postId);
            result.put("success", success);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("error", "操作失败");
            e.printStackTrace();
        }

        resp.getWriter().write(JSON.toJSONString(result));
    }
}