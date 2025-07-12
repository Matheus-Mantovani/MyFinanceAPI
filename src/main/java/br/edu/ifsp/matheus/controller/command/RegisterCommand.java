package br.edu.ifsp.matheus.controller.command;

import br.edu.ifsp.matheus.dao.DAOFactory;
import br.edu.ifsp.matheus.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String view = null;
		var name = request.getParameter("name");
		var email = request.getParameter("email");
		var password = request.getParameter("password");
		
		try(var factory = new DAOFactory()) {
			var userDao = factory.getUserDAO();
			
			if(userDao.findByEmail(email) != null) {
				request.setAttribute("error", "Email already in use.");
				view = "controller.do?action=register-page";
			} else {
				var user = new User(name, email, password);
				userDao.create(user);
				view = "controller.do?action=transactions-page";
			}
		}
		
		return view;
	}

}
