package br.edu.ifsp.matheus.controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

import br.edu.ifsp.matheus.model.User;

@WebFilter(urlPatterns = {"/controller.do"})
public class SessionFilter extends HttpFilter implements Filter {
	private final Set<String> protectedActions = Set.of("transactions-page", "new-transaction", "new-transaction-page", "edit-transaction", "edit-transaction-page", "summary-page");
	private static final long serialVersionUID = 1L;

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String action = req.getParameter("action");
		
        boolean authRequired = protectedActions.contains(action);
        
        if(!authRequired) {
    		chain.doFilter(request, response);
    		return;
        }
        
        if(session == null) {
        	res.sendRedirect("controller.do?action=restricted-access-page");
        	return;
        }
        
        User user = (User) session.getAttribute("user");
        if(user == null) {
        	res.sendRedirect("controller.do?action=restricted-access-page");
        	return;
        }
        
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {}
	public void destroy() {}
}
