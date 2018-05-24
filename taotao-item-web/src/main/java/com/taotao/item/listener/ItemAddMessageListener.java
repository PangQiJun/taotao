package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage){
				TextMessage textMessage = (TextMessage)message;
				Long itemId = Long.parseLong(textMessage.getText());
				TbItem tbItem = itemService.getItemById(itemId);
				Item item = new Item(tbItem);
				TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
				// 生成静态页面
				Configuration configuration = freeMarkerConfigurer.getConfiguration();
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("item", item);
				data.put("itemDesc", tbItemDesc);
				Writer out = new FileWriter(new File("D:/html/item" + itemId + ".html"));
				Template template = configuration.getTemplate("item.ftl");
				template.process(data, out);
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
