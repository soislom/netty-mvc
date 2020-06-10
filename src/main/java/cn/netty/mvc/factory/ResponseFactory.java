package cn.netty.mvc.factory;

import io.netty.handler.codec.http.HttpResponse;

public interface ResponseFactory {
	
	HttpResponse getInstance();
	
	HttpResponse setHeader(String key, Object value);
	
	
}
