package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String queryStr, int page, int rows) throws Exception {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		if (page < 1) page = 1;
		query.setStart((page - 1) * rows);
		if (rows < 10) rows = 10;
		query.setRows(rows);
		// 设置默认搜索域
		query.set("df", "item_title");
		// 设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<font color='red'>");
		query.setHighlightSimplePost("</font>");
		// 执行查询
		SearchResult searchResult = searchDao.search(query);
		long recordCount = searchResult.getRecordCount();
		long pages = recordCount / rows;
		if((recordCount % rows) > 0) {
			pages++;
		}
		searchResult.setTotalPages(pages);
		return searchResult;
	}

}
