package cn.netty.core.devtool;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * hot devloyment
 * 
 * @author 34305
 *
 */
public class DevTool extends ClassLoader {

	private static final ScheduledExecutorService SCHEDULEDEXECUTORSERVICE = Executors.newScheduledThreadPool(1);

	public DevTool() {
		System.out.println("load netty devtool");
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("init DevTool findClass method");
		InputStream resourceAsStream = null;

		try {
			String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
			resourceAsStream = this.getClass().getResourceAsStream(fileName);
			byte[] array = new byte[resourceAsStream.available()];
			resourceAsStream.read(array);
			return defineClass(name, array, 0, array.length);
		} catch (Exception e) {
			throw new ClassNotFoundException(name);
		} finally {
			try {
				resourceAsStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
