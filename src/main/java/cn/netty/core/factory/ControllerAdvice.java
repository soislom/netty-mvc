package cn.netty.core.factory;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.reflections.Reflections;

import cn.netty.core.annotation.Controller;
import cn.netty.core.annotation.HotSwap;
import cn.netty.core.exception.AnnotationException;
import cn.netty.core.handler.http.HttpHandler;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class ControllerAdvice implements GenericFutureListener<ChannelFuture> {

	private static final Logger LOGGER = Logger.getLogger(ControllerAdvice.class.getName());

	public static final Set<Class<?>> hotClass = new HashSet<>();

	public void operationComplete(ChannelFuture future) throws Exception {
		Reflections reflections = new Reflections("com.test");
		Set<Class<?>> actionSet = reflections.getTypesAnnotatedWith(Controller.class);
		Set<Class<?>> hotswapSet = reflections.getTypesAnnotatedWith(HotSwap.class);

		for (Class<?> clasz : actionSet) {
			try {
				LOGGER.info("loading " + clasz + " to the system container");
				Controller action = clasz.getAnnotation(Controller.class);
				HttpHandler.routerMap.put(action.value(), clasz);
			} catch (NullPointerException e) {
				throw new AnnotationException(clasz.getName() + " no @antion annotation");
			}
		}

		for (Class<?> clasz : hotswapSet) {
			HotSwap hotSwap = clasz.getAnnotation(HotSwap.class);
			if (hotSwap.value()) {
				hotClass.add(clasz);
			}
		}
		LOGGER.info("load system container success");
	}

}
