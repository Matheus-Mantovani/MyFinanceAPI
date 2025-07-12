package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		var name = request.getParameter("name");
		var email = request.getParameter("email");
		var password = request.getParameter("password");

		try (var factory = new DAOFactory()) {
			var userDao = factory.getUserDAO();

			if (userDao.findByEmail(email) != null) {
				request.setAttribute("error", "Email already in use.");
				request.getRequestDispatcher("register.jsp").forward(request, response);
				return;
			}
			
			var user = new User(name, email, password);
			
			if(userDao.create(user)) {
				factory.commit();
				var session = request.getSession(true);
				session.setAttribute("user", user);
			} else {
				factory.rollback();
				request.setAttribute("error", "Failed to complete the registration. Please try again.");
				request.getRequestDispatcher("register.jsp").forward(request, response);
				return;
			}
			
			response.sendRedirect("controller.do?action=transactions-page");
		}
	}

}
