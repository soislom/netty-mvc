package cn.netty.core.handler.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.test.UserInfo;

import cn.netty.core.Controller;
import cn.netty.core.annotation.Url;
import cn.netty.core.exception.HttpMethodException;
import cn.netty.core.exception.HttpUrlException;
import cn.netty.core.util.JacksonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

public class HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(HttpHandler.class.getName());

	public static final Map<String, Class<? extends Controller>> routerMap = new ConcurrentHashMap<String, Class<? extends Controller>>();

	public static void handler(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_GATEWAY);
		}

		String[] urlParams = request.getUri().toString().split("\\?");
		Object result = null;
		try {
			switch (request.getMethod().toString()) {
			case "GET":
				Map<String, String> map = HttpHandler.decoderGet(request);
				result = urlMapping(urlParams[0]);
				break;
			case "POST":
				Map<String, String> decoderPost = HttpHandler.decoderPost(request);
				result = urlMapping(urlParams[0]);
				break;
			case "PUT":

				break;
			case "DELETE":

				break;
			case "OPTIONS":

				break;
			default:
				throw new HttpMethodException("暂不支持" + request.getMethod().toString() + "请求");
			}
			success(ctx, result);
		} catch (HttpUrlException e) {
			LOGGER.info(e.getMessage());
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
		}
	}

	private static Object urlMapping(String uri) throws Exception {
		Class<? extends Controller> class1 = routerMap.get(uri);
		if (class1 == null) {
			throw new HttpUrlException(uri + "不存在");
		}

		for (Method method : class1.getMethods()) {
			Url url = method.getAnnotation(Url.class);
			if (url.name().startsWith(uri)) {
				Object data = method.invoke(class1.newInstance(), null);
				return JacksonUtil.toJson(data);
			}
		}
		throw new Exception("系统异常");
	}

	private static void success(ChannelHandlerContext ctx, Object data) {
		success(ctx, HttpResponseStatus.OK, data);
	}

	private static void success(ChannelHandlerContext ctx, HttpResponseStatus status, Object data) {
		ByteBuf byteBuf = Unpooled.copiedBuffer(data.toString(), CharsetUtil.UTF_8);

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
		response.content().writeBytes(byteBuf);
		byteBuf.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set("content_type", "application/json; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static Map<String, String> decoderGet(FullHttpRequest request) {
		Map<String, String> parmMap = new HashMap<String, String>();
		QueryStringDecoder stringDecoder = new QueryStringDecoder(request.getUri());
		for (String key : stringDecoder.parameters().keySet()) {
			parmMap.put(key, stringDecoder.parameters().get(key).get(0));
		}
		return parmMap;
	}

	public static Map<String, String> decoderPost(FullHttpRequest request) throws IOException {
		Map<String, String> parmMap = new HashMap<String, String>();
		HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(request);
		List<InterfaceHttpData> bodyHttpDatas = postRequestDecoder.getBodyHttpDatas();
		for (InterfaceHttpData httpData : bodyHttpDatas) {
			Attribute data = (Attribute) httpData;
			parmMap.put(data.getName(), data.getValue());
		}
		return parmMap;
	}

}
