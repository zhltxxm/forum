package servlet;

import com.alibaba.fastjson.JSON;
import dao.ReportDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/api/report")
public class ReportServlet extends HttpServlet {
    private final ReportDao reportDao = new ReportDao();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int reporterId = (int) req.getSession().getAttribute("userId");
        String targetType = req.getParameter("type");
        int targetId = Integer.parseInt(req.getParameter("targetId"));
        String reason = req.getParameter("reason");
        Map<String,Object> resultMap = new HashMap<>();
        try {
            boolean success = reportDao.createReport(
                    reporterId, targetType, targetId, reason
            );
            resultMap.put("success",success);
            resp.getWriter().write(JSON.toJSONString(
                    resultMap
            ));
        } catch (SQLException e) {
            resp.sendError(500, "举报提交失败");
        }
    }
}