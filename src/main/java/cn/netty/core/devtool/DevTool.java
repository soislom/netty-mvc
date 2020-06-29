package cn.netty.core.devtool;

import java.io.InputStream;

/**
 * hot devloyment
 * 
 * @author 34305
 *
 */
public class DevTool extends ClassLoader {

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
			InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
			byte[] array = new byte[resourceAsStream.available()];
			resourceAsStream.read(array);
			return defineClass(name, array, 0, array.length);
		} catch (Exception e) {
			throw new ClassNotFoundException(name);
		}
	}

}
