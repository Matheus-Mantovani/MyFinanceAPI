package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionsSummaryPageCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try(var factory = new DAOFactory()) {
			var session = request.getSession(false);
			var user = (User) session.getAttribute("user");
			var transactionDao = factory.getTransactionDAO();
			
			var totalIncome = transactionDao.totalIncome(user.getId());
			var totalExpenses = transactionDao.totalExpense(user.getId());
			var currentBalance = totalIncome - totalExpenses;
			var expensesByCategory = transactionDao.expensesByCategory(user.getId());
			var incomeByCategory = transactionDao.incomeByCategory(user.getId());
			
			request.setAttribute("totalIncome", totalIncome);
			request.setAttribute("totalExpenses", totalExpenses);
			request.setAttribute("currentBalance", currentBalance);
			request.setAttribute("expensesByCategory", expensesByCategory);
			request.setAttribute("incomeByCategory", incomeByCategory);
		}
		
		request.getRequestDispatcher("transactions-summary.jsp").forward(request, response);
	}

}
