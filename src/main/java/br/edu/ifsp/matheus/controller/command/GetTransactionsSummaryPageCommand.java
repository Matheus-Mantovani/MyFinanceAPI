package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionsSummaryPageCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try (var factory = new DAOFactory()) {
            var session = request.getSession(false);
            var user = (User) session.getAttribute("user");

            if (user == null) {
                if ("application/json".equals(request.getHeader("Accept"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    var json = new JsonObject();
                    json.addProperty("error", "Unauthorized: User not logged in.");
                    response.getWriter().write(new Gson().toJson(json));
                } else {
                    request.setAttribute("error", "You must be logged in to view this page.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
                return;
            }

            var transactionDao = factory.getTransactionDAO();

            var totalIncome = transactionDao.totalIncome(user.getId());
            var totalExpenses = transactionDao.totalExpense(user.getId());
            var currentBalance = totalIncome - totalExpenses;
            var expensesByCategory = transactionDao.expensesByCategory(user.getId());
            var incomeByCategory = transactionDao.incomeByCategory(user.getId());

            if ("application/json".equals(request.getHeader("Accept"))) {
                response.setContentType("application/json");
                var gson = new Gson();

                var json = new JsonObject();
                json.addProperty("totalIncome", totalIncome);
                json.addProperty("totalExpenses", totalExpenses);
                json.addProperty("currentBalance", currentBalance);
                json.add("expensesByCategory", gson.toJsonTree(expensesByCategory));
                json.add("incomeByCategory", gson.toJsonTree(incomeByCategory));

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(json));
                return;
            }

            request.setAttribute("totalIncome", totalIncome);
            request.setAttribute("totalExpenses", totalExpenses);
            request.setAttribute("currentBalance", currentBalance);
            request.setAttribute("expensesByCategory", expensesByCategory);
            request.setAttribute("incomeByCategory", incomeByCategory);
            request.getRequestDispatcher("transactions-summary.jsp").forward(request, response);
        }
    }
}
