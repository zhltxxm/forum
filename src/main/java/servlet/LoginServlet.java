package servlet;

import com.alibaba.fastjson.JSONObject;
import dao.UserDao;
import dao.UserDaoImpl;
import model.User;
import util.DBUtil;
import dao.UserDaoImpl;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import util.DBUtil;

//@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            System.out.println("请求参数: " + request.getParameterMap());



        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        JSONObject json = JSON.parseObject(sb.toString());
        String username = json.getString("username");
        String password = json.getString("password");


            // 参数验证
            if (username == null || username.isEmpty() ||
                    password == null || password.isEmpty()) {
                response.setStatus(400);
                out.write("{\"success\":false, \"message\":\"参数缺失\"}");
                return;
            }

            // 业务逻辑
            out.write("{\"success\":true}");

        } catch (Exception e) {
            response.setStatus(500);
            out.write("{\"success\":false, \"message\":\"服务器异常\"}");
            e.printStackTrace();
        }
    }


}