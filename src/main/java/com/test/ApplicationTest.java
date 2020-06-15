package com.test;

import cn.netty.core.Application;
import cn.netty.core.annotation.Controller;

@Controller
public class ApplicationTest {

	public static void main(String[] args) throws InterruptedException {
		Application.getInstance().run();
	}
	
}
