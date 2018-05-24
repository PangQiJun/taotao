package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryServiceImpl;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name="id", defaultValue="0") Long parentId) {
		List<EasyUITreeNode> list = contentCategoryServiceImpl.getContentCategoryList(parentId);
		return list;
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult addContentCategory(Long parentId, String name) {
		TaotaoResult contentCategory = contentCategoryServiceImpl.addContentCategory(parentId, name);
		return contentCategory;
	}
	
	@RequestMapping("/content/category/update")
	@ResponseBody
	public void updateContentCategory(Long id, String name) {
		contentCategoryServiceImpl.updateContentCategory(id, name);
	}
	
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public String deleteContentCategory(Long id, Long parentId) {
		contentCategoryServiceImpl.deleteContentCategory(parentId, id);
		return "{}";
	}
}
