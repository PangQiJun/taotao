package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkData(@PathVariable(value="param") String param, @PathVariable(value="type") int type) {
		TaotaoResult result = userService.checkDate(param, type);
		return result;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		TaotaoResult result = userService.addUser(user);
		return result;
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletResponse response
			, HttpServletRequest request) {
		TaotaoResult result = userService.login(username, password);
		// 把token写入cookie
		CookieUtils.setCookie(request, response, "TT_TOKEN", result.getData().toString());
		return result;
	}
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public TaotaoResult getUserInfoByToken(@PathVariable(value="token") String token) {
		return userService.getUserByToken(token);
	}
	
	@RequestMapping("user/logout")
	public void logout(String token) {
		userService.logout(token);
	}
	
}
