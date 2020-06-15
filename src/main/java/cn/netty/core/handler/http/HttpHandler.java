package cn.netty.core.handler.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.UserInfo;

import cn.netty.core.exception.HttpMethodException;
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

	public static void handler(ChannelHandlerContext ctx, FullHttpRequest request)
			throws HttpMethodException, IOException {
		if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_GATEWAY);
		}

		switch (request.getMethod().toString()) {
		case "GET":
			Map<String, String> map = HttpHandler.decoderGet(request);
			System.out.println(map);
			break;
		case "POST":
			Map<String, String> decoderPost = HttpHandler.decoderPost(request);
			System.out.println(decoderPost);
			break;
		case "PUT":

			break;
		case "DELETE":

			break;
		case "OPTIONS":

			break;
		default:
			throw new HttpMethodException("暂不支持" + request.getMethod().toString() + "方法...");
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		userInfo.setName("admin");
		userInfo.setPassword("123456");

		String json = JacksonUtil.toJson(userInfo);

		ByteBuf byteBuf = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
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
