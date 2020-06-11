package com.test;

import cn.netty.mvc.Application;
import cn.netty.mvc.annotation.Controller;

@Controller
public class ApplicationTest{

	public static void main(String[] args) throws InterruptedException {
		Application.getInstance().run();
	}
	
}
