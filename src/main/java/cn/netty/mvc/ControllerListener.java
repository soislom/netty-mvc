package cn.netty.mvc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.netty.mvc.factory.ControllerAdvice;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class ControllerListener implements GenericFutureListener<ChannelFuture> {
	
	private static final Map<String, Object> routerMap = new ConcurrentHashMap<String, Object>();

	public void operationComplete(ChannelFuture future) throws Exception {
		ControllerAdvice controllerAdvice = new ControllerAdvice();
		controllerAdvice.arroundAdvice(point);
		
		System.out.println("init system listener...");
	}

}
