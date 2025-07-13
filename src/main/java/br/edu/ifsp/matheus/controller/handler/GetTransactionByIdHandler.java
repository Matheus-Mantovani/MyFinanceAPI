package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.GetTransactionByIdCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionByIdHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET") && "transaction".equals(request.getParameter("action"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new GetTransactionByIdCommand().execute(request, response);
	}

}
