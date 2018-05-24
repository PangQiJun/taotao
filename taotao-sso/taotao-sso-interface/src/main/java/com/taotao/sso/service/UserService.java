package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	TaotaoResult checkDate(String param, int type);
	TaotaoResult addUser(TbUser user);
	TaotaoResult login(String username, String password);
	TaotaoResult getUserByToken(String token);
	void logout(String token);
}
