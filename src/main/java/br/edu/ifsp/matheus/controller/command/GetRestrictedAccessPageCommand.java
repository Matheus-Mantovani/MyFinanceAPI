package br.edu.ifsp.matheus.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetRestrictedAccessPageCommand implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getRequestDispatcher("restricted-access.jsp").forward(request, response);
	}

}
