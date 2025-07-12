package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		var email = request.getParameter("email");
		var password = request.getParameter("password");

		try (var factory = new DAOFactory()) {
			var userDao = factory.getUserDAO();
			var user = userDao.findByEmail(email);
			var authenticated = User.authenticate(user, email, password);

			if (!authenticated) {
				request.setAttribute("error", "Invalid login");
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}

			var session = request.getSession(true);
			session.setAttribute("user", user);
			response.sendRedirect("controller.do?action=transactions-page");
		}
	}

}
