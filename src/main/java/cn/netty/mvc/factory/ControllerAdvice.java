package cn.netty.mvc.factory;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ControllerAdvice implements MethodInterceptor{

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		System.out.println("�����Ƕ�Ŀ���������ǿ������");
        //ע������ķ������ã������÷���Ŷ������
        Object object = proxy.invokeSuper(obj, args);
		return object;
	}

}
