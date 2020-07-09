package com.test.action;

import com.test.UserInfo;

import cn.netty.core.annotation.Controller;
import cn.netty.core.annotation.Param;
import cn.netty.core.annotation.Path;
import cn.netty.core.annotation.Body;
import cn.netty.core.enume.HttpMethod;

@Controller("/user")
public class UserAction {

	@Path()
	public String get(@Param("name") String name, @Param("pwd") String pwd) {
		System.out.println(name + '-' + pwd);
		return "this is GET method";
	}

	@Path(method = HttpMethod.POST)
	public String post(@Param("name") String name, @Body UserInfo userInfo, @Param("pwd") String pwd) {
		System.out.println(name);
		System.out.println(pwd);
		System.out.println(userInfo.toString());
		return "this is POST method";
	}

	@Path(method = HttpMethod.PUT)
	public String put() {
		return "this is PUT method";
	}

	@Path(method = HttpMethod.DELETE)
	public String delete() {
		return "this is DELETE method";
	}
}
