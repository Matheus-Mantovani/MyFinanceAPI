package br.edu.ifsp.matheus.controller.handler;

import br.edu.ifsp.matheus.controller.command.GetRestrictedAccessPageCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetRestrictedAccessPageHandler extends AbstractHandler {

	@Override
	protected boolean canHandle(HttpServletRequest request) {
		return request.getMethod().equals("GET") && "restricted-access-page".equals(request.getParameter("action"));
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new GetRestrictedAccessPageCommand().execute(request, response);
	}

}
