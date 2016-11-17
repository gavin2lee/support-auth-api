package com.lachesis.support.auth.api.handler;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.lachesis.support.auth.api.exception.AuthenticationException;

public class LocalResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		response.setContentType("application/json; charset=utf-8");
		try {
			if (ex instanceof AuthenticationException) {
				AuthenticationException authEx = (AuthenticationException) ex;
				
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				PrintWriter writer = response.getWriter();
				writer.write(convertToJson(authEx));
				writer.close();
			}else{
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				PrintWriter writer = response.getWriter();
				writer.write(String.format("{\"error\":\"%s\"}", ex.getMessage()));
				writer.close();
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
