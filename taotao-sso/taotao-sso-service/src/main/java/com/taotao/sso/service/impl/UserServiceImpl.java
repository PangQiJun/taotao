package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public TaotaoResult checkDate(String param, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		}
		if (type == 2) {
			criteria.andPhoneEqualTo(param);
		}
		if (type == 3) {
			criteria.andEmailEqualTo(param);
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			return TaotaoResult.ok(false);
		}
		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult addUser(TbUser user) {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setId(IDUtils.getItemId());
		String md2Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md2Pass);
		userMapper.insert(user);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		TbUser user = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码不正确");
		}
		user.setPassword(null);
		String token = UUID.randomUUID().toString();
		jedisClient.set("USER_SESSION:" + token, JsonUtils.objectToJson(user));
		jedisClient.expire("USER_SESSION:" + token, 1800);
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get("USER_SESSION:" + token);
		if(StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "用户登录已过期，请重新登录！");
		}
		jedisClient.expire("USER_SESSION:" + token, 1800);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(user);
	}

	@Override
	public void logout(String token) {
		jedisClient.delete(token);
	}

}
