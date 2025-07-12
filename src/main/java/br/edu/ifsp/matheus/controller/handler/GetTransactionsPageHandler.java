package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.GetTransactionsPageCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionsPageHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET") && "transactions-page".equals(request.getParameter("action"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new GetTransactionsPageCommand().execute(request, response);
	}

}
