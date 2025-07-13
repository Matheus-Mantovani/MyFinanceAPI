package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.dto.TransactionDTO;
import br.edu.ifsp.matheus.model.User;
import br.edu.ifsp.matheus.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public class GetTransactionByIdCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        var session = request.getSession(false);
        var user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject json = new JsonObject();
            json.addProperty("error", "Unauthorized: User not logged in.");
            response.getWriter().write(new Gson().toJson(json));
            return;
        }

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject json = new JsonObject();
            json.addProperty("error", "Invalid or missing transaction ID.");
            response.getWriter().write(new Gson().toJson(json));
            return;
        }

        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject json = new JsonObject();
            json.addProperty("error", "Transaction ID must be a valid number.");
            response.getWriter().write(new Gson().toJson(json));
            return;
        }

        try (var factory = new DAOFactory()) {
            var transactionDao = factory.getTransactionDAO();
            var userDao = factory.getUserDAO();

            var transaction = transactionDao.findById(id);

            if (transaction == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JsonObject json = new JsonObject();
                json.addProperty("error", "Transaction not found.");
                response.getWriter().write(new Gson().toJson(json));
                return;
            }

            if (!transaction.getPayerId().equals(user.getId())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                JsonObject json = new JsonObject();
                json.addProperty("error", "You are not allowed to access this transaction.");
                response.getWriter().write(new Gson().toJson(json));
                return;
            }

            var receiverEmail = userDao.findById(transaction.getReceiverId()).getEmail();

            var transactionDTO = new TransactionDTO(
                    transaction.getId(),
                    transaction.getPayerId(),
                    transaction.getReceiverId(),
                    transaction.getPrice(),
                    transaction.getDescription(),
                    transaction.getType(),
                    transaction.getCategory(),
                    transaction.getDateTime(),
                    receiverEmail
            );

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(transactionDTO));
        }
    }
}
