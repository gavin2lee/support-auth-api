package com.lachesis.support.auth.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lachesis.support.auth.api.vo.AuthResponse;

@RestController
@RequestMapping("/")
public class AuthCenterController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthCenterController.class);
	
	@RequestMapping("token")
	public AuthResponse requestToken(String username, String password, HttpServletRequest request){
		if(LOG.isInfoEnabled()){
			LOG.info(String.format("request token for [username:%s]", username));
		}
		
		if(isBlank(username) || isBlank(password)){
			
		}
		return null;
	}
	
	@RequestMapping("")
	public AuthResponse verifyToken(String token,String ip){
		return null;
	}
	
	private boolean isBlank(String s){
		return StringUtils.isBlank(s);
	}
}
