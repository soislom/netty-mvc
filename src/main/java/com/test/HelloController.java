package com.test;

import cn.netty.core.Controller;
import cn.netty.core.annotation.Action;
import cn.netty.core.annotation.Path;

public class HelloController{
	
	@Path("/")
	public Object hello() {
		System.out.println("this is hello world");
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("admin");
		userInfo.setPassword("123456");
		return userInfo;
	}

}
