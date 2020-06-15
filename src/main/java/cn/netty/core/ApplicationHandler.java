package cn.netty.core;

import cn.netty.core.handler.http.HttpHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class ApplicationHandler extends SimpleChannelInboundHandler {
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof FullHttpRequest) {
			HttpHandler.handler(ctx, (FullHttpRequest)msg);
		} 
//		else if(msg instanceof WebSocketFrame) {
//			WebsocketHandler.handleWebSocketFrame(ctx, (WebSocketFrame)msg);
//		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}


}
