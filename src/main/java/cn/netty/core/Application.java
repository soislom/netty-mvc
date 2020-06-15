package cn.netty.core;

import cn.netty.core.factory.ControllerAdvice;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public abstract class Application {

	private static Application instance = new Application() {
	};

	private Application() {
		new ControllerAdvice();
	}

	public static Application getInstance() {
		return instance;
	}

	public void run() throws InterruptedException {
		run(Constant.DEFAULT_PORT);
	}

	public void run(int port) throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = serverBootstrap(boss, work);
			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			channelFuture.addListener(new ControllerAdvice());
			Channel channel = channelFuture.channel();
			System.out.println("Open you brower and navigate to http://127.0.0.1:" + port + "/");
			channel.closeFuture().sync();
		} finally {
			close(boss, work);
		}
	}

	protected ServerBootstrap serverBootstrap(EventLoopGroup boss, EventLoopGroup work) {
		ServerBootstrap b = new ServerBootstrap();
		b.group(boss, work).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(channelInitializer());
		return b;
	}

	protected ChannelInitializer<SocketChannel> channelInitializer() {
		ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new HttpServerCodec());
				pipeline.addLast(new HttpObjectAggregator(65536));
				pipeline.addLast(new ChunkedWriteHandler());
				pipeline.addLast(new ApplicationHandler());
			}
		};
		return channelInitializer;
	}

	private void close(EventLoopGroup... eventLoopGroups) {
		if (eventLoopGroups.length == 0)
			throw new NullPointerException();
		for (EventLoopGroup eventLoopGroup : eventLoopGroups) {
			eventLoopGroup.shutdownGracefully();
		}
	}

}
