package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {

	@Value("${SEARCH_RESULT_ROWS}")
	private Integer rows;
	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryStr, @RequestParam(defaultValue="1") Integer page, Model model) throws Exception {
		int a = 1/0;
		queryStr = new String(queryStr.getBytes("iso8859-1"), "utf-8");
		SearchResult searchResult = searchService.search(queryStr, page, rows);
		model.addAttribute("query", queryStr);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);
		return "search";
	}
}
