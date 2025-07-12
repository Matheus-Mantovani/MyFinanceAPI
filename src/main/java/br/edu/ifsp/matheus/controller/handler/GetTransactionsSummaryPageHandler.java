package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.GetTransactionsSummaryPageCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTransactionsSummaryPageHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET") && "summary-page".equals(request.getParameter("action"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new GetTransactionsSummaryPageCommand().execute(request, response);
	}

}
