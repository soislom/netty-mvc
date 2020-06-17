package cn.netty.core.factory;

import java.util.Set;

import org.reflections.Reflections;

import cn.netty.core.Controller;
import cn.netty.core.annotation.Action;
import cn.netty.core.handler.http.HttpHandler;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class ControllerAdvice implements GenericFutureListener<ChannelFuture> {

	public void operationComplete(ChannelFuture future) throws Exception {
		Reflections reflections = new Reflections("com.test");
		Set<Class<? extends Controller>> subTypesOf = reflections.getSubTypesOf(Controller.class);

		for (Class<? extends Controller> clasz : subTypesOf) {
			Action action = clasz.getAnnotation(Action.class);
			HttpHandler.routerMap.put(action.value(), clasz);
		}
		System.out.println(HttpHandler.routerMap);
		System.out.println("init system listener...");
	}

}
