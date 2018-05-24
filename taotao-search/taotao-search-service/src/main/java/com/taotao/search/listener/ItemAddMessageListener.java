package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.search.service.SearchItemService;

public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private SearchItemService searchItemService;
	
	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				Long itemId = Long.parseLong(textMessage.getText());
				// 等待事务提交
				Thread.sleep(1000);
				searchItemService.addDocument(itemId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
