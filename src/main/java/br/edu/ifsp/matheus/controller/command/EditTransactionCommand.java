package br.edu.ifsp.matheus.controller.command;

import java.time.LocalDateTime;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.User;
import br.edu.ifsp.matheus.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EditTransactionCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var session = request.getSession(false);
        var user = (User) session.getAttribute("user");
        boolean isJson = "application/json".equalsIgnoreCase(request.getHeader("Accept"));
        response.setContentType(isJson ? "application/json" : "text/html");

        try (var factory = new DAOFactory()) {
            var userDao = factory.getUserDAO();
            var transactionDao = factory.getTransactionDAO();

            String idStr, receiverEmail, strPrice, description, strType, strCategory;

            if (isJson) {
                var gson = new Gson();
                JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
                idStr = json.get("id").getAsString();
                receiverEmail = json.get("receiverEmail").getAsString();
                strPrice = json.get("price").getAsString();
                description = json.get("description").getAsString();
                strType = json.get("type").getAsString();
                strCategory = json.get("category").getAsString();
            } else {
                idStr = request.getParameter("id");
                receiverEmail = request.getParameter("receiverEmail");
                strPrice = request.getParameter("price");
                description = request.getParameter("description");
                strType = request.getParameter("type");
                strCategory = request.getParameter("category");
            }

            if (hasEmptyParameters(idStr, receiverEmail, strPrice, description, strType, strCategory)) {
                handleError(response, session, isJson, "All fields must be filled.");
                return;
            }

            Long id;
            try {
                id = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                handleError(response, session, isJson, "Invalid transaction ID.");
                return;
            }

            var transaction = transactionDao.findById(id);
            if (transaction == null || !transaction.getPayerId().equals(user.getId())) {
                handleError(response, session, isJson, "Unauthorized operation.");
                return;
            }

            if (user.getEmail().equalsIgnoreCase(receiverEmail)) {
                handleError(response, session, isJson, "You can't make a transaction to your own email.");
                return;
            }

            var receiver = userDao.findByEmail(receiverEmail);
            if (receiver == null) {
                handleError(response, session, isJson, "Receiver e-mail not found.");
                return;
            }

            TransactionType type;
            TransactionCategory category;
            double price;

            try {
                type = TransactionType.parseType(strType);
                category = TransactionCategory.parseCategory(strCategory);
                price = Double.parseDouble(strPrice);
            } catch (Exception e) {
                handleError(response, session, isJson, "Invalid input format.");
                return;
            }

            transaction.setReceiverId(receiver.getId());
            transaction.setPrice(price);
            transaction.setDescription(description);
            transaction.setType(type);
            transaction.setCategory(category);
            transaction.setDateTime(LocalDateTime.now());

            if (transactionDao.update(transaction)) {
                factory.commit();
                if (isJson) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    var gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                            .create();
                    JsonObject successJson = new JsonObject();
                    successJson.addProperty("message", "Transaction updated successfully.");
                    response.getWriter().write(gson.toJson(successJson));
                } else {
                    session.setAttribute("success", "Transaction updated successfully.");
                    response.sendRedirect("controller.do?action=transactions-page");
                }
            } else {
                factory.rollback();
                handleError(response, session, isJson, "Failed to update the transaction.");
            }
        }
    }

    private boolean hasEmptyParameters(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) return true;
        }
        return false;
    }

    private void handleError(HttpServletResponse response, HttpSession session,
                             boolean isJson, String message) throws Exception {
        if (isJson) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject json = new JsonObject();
            json.addProperty("error", message);
            response.getWriter().write(json.toString());
        } else {
            session.setAttribute("error", message);
            response.sendRedirect("controller.do?action=transactions-page");
        }
    }
}
