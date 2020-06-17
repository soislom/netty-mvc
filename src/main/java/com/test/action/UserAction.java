package com.test.action;

import cn.netty.core.Controller;
import cn.netty.core.annotation.Action;
import cn.netty.core.annotation.Url;

@Action("/user")
public class UserAction implements Controller{

	@Url()
	public void findById() {
		
	}
	
}
