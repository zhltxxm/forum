package qgforum;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter implements Filter {
    // 不需要认证的白名单
    private static final String[] WHITE_LIST = {
            "/api/login",
            "/api/register"
    };

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 检查白名单
        for (String whitePath : WHITE_LIST) {
            if (path.startsWith(whitePath)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // 验证会话
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"请先登录\"}");
            return;
        }

        chain.doFilter(req, res);
    }
}