package br.edu.ifsp.matheus.controller.command;

import java.time.LocalDateTime;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.Transaction;
import br.edu.ifsp.matheus.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewTransactionCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        var gson = new Gson();
        boolean isApi = "application/json".equalsIgnoreCase(request.getHeader("Accept"));

        try (var factory = new DAOFactory()) {
            var userDao = factory.getUserDAO();
            var transactionDao = factory.getTransactionDAO();

            var session = request.getSession(false);
            var user = (User) (session != null ? session.getAttribute("user") : null);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                if (isApi) {
                    JsonObject errorJson = new JsonObject();
                    errorJson.addProperty("error", "Unauthorized: user not logged in.");
                    response.getWriter().write(gson.toJson(errorJson));
                } else {
                    request.setAttribute("error", "You must be logged in.");
                    request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
                }
                return;
            }

            String receiverEmail = request.getParameter("receiverEmail");
            String strPrice = request.getParameter("price");
            String description = request.getParameter("description");
            String strType = request.getParameter("type");
            String strCategory = request.getParameter("category");

            if (hasEmptyParameters(receiverEmail, strPrice, description, strType, strCategory)) {
                try {
                    JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);
                    if (json != null) {
                        if (json.has("receiverEmail")) receiverEmail = json.get("receiverEmail").getAsString();
                        if (json.has("price")) strPrice = json.get("price").getAsString();
                        if (json.has("description")) description = json.get("description").getAsString();
                        if (json.has("type")) strType = json.get("type").getAsString();
                        if (json.has("category")) strCategory = json.get("category").getAsString();
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }

            if (hasEmptyParameters(receiverEmail, strPrice, description, strType, strCategory)) {
                if (isApi) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    JsonObject errorJson = new JsonObject();
                    errorJson.addProperty("error", "All fields must be filled.");
                    response.getWriter().write(gson.toJson(errorJson));
                } else {
                    request.setAttribute("error", "All fields must be filled.");
                    request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
                }
                return;
            }

            var payerId = user.getId();
            if (userDao.findById(payerId).getEmail().equalsIgnoreCase(receiverEmail)) {
                if (isApi) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    JsonObject errorJson = new JsonObject();
                    errorJson.addProperty("error", "You can't make a transaction to your own email.");
                    response.getWriter().write(gson.toJson(errorJson));
                } else {
                    request.setAttribute("error", "You can't make a transaction to your own email.");
                    request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
                }
                return;
            }

            var receiver = userDao.findByEmail(receiverEmail);
            if (receiver == null) {
                if (isApi) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject errorJson = new JsonObject();
                    errorJson.addProperty("error", "Receiver e-mail not found.");
                    response.getWriter().write(gson.toJson(errorJson));
                } else {
                    request.setAttribute("error", "Receiver e-mail not found.");
                    request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
                }
                return;
            }

            var receiverId = receiver.getId();
            var type = TransactionType.parseType(strType);
            var category = TransactionCategory.parseCategory(strCategory);
            var dateTime = LocalDateTime.now();
            double price;

            try {
                price = Double.parseDouble(strPrice);
            } catch (NumberFormatException e) {
                if (isApi) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    JsonObject errorJson = new JsonObject();
                    errorJson.addProperty("error", "Invalid amount format.");
                    response.getWriter().write(gson.toJson(errorJson));
                } else {
                    request.setAttribute("error", "Invalid amount format.");
                    request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
                }
                return;
            }

            var transaction = new Transaction(payerId, receiverId, price, description, type, category, dateTime);
            if (transactionDao.create(transaction)) {
                factory.commit();
                if (isApi) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    JsonObject successJson = new JsonObject();
                    successJson.addProperty("message", "Transaction completed successfully.");
                    successJson.addProperty("transactionId", transaction.getId());
                    response.getWriter().write(gson.toJson(successJson));
                } else {
                    request.setAttribute("success", "Transaction completed successfully.");
                    request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
                }
            } else {
                factory.rollback();
                if (isApi) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObject errorJson = new JsonObject();
                    errorJson.addProperty("error", "Failed to complete the transaction. Please try again.");
                    response.getWriter().write(gson.toJson(errorJson));
                } else {
                    request.setAttribute("error", "Failed to complete the transaction. Please try again.");
                    request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
                }
            }
        }
    }

    private boolean hasEmptyParameters(String... params) {
        for (String param : params) {
            if (param == null || param.isBlank()) return true;
        }
        return false;
    }
}
