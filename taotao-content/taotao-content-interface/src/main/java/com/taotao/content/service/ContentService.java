package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows);
	
	public TaotaoResult addContent(TbContent tbContent);
	
	public TaotaoResult updateContent(TbContent content);
	
	public TaotaoResult deleteContent(Long[] ids);
	
	public List<TbContent> getContentByCid(Long cid);
}
