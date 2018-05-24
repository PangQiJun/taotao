package com.taotao.test;

import com.taotao.util.FastDFSClient;

public class Test1 {

	public static void main(String[] args) throws Exception {
		FastDFSClient client = new FastDFSClient("classpath:resources/client.conf");
		//String string = client.uploadFile("C:/Users/pangqijun/Desktop/新建文件夹/111.jpg");
		//System.out.println(string);
	}
}
