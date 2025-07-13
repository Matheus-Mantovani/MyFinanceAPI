package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.dto.TransactionDTO;
import br.edu.ifsp.matheus.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import br.edu.ifsp.matheus.util.LocalDateTimeAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public class GetEditTransactionPageCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var idStr = request.getParameter("id");
        boolean isJson = "application/json".equalsIgnoreCase(request.getHeader("Accept"));

        try (var factory = new DAOFactory()) {
            var transactionDao = factory.getTransactionDAO();
            var userDao = factory.getUserDAO();

            if (idStr == null || idStr.isBlank()) {
                if (isJson) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid transaction ID.\"}");
                } else {
                    request.setAttribute("error", "Invalid transaction ID.");
                    request.getRequestDispatcher("transactions.jsp").forward(request, response);
                }
                return;
            }

            var id = Long.parseLong(idStr);
            var auxTransaction = transactionDao.findById(id);

            if (auxTransaction == null) {
                if (isJson) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Transaction not found.\"}");
                } else {
                    request.setAttribute("error", "Transaction not found.");
                    request.getRequestDispatcher("transactions.jsp").forward(request, response);
                }
                return;
            }

            var receiverEmail = userDao.findById(auxTransaction.getReceiverId()).getEmail();

            var transaction = new TransactionDTO(
                    auxTransaction.getId(),
                    auxTransaction.getPayerId(),
                    auxTransaction.getReceiverId(),
                    auxTransaction.getPrice(),
                    auxTransaction.getDescription(),
                    auxTransaction.getType(),
                    auxTransaction.getCategory(),
                    auxTransaction.getDateTime(),
                    receiverEmail
            );

            if (isJson) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();

                response.getWriter().write(gson.toJson(transaction));
            } else {
                request.setAttribute("transaction", transaction);
                request.getRequestDispatcher("edit-transaction.jsp").forward(request, response);
            }
        }
    }
}
