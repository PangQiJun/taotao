package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.util.JsonUtils;
import com.taotao.util.FastDFSClient;

@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String serverURL;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String picUpload(MultipartFile uploadFile) {
		Map map = new HashMap<>();
		try {
			// 取扩展名
			String origFileName = uploadFile.getOriginalFilename();
			String extName = origFileName.substring(origFileName.lastIndexOf(".") + 1);
			// 上传
			FastDFSClient client = new FastDFSClient("classpath:resources/client.conf");
			String url = client.uploadFile(uploadFile.getBytes(), extName);
			url = serverURL + url;
			map.put("error", 0);
			map.put("url", url);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", 1);
			map.put("message", "图片上传失败" + e.getMessage());
		}
		
		return JsonUtils.objectToJson(map);
	}
}
