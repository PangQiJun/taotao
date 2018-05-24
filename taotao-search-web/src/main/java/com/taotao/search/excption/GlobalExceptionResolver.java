package com.taotao.search.excption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver {
	
	private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object handle,
			Exception e) {
		logger.error(e.getMessage(), e);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "系统异常，请稍后再试！");
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
