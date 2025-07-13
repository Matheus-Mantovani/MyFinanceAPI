package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var email = request.getParameter("email");
        var password = request.getParameter("password");

        boolean expectsJson = false;
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            expectsJson = true;
        } else if ("json".equalsIgnoreCase(request.getParameter("format"))) {
            expectsJson = true;
        }

        try (var factory = new DAOFactory()) {
            var userDao = factory.getUserDAO();
            var user = userDao.findByEmail(email);
            var authenticated = User.authenticate(user, email, password);

            if (!authenticated) {
                if (expectsJson) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    var json = new JsonObject();
                    json.addProperty("error", "Invalid login");
                    response.getWriter().write(json.toString());
                } else {
                    request.setAttribute("error", "Invalid login");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
                return;
            }

            var session = request.getSession(true);
            session.setAttribute("user", user);

            if (expectsJson) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                var json = new JsonObject();
                json.addProperty("message", "Login successful");
                json.addProperty("userName", user.getName());
                json.addProperty("userEmail", user.getEmail());
                response.getWriter().write(json.toString());
            } else {
                response.sendRedirect("controller.do?action=transactions-page");
            }
        }
    }
}
