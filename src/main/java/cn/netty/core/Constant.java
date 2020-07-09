package cn.netty.core;

public interface Constant {

	public static final boolean SSL = System.getProperty("ssl") != null;
	public static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));

	String DEFAULT_WEBSOCKET_PATH = "/websocket";

}
