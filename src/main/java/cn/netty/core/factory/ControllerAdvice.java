package cn.netty.core.factory;

import java.util.Set;

import org.reflections.Reflections;

import cn.netty.core.Controller;
import cn.netty.core.annotation.Action;
import cn.netty.core.exception.AnnotationException;
import cn.netty.core.handler.http.HttpHandler;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class ControllerAdvice implements GenericFutureListener<ChannelFuture> {

	public void operationComplete(ChannelFuture future) throws Exception {
		Reflections reflections = new Reflections("com.test");
		Set<Class<? extends Controller>> subTypesOf = reflections.getSubTypesOf(Controller.class);

		for (Class<? extends Controller> clasz : subTypesOf) {
			try {
				Action action = clasz.getAnnotation(Action.class);
				HttpHandler.routerMap.put(action.value(), clasz);
			} catch (NullPointerException e) {
				throw new AnnotationException(clasz.getName() + " no @antion annotation");
			}
		}
		System.out.println(HttpHandler.routerMap);
		System.out.println("init system listener...");
	}

}
