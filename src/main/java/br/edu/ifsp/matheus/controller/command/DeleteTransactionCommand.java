package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransactionCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try (var factory = new DAOFactory()) {
            var session = request.getSession();
            var transactionDao = factory.getTransactionDAO();
            var user = (User) session.getAttribute("user");

            response.setContentType("application/json");
            var gson = new Gson();

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                JsonObject json = new JsonObject();
                json.addProperty("error", "Unauthorized: User not logged in.");
                response.getWriter().write(gson.toJson(json));
                return;
            }

            var userId = user.getId();

            String idStr = request.getParameter("id");

            if (idStr == null || idStr.isBlank()) {
                try {
                    JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
                    if (json != null && json.has("id")) {
                        idStr = json.get("id").getAsString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (idStr == null || idStr.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject json = new JsonObject();
                json.addProperty("error", "Invalid transaction ID.");
                response.getWriter().write(gson.toJson(json));
                return;
            }

            long id = Long.parseLong(idStr);
            var transaction = transactionDao.findById(id);

            if (transaction == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JsonObject json = new JsonObject();
                json.addProperty("error", "Transaction not found.");
                response.getWriter().write(gson.toJson(json));
                return;
            }

            if (transaction.getPayerId() != userId) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                JsonObject json = new JsonObject();
                json.addProperty("error", "Forbidden: You cannot delete transactions from other users.");
                response.getWriter().write(gson.toJson(json));
                return;
            }

            if (transactionDao.delete(transaction)) {
                factory.commit();
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            } else {
                factory.rollback();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                JsonObject json = new JsonObject();
                json.addProperty("error", "Failed to delete the transaction. Please try again.");
                response.getWriter().write(gson.toJson(json));
            }
        }
    }
}
