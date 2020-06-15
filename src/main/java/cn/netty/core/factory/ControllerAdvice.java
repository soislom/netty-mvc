package cn.netty.core.factory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;

import cn.netty.core.Controller;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class ControllerAdvice implements GenericFutureListener<ChannelFuture>{

	private static final Map<String, Class<? extends Controller>> routerMap = new ConcurrentHashMap<String, Class<? extends Controller>>();

	public void operationComplete(ChannelFuture future) throws Exception {
		Reflections reflections = new Reflections("com.test");
		Set<Class<? extends Controller>> subTypesOf = reflections.getSubTypesOf(Controller.class);

		for (Class<? extends Controller> clasz : subTypesOf) {
			routerMap.put(clasz.getName(), clasz);
		}
		System.out.println(routerMap);
		System.out.println("init system listener...");
	}
	
}
