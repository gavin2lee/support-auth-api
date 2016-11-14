package com.lachesis.support.auth.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lachesis.support.auth.api.vo.AuthResponse;

@RestController
@RequestMapping("/")
public class AuthCenterController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthCenterController.class);
	
	@RequestMapping("")
	public AuthResponse requestToken(){
		return null;
	}
	
	@RequestMapping("")
	public AuthResponse verifyToken(String token,String ip){
		return null;
	}
}
