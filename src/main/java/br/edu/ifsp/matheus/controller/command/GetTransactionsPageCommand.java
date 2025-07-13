package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.*;
import br.edu.ifsp.matheus.util.LocalDateTimeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject; 
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

public class GetTransactionsPageCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var yearStr = request.getParameter("year");
        var monthStr = request.getParameter("month");
        var typeStr = request.getParameter("type");
        var categoryStr = request.getParameter("category");
        String pageNumberStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("size");
        
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
                    request.setAttribute("error", "Login required.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
                return;
            }

            var transactionDao = factory.getTransactionDAO();

            int pageNumber = 1;
            int pageSize = 10;
            int year = 0;
            int month = 0;
            TransactionType type = null;
            TransactionCategory category = null;

            if (yearStr != null && !yearStr.isBlank()) {
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    if ("application/json".equals(request.getHeader("Accept"))) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Invalid year format.\"}");
                        return;
                    }
                    request.setAttribute("error", "Invalid year format.");
                    request.getRequestDispatcher("transactions.jsp").forward(request, response);
                    return;
                }
            }

            if (monthStr != null && !monthStr.isBlank()) {
                try {
                    month = Integer.parseInt(monthStr);
                } catch (NumberFormatException e) {
                    if ("application/json".equals(request.getHeader("Accept"))) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Invalid month format.\"}");
                        return;
                    }
                    request.setAttribute("error", "Invalid month format.");
                    request.getRequestDispatcher("transactions.jsp").forward(request, response);
                    return;
                }
            }

            if (typeStr != null && !typeStr.isBlank()) {
                try {
                    type = TransactionType.parseType(typeStr);
                } catch (FormatFlagsConversionMismatchException e) {
                    if ("application/json".equals(request.getHeader("Accept"))) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Invalid type format.\"}");
                        return;
                    }
                    request.setAttribute("error", "Invalid type format.");
                    request.getRequestDispatcher("transactions.jsp").forward(request, response);
                    return;
                }
            }

            if (categoryStr != null && !categoryStr.isBlank()) {
                try {
                    category = TransactionCategory.parseCategory(categoryStr);
                } catch (FormatFlagsConversionMismatchException e) {
                    if ("application/json".equals(request.getHeader("Accept"))) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Invalid category format.\"}");
                        return;
                    }
                    request.setAttribute("error", "Invalid category format.");
                    request.getRequestDispatcher("transactions.jsp").forward(request, response);
                    return;
                }
            }

            if (pageNumberStr != null) {
                pageNumber = Integer.parseInt(pageNumberStr);
            }

            if (pageSizeStr != null) {
                pageSize = Integer.parseInt(pageSizeStr);
            }

            List<Transaction> transactions = transactionDao.findByPayerId(user.getId(), year, month, type, category, pageNumber, pageSize);
            int totalPages = transactionDao.totalPages(user.getId(), year, month, type, category, pageSize);

            if ("application/json".equals(request.getHeader("Accept"))) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");

                var gson = new GsonBuilder()
                	    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                	    .create();
                var json = new JsonObject();
                json.add("transactions", gson.toJsonTree(transactions));
                json.addProperty("currentPage", pageNumber);
                json.addProperty("totalPages", totalPages);

                response.getWriter().write(gson.toJson(json));
                return;
            }

            request.setAttribute("transactions", transactions);
            request.setAttribute("page", pageNumber);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("transactions.jsp").forward(request, response);
        }
    }
}
