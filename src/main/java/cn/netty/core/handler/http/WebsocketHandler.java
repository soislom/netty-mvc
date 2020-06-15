package cn.netty.core.handler.http;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebsocketHandler {

	private static final Logger LOGGER = Logger.getLogger(WebsocketHandler.class.getName());

	public static void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

//		 new WebSocketServerHandshakerFactory(frame., subprotocols, allowExtensions)

		// Check for closing frame
		if (frame instanceof CloseWebSocketFrame) {
//			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}

		// Send the uppercase string back.
		String request = ((TextWebSocketFrame) frame).text();
		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine(String.format("%s received %s", ctx.channel(), request));
		}
		ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));
	}
}
