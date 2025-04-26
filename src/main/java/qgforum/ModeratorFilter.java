package qgforum;

import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/moderator/*")
public class ModeratorFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)req).getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("MODERATOR")) {
            ((HttpServletResponse)res).sendError(403, "权限不足");
            return;
        }
        chain.doFilter(req, res);
    }
}