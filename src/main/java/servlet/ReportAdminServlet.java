package servlet;

import com.alibaba.fastjson.JSON;
import dao.ReportDao;
import model.Report;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet("/admin/report")
public class ReportAdminServlet extends HttpServlet {
    private final ReportDao reportDao = new ReportDao();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Report> reports = reportDao.getPendingReports();
            req.setAttribute("reports", reports);
            req.getRequestDispatcher("/admin/report_manage.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.sendError(500, "数据库错误");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean approved = "true".equals(req.getParameter("approved"));
        String remark = req.getParameter("remark");
        Map<String,Object> resultMap = new HashMap<>();

        try {
            boolean success = reportDao.processReport(id, approved, remark);
            resultMap.put("success",success);

            resp.getWriter().write(JSON.toJSONString(resultMap));
        } catch (SQLException e) {
            resp.sendError(500, "处理失败");
        }
    }
}