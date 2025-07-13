package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.GetEditTransactionPageCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetEditTransactionPageHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET") && "edit-transaction-page".equals(request.getParameter("action"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new GetEditTransactionPageCommand().execute(request, response);
	}

}
