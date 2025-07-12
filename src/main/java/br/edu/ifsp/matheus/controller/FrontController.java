package br.edu.ifsp.matheus.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import br.edu.ifsp.matheus.controller.handler.Handler;
import br.edu.ifsp.matheus.controller.handler.HandlerFactory;

@WebServlet("/controller.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Handler chain;

	@Override
	public void init() {
		try {
			chain = HandlerFactory.createChain();
		} catch (Exception e) {
			throw new RuntimeException("Error initializing handler chain", e);
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			chain.handle(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service(request, response);
	}

}
