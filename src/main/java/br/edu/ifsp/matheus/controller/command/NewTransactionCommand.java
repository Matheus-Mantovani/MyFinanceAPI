package br.edu.ifsp.matheus.controller.command;

import java.time.LocalDateTime;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.Transaction;
import br.edu.ifsp.matheus.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewTransactionCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try(var factory = new DAOFactory()) {
			var userDao = factory.getUserDAO();
			var transactionDao = factory.getTransactionDAO();

			var session = request.getSession(false);
			var payerId = ((User) session.getAttribute("user")).getId();
			var receiverEmail = request.getParameter("receiverEmail");
			var strPrice = request.getParameter("price");
			var description = request.getParameter("description");
			var strType = request.getParameter("type");
			var strCategory = request.getParameter("category");
			
			if(hasEmptyParameters(receiverEmail, strPrice, description, strType, strCategory)) {
				request.setAttribute("error", "All fields must be filled.");
				request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
				return;
			}
			
			if(userDao.findById(payerId).getEmail().equalsIgnoreCase(receiverEmail)) {
				request.setAttribute("error", "You can't make a transaction to your own email.");
				request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
				return;
			}
			
			var receiver = userDao.findByEmail(receiverEmail);
			if(receiver == null) {
				request.setAttribute("error", "Receiver e-mail not found.");
				request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
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
				request.setAttribute("error", "Invalid amount format.");
				request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
				return;
			}
			
			var transaction = new Transaction(payerId, receiverId, price, description, type, category, dateTime);
			if(transactionDao.create(transaction)) {
				factory.commit();
				request.setAttribute("success", "Transaction completed successfully.");
				request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
				return;
			} else {
				factory.rollback();
				request.setAttribute("error", "Failed to complete the transaction. Please try again.");
				request.getRequestDispatcher("new-transaction.jsp").forward(request, response);
			}
		}
	}

	private boolean hasEmptyParameters(String... params) {
		for(String param : params) {
			if(param == null || param.isBlank()) {
				System.out.println(param);
				return true;
			}
		}
		return false;
	}

}
