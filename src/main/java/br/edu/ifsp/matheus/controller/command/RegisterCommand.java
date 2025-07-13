package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var name = request.getParameter("name");
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

            if (userDao.findByEmail(email) != null) {
                if (expectsJson) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    JsonObject json = new JsonObject();
                    json.addProperty("error", "Email already in use.");
                    response.getWriter().write(json.toString());
                } else {
                    request.setAttribute("error", "Email already in use.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
                return;
            }

            var user = new User(name, email, password);

            if (userDao.create(user)) {
                factory.commit();
                user = userDao.findByEmail(email);
                var session = request.getSession(true);
                session.setAttribute("user", user);

                if (expectsJson) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    JsonObject json = new JsonObject();
                    json.addProperty("message", "User registered successfully.");
                    json.addProperty("userId", user.getId());
                    response.getWriter().write(json.toString());
                } else {
                    response.sendRedirect("controller.do?action=transactions-page");
                }
            } else {
                factory.rollback();
                if (expectsJson) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObject json = new JsonObject();
                    json.addProperty("error", "Failed to complete the registration. Please try again.");
                    response.getWriter().write(json.toString());
                } else {
                    request.setAttribute("error", "Failed to complete the registration. Please try again.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            }
        }
    }
}
