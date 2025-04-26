package servlet;

import com.alibaba.fastjson.JSON;
import dao.SectionDao;
import model.Section;
import util.DBUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet("/api/section/apply")
public class SectionApplyServlet extends HttpServlet {
    private final SectionDao sectionDao = new SectionDao() {
        @Override
        public boolean createApplication(Connection conn, int userId, String name, String description) throws SQLException {
            return false;
        }

        @Override
        public List<Section> getPendingSections(Connection conn) throws SQLException {
            return Collections.emptyList();
        }

        @Override
        public boolean approveSection(Connection conn, int applicationId, boolean approved) throws SQLException {
            return false;
        }

        @Override
        public boolean isModerator(Connection conn, int sectionId, int userId) throws SQLException {
            return false;
        }

        @Override
        public Section getById(Connection conn, int id) throws SQLException {
            return null;
        }

        @Override
        public List<Section> getUserSections(Connection conn, int userId) throws SQLException {
            return Collections.emptyList();
        }
    };
    private Connection conn;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int userId = (int) req.getSession().getAttribute("userId");
        String sectionName = req.getParameter("name");
        String description = req.getParameter("desc");
        Map<String,Object> resultMap = new HashMap<>();
        try {
            conn= DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            boolean success = sectionDao.createApplication(
                    conn,userId, sectionName, description
            );
            resultMap.put("success",success);
            resp.getWriter().write(JSON.toJSONString(
                    resultMap
            ));
        } catch (SQLException e) {
            resp.sendError(500, "申请提交失败");
        }
    }
}