package br.edu.ifsp.matheus.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {
	void setNext(Handler next);
	void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
