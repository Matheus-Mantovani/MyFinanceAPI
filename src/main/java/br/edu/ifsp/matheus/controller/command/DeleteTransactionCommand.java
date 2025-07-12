package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTransactionCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try(var factory = new DAOFactory()) {
			var session = request.getSession();
			var transactionDao = factory.getTransactionDAO();
			var user = (User) session.getAttribute("user");
			
			var idStr = request.getParameter("id");
			
			if(idStr == null || idStr.isBlank()) {
				session.setAttribute("error", "Invalid transaction ID.");
				response.sendRedirect("controller.do?action=transactions-page");
				return;
			}

			var id = Long.parseLong(idStr);
			var userId = user.getId();
			var transaction = transactionDao.findById(id);
			
			if(userId != transaction.getPayerId()) {
				session.setAttribute("error", "You are not allowed to delete transactions from other users.");
				response.sendRedirect("controller.do?action=transactions-page");
				return;
			}
			
			
			if(transactionDao.delete(transaction)) {
				factory.commit();
				session.setAttribute("success", "Transaction deleted successfully.");
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			} else {
				session.setAttribute("error", "Failed to delete the transaction. Please try again.");
				response.sendRedirect("controller.do?action=transactions-page");
			}
		}
	}
}
