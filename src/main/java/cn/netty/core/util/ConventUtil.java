package cn.netty.core.util;

public class ConventUtil {

	public static <T> T convent(Class<T> clasz, Object object) {
		if (clasz.isInstance(object)) {
			return clasz.cast(object);
		}
		return null;
	}
	
}
