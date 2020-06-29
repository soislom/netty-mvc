package cn.netty.core.handler.http;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;

import cn.netty.core.Controller;
import cn.netty.core.annotation.Param;
import cn.netty.core.annotation.Path;
import cn.netty.core.annotation.body;
import cn.netty.core.enume.HttpMethod;
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
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

public class HttpHandler {

//	private static final String ALL_REGEX = "[\\d\\D]";

	private static final Logger LOGGER = Logger.getLogger(HttpHandler.class.getName());

	public static final Map<String, Class<? extends Controller>> routerMap = new ConcurrentHashMap<String, Class<? extends Controller>>();

	public static final Map<String, Map<HttpMethod, Method>> urlMappings = new LinkedHashMap<String, Map<HttpMethod, Method>>();

	public static void handler(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_GATEWAY);
		}

		String[] urlParams = request.getUri().toString().split("\\?");

		Object result = "success";
		try {
			Map<String, Object> paramMap = HttpHandler.decoderGet(request);
			switch (request.getMethod().toString()) {
			case "GET":
				result = getMapping(urlParams[0], HttpMethod.GET, paramMap);
				break;
			case "POST":
				Map<String, Object> bodyParamMap = HttpHandler.decoderPost(request);
				result = postMapping(urlParams[0], HttpMethod.POST, paramMap, bodyParamMap);
				break;
			case "PUT":
//				result = urlMapping(urlParams[0], HttpMethod.PUT);
				break;
			case "DELETE":
//				result = urlMapping(urlParams[0], HttpMethod.DELETE);
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

	/**
	 * check url is exist
	 * 
	 * @param uri
	 * @return
	 */
	private static Class<? extends Controller> checkUrl(String uri) {
		Class<? extends Controller> class1 = routerMap.get(uri);
		if (class1 == null) {
			throw new HttpUrlException("uri:" + uri + "不存在");
		}
		return class1;
	}

	/**
	 * handler get request
	 * 
	 * @param uri
	 * @param httpMethod
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private static Object getMapping(String uri, HttpMethod httpMethod, Map<String, Object> params) throws Exception {
		Class<? extends Controller> checkUrl = checkUrl(uri);
		for (Method method : checkUrl.getMethods()) {
			Path url = method.getAnnotation(Path.class);
			String name = uri + url.value();
			if (name.startsWith(uri) && url.method().equals(httpMethod)) {
				Parameter[] parameters = method.getParameters();

				List<Object> methodParamList = new Vector<>();

				for (Parameter parameter : parameters) {
					for (String key : params.keySet()) {
						if (parameter.getAnnotation(Param.class) != null
								&& parameter.getAnnotation(Param.class).value().equals(key)) {
							methodParamList.add(params.get(key));
						}
					}
				}
				Object[] array = methodParamList.toArray();
				Object data = method.invoke(checkUrl.newInstance(), array.length == 0 ? null : array);
				return JacksonUtil.toJson(data);
			}
		}
		throw new Exception("系统异常");
	}

	/**
	 * 
	 * @param method
	 * @param class1
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private static Object postMapping(String uri, HttpMethod httpMethod, Map<String, Object> params,
			Map<String, Object> bodyParams) throws Exception {
		Class<? extends Controller> checkUrl = checkUrl(uri);
		for (Method method : checkUrl.getMethods()) {
			Path url = method.getAnnotation(Path.class);
			String name = uri + url.value();
			if (name.startsWith(uri) && url.method().equals(httpMethod)) {
				Parameter[] parameters = method.getParameters();
				Class<?>[] parameterTypes = method.getParameterTypes();
				List<Object> methodParamList = new Vector<>();

				for (int i = 0; i < parameters.length; i++) {
					for (String key : params.keySet()) {
						if (parameters[i].getAnnotation(Param.class) != null
								&& parameters[i].getAnnotation(Param.class).value().equals(key)) {
							methodParamList.add(params.get(key));
						}
					}
					if (parameters[i].getAnnotation(body.class) != null) {
						Object parseObject = JSON.parseObject(JSON.toJSONString(bodyParams), parameterTypes[i]);
						methodParamList.add(parseObject);
					}
				}
				try {
					Object[] array = methodParamList.toArray();
					Object data = method.invoke(checkUrl.newInstance(), array.length == 0 ? null : array);
					return JacksonUtil.toJson(data);
				} catch (Exception e) {
					throw new Exception("参数不合法");
				}
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

	public static Map<String, Object> decoderGet(FullHttpRequest request) {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		QueryStringDecoder stringDecoder = new QueryStringDecoder(request.getUri());
		for (String key : stringDecoder.parameters().keySet()) {
			parmMap.put(key, stringDecoder.parameters().get(key).get(0));
		}
		return parmMap;
	}

	public static Map<String, Object> decoderPost(FullHttpRequest request) throws IOException {
		Map<String, Object> parmMap = new HashMap<String, Object>();
		HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(request);
		List<InterfaceHttpData> bodyHttpDatas = postRequestDecoder.getBodyHttpDatas();
		for (InterfaceHttpData httpData : bodyHttpDatas) {
			Attribute data = (Attribute) httpData;
			parmMap.put(data.getName(), data.getValue());
		}
		return parmMap;
	}

}
