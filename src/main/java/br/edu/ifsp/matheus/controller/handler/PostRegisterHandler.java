package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.RegisterCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostRegisterHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("POST") && "register".equals(request.getParameter("action"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new RegisterCommand().execute(request, response);
	}

}
