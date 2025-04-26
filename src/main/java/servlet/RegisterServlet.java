package servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.UserDao;
import dao.UserDaoImpl;
import model.User;
import util.DBUtil;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 读取请求体中的 JSON 数据
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        JSONObject jsonObject = JSONObject.parseObject(sb.toString());
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            sendError(response, 400, "用户名和密码不能为空");
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            if (userDao.getByUsername(conn, username) != null) {
                sendError(response, 409, "用户名已存在");
                return;
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            newUser.setRole("USER");

            if (userDao.createUser(conn, newUser)) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "注册成功");
                response.getWriter().write(JSON.toJSONString(result));
            } else {
                sendError(response, 500, "注册失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendError(response, 500, "数据库错误");
        } finally {
            DBUtil.close(conn, null, null);
        }
    }

    private void sendError(HttpServletResponse response, int statusCode, String message)
            throws IOException {
        response.setStatus(statusCode);
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        response.getWriter().write(JSON.toJSONString(error));
    }
}