package cn.netty.mvc.factory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ControllerAdvice {

	@Around("@annotation(cn.netty.mvc.annotation.Controller)")
	public void arroundAdvice(ProceedingJoinPoint point) {
//		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		
	}
}
