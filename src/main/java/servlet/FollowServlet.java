package servlet;

import com.alibaba.fastjson.JSON;
import dao.FollowDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/api/follow")
public class FollowServlet extends HttpServlet {
    private final FollowDao followDao = new FollowDao();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int userId = (int) req.getSession().getAttribute("userId");
        int targetUserId = Integer.parseInt(req.getParameter("targetUserId"));
        Map<String,Object> resultMap = new HashMap<>();
        try {
            boolean success = followDao.followUser(userId, targetUserId);
            resultMap.put("success",success);
            resp.getWriter().write(JSON.toJSONString(
                    resultMap
            ));
        } catch (SQLException e) {
            resp.sendError(500, "数据库操作失败");
        }
    }
}