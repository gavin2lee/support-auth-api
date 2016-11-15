package com.lachesis.support.auth.api.exception;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public class LocalResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		try {
			if (ex instanceof AuthenticationException) {
				AuthenticationException authEx = (AuthenticationException) ex;
				response.setContentType("application/json; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				PrintWriter writer = response.getWriter();
				writer.write(convertToJson(authEx));
				writer.close();
				// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, );
			}
		} catch (Exception handlerEx) {

		}

		return new ModelAndView();
	}

	private String convertToJson(AuthenticationException ex) {
		String template = "{\"subcode\":\"%s\",\"submsg\":\"%s\"}";
		String ret = String.format(template, ex.getCode(), ex.getMessage());
		return ret;
	}

}
