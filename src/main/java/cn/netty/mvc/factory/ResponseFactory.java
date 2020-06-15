package cn.netty.mvc.factory;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class ResponseFactory {

	public static DefaultFullHttpResponse getInstance() {
		return getInstance(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, null);
	}
	
	public static DefaultFullHttpResponse getInstance(ByteBuf buffer) {
		return getInstance(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
	}

	public static DefaultFullHttpResponse getInstance(HttpVersion httpVersion, HttpResponseStatus status) {
		return getInstance(httpVersion, status, null);
	}

	public static DefaultFullHttpResponse getInstance(HttpVersion httpVersion, HttpResponseStatus status, ByteBuf buffer) {
		return new DefaultFullHttpResponse(httpVersion, status, buffer);
	}
	
	public static void initResponseHeaders(HttpResponse response) {
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
		response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
		response.headers().set(HttpHeaderNames.CONTENT_LANGUAGE, "zh-cn");
		response.headers().set(HttpHeaderNames.DATE, new Date());
		response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST,DELETE, PUT, OPTIONS");
	}
	
	
}
