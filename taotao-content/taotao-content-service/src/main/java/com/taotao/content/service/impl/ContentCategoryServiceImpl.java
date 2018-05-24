package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		List<EasyUITreeNode> nodes = new ArrayList<>();
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			node.setText(tbContentCategory.getName());
			nodes.add(node);
		}
		return nodes;
	}

	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		// 设置状态 :1 (正常) 2(删除)
		contentCategory.setSortOrder(1);
		// 排序，默认为1
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(contentCategory);
		
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public void updateContentCategory(long id, String name) {
		TbContentCategory record = new TbContentCategory();
		record.setId(id);
		record.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public void deleteContentCategory(long parentId, long id) {
		contentCategoryMapper.deleteByPrimaryKey(id);
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setId(parentId);
		contentCategory.setIsParent(false);
		if(list.isEmpty()){
			contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		}
	}

}
