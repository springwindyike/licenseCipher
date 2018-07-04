package com.ejiahe.jeservice.jsonrpc.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequirePermission {
	
	JSONRPCClientPermission basePermission() default JSONRPCClientPermission.ZERO;
	
}
