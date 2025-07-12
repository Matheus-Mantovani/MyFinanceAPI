package br.edu.ifsp.matheus.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		var session = request.getSession(false);
		
		if(session != null) {
			session.invalidate();
		}

		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
