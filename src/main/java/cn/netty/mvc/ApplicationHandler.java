package cn.netty.mvc;

import com.test.UserInfo;

import cn.netty.mvc.factory.ResponseFactory;
import cn.netty.mvc.util.JacksonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class ApplicationHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		
		UserInfo userInfo = new UserInfo(1, "admin", "123456");
		String json = JacksonUtil.toJson(userInfo);
		ByteBuf buffer = ctx.alloc().buffer(json.length());
		buffer.writeCharSequence(json, CharsetUtil.UTF_8);
		
		DefaultFullHttpResponse response = ResponseFactory.getInstance(buffer);
		ResponseFactory.initResponseHeaders(response);
		
		sendResponse(ctx, response);
	}
	
	private void sendResponse(ChannelHandlerContext ctx, HttpResponse response) {
		ChannelFuture future = ctx.writeAndFlush(response);
		future.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		ChannelFuture future = ctx.writeAndFlush(response);
		future.addListener(ChannelFutureListener.CLOSE);
	}
	
}
