package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.NewTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PostNewTransactionHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("POST") && "new-transaction".equals(request.getParameter("action"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new NewTransactionCommand().execute(request, response);
	}

}
