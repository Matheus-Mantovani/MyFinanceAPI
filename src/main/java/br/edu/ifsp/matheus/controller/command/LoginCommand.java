package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String view = null;
		var email = request.getParameter("email");
		var password = request.getParameter("password");
		
		try(var factory = new DAOFactory()) {
			var userDao = factory.getUserDAO();			
			var user = userDao.findByEmail(email);
			var authenticated = User.authenticate(user, email, password);
			
			if(authenticated) {
				var session = request.getSession(false);
				session.setAttribute("user", user);
				view = "controller.do?action=transactions-page";
			} else {
				request.setAttribute("error", "Invalid login");
				view = "controller.do?action=login-page";
			}
		}
		
		return view;
	}

}
