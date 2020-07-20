package cn.netty.core.factory;

import java.util.Set;
import java.util.logging.Logger;

import org.reflections.Reflections;

import cn.netty.core.annotation.Controller;
import cn.netty.core.exception.AnnotationException;
import cn.netty.core.handler.http.HttpHandler;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public class ApplicationFactory implements GenericFutureListener<ChannelFuture> {

	private static final Logger LOGGER = Logger.getLogger(ApplicationFactory.class.getName());

	public void operationComplete(ChannelFuture future) throws Exception {
		Reflections reflections = new Reflections("com.test");
		Set<Class<?>> actionSet = reflections.getTypesAnnotatedWith(Controller.class);

		for (Class<?> clasz : actionSet) {
			try {
				LOGGER.info("loading " + clasz + " to the system container");
				Controller action = clasz.getAnnotation(Controller.class);
				HttpHandler.routerMap.put(action.value(), clasz);
			} catch (NullPointerException e) {
				throw new AnnotationException(clasz.getName() + " no @antion annotation");
			}
		}
		LOGGER.info("load system container success");
	}

}
