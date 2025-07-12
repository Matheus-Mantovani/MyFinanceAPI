package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		
		try(var factory = new DAOFactory()) {
			var session = request.getSession(false);
			var user = (User) session.getAttribute("user");
			var transactionDao = factory.getTransactionDAO();
			
			int pageNumber = 1;
			int pageSize = 10;
			int year = 0;
			int month = 0;
			TransactionType type = null;
			TransactionCategory category = null;
			
			if(yearStr != null && !yearStr.isBlank()) {
				try {
					year = Integer.parseInt(yearStr);
				} catch (NumberFormatException e) {
					request.setAttribute("error", "Invalid year format.");
					request.getRequestDispatcher("transactions.jsp").forward(request, response);
					return;
				}
			}
			
			if(monthStr != null && !monthStr.isBlank()) {
				try {
					month = Integer.parseInt(monthStr);
				} catch (NumberFormatException e) {
					request.setAttribute("error", "Invalid month format.");
					request.getRequestDispatcher("transactions.jsp").forward(request, response);
					return;
				}
			}
			
			if(typeStr != null && !typeStr.isBlank()) {
				try {
					type = TransactionType.parseType(typeStr);
				} catch (FormatFlagsConversionMismatchException e) {
					request.setAttribute("error", "Invalid type format.");
					request.getRequestDispatcher("transactions.jsp").forward(request, response);
				}
			}
			
			if(categoryStr != null && !categoryStr.isBlank()) {
				try {
					category = TransactionCategory.parseCategory(categoryStr);
				} catch (FormatFlagsConversionMismatchException e) {
					request.setAttribute("error", "Invalid category format.");
					request.getRequestDispatcher("transactions.jsp").forward(request, response);
				}
			}
			
			if(pageNumberStr != null) {
				pageNumber = Integer.parseInt(pageNumberStr);
			}
			
			if(pageSizeStr != null) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			
			List<Transaction> transactions = transactionDao.findByPayerId(user.getId(), year, month, type, category, pageNumber, pageSize);
			int totalPages = transactionDao.totalPages(user.getId(), year, month, type, category, pageSize);
			request.setAttribute("transactions", transactions);
			request.setAttribute("page", pageNumber);
			request.setAttribute("totalPages", totalPages);
		}
		
		request.getRequestDispatcher("transactions.jsp").forward(request, response);
	}

}
