package br.edu.ifsp.matheus.controller.command;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        boolean expectsJson = false;
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            expectsJson = true;
        } else if ("json".equalsIgnoreCase(request.getParameter("format"))) {
            expectsJson = true;
        }

        if (expectsJson) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            var json = new JsonObject();
            json.addProperty("message", "Logout successful");
            response.getWriter().write(json.toString());
        } else {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
