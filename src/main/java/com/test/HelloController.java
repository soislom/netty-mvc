package com.test;

import cn.netty.core.Controller;
import cn.netty.core.annotation.Action;
import cn.netty.core.annotation.Url;

@Action()
public class HelloController implements Controller{
	
	@Url(name ="/")
	public Object hello() {
		System.out.println("this is hello world");
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("admin");
		userInfo.setPassword("123456");
		return userInfo;
	}

}
