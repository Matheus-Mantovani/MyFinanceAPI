package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.EditTransactionCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PutEditTransactionHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
	    return ("PUT".equalsIgnoreCase(request.getParameter("_method")) || request.getMethod().equalsIgnoreCase("PUT"))
	        && "edit-transaction".equals(request.getParameter("action"));
	}


	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new EditTransactionCommand().execute(request, response);
	}

}
