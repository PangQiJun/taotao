package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {

	public TaotaoResult importItemToIndex();
	public void addDocument(long itemId) throws Exception;
}
