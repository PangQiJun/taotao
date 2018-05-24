package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	
	@Override
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 查询数据
		TbContentExample example = new TbContentExample();
		if(categoryId != 0) {
			Criteria criteria = example.createCriteria();
			criteria.andCategoryIdEqualTo(categoryId);
		}
		List<TbContent> list = contentMapper.selectByExample(example);
		// 取查询结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	
	@Override
	public TaotaoResult addContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		contentMapper.insert(tbContent);
		try {
			jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult updateContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKey(content);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteContent(Long[] ids) {
		for (Long id : ids) {
			contentMapper.deleteByPrimaryKey(id);
		}
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentByCid(Long cid) {
		// 查询缓存，存在则直接返回数据
		try {
			String json = jedisClient.hget(INDEX_CONTENT, cid + "");
			if(!StringUtils.isBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 放入缓存
		try {
			jedisClient.hset(INDEX_CONTENT, cid + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
