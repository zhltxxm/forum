package servlet;

import dao.UserDao;
import dao.UserDaoImpl;
import model.User;
import util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import com.alibaba.fastjson.JSON;

//@WebServlet("/api/user/current")
public class CurrentUserServlet extends HttpServlet {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            return;
        }

        int userId = (int) session.getAttribute("userId");
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            User user = userDao.getById(conn, userId);

            if (user == null) {
                session.invalidate();
                response.setStatus(404);
                return;
            }

            // 返回用户基本信息（不含密码）
            user.setPassword(null);
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString(user));

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            DBUtil.close(conn, null, null);
        }
    }
}