package com.lachesis.support.auth.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lachesis.support.auth.api.common.AuthBizErrorCodes;
import com.lachesis.support.auth.api.exception.AuthenticationException;
import com.lachesis.support.auth.api.vo.AuthResponse;
import com.lachesis.support.auth.api.vo.TokenResponse;
import com.lachesis.support.auth.api.vo.UserDetailsResponse;
import com.lachesis.support.auth.service.CentralizedAuthSupporter;
import com.lachesis.support.auth.vo.UserDetails;

@RestController
@RequestMapping("/")
public class AuthCenterController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthCenterController.class);
	
	@Autowired
	private CentralizedAuthSupporter authSupporter;
	
	@RequestMapping("token")
	public AuthResponse requestToken(String username, String password, HttpServletRequest request){
		if(LOG.isInfoEnabled()){
			LOG.info(String.format("request token for [username:%s]", username));
		}
		
		if(isBlank(username) || isBlank(password)){
			LOG.error(String.format("errors with [username:%s]", username));
			throw new AuthenticationException(AuthBizErrorCodes.AUTH_FAILED_ARGS, "用户名或密码为空");
		}
		
		String userid = username;
		String psword = password;
		String ip = determineTerminalIpAddress(request);
		
		String token = authSupporter.generateToken(userid, psword, ip);
		if(isBlank(token)){
			LOG.error(String.format("authenticating failed for [userid:%s, ip:%s]", userid, ip));
			throw new AuthenticationException(AuthBizErrorCodes.AUTH_FAILED, "认证错误");
		}
		
		UserDetails userDetails = authSupporter.authenticate(token, ip);
		if(userDetails == null){
			LOG.error("cannot get userdetails with token:"+token);
			throw new AuthenticationException(AuthBizErrorCodes.AUTH_FAILED, "认证失败");
		}
		
		String resourceId = userDetails.getResourceId();
		
		TokenResponse tokenResp = new TokenResponse();
		tokenResp.setToken(token);
		tokenResp.setUserId(resourceId);
		return tokenResp;
	}
	
	@RequestMapping("authentication")
	public AuthResponse verifyToken(String token,String ip){
		if(LOG.isInfoEnabled()){
			LOG.info(String.format("authentication for [token:%s,ip:%s]", token, ip));
		}
		
		if(isBlank(token) || isBlank(ip)){
			LOG.error(String.format("errors with [token:%s, ip:%s]", token, ip));
			throw new AuthenticationException(AuthBizErrorCodes.AUTH_FAILED_ARGS, "token或IP为空");
		}
		
		UserDetails userDetails = authSupporter.authenticate(token, ip);
		if(userDetails == null){
			LOG.error(String.format("authentication failed with [token:%s, ip:%s]", token, ip));
			throw new AuthenticationException(AuthBizErrorCodes.AUTH_FAILED_TOKEN, "无效token");
		}
		
		UserDetailsResponse resp = new UserDetailsResponse();
		resp.setUserId(userDetails.getResourceId());
		resp.setUsername(userDetails.getUserid());
		return resp;
	}
	
	private String determineTerminalIpAddress(HttpServletRequest request){
		String ip = request.getRemoteAddr();
		return ip;
	}
	
	private boolean isBlank(String s){
		return StringUtils.isBlank(s);
	}
}
