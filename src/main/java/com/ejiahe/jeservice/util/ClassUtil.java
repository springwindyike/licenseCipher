package com.ejiahe.jeservice.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {
	
	private ClassUtil(){
		
	}
	
	
	
	/**
	 * 获取所有公共方法(包括自身声明,实现接口,继承的)
	 * @param c
	 * @return
	 */
	public static List<String> getAllPublicMethodName(Class c){
		List<String> methods = new ArrayList<String>();
		for(Method method : c.getMethods()){
			methods.add(method.getName());
		}
		return methods;
	}
	
	/**
	 * 获取所有方法(任何作用域)-(包括自身声明,实现接口, 即除了构造函数外所有显式存在于class c中的方法)
	 * @param c
	 * @return
	 */
	public static List<String> getAllMethodNameWithOwn(Class c){
		List<String> methods = new ArrayList<String>();
		for(Method method : c.getDeclaredMethods()){
			methods.add(method.getName());
		}
		return methods;
	}
	
	
	/**
	 * 获取所有公共方法,只包含在自身类中实现的(包括声明和重载的公共方法)
	 * @param c
	 * @return
	 */
	public static List<String> getAllPublicMethodNameWithOwn(Class c){
		List<String> methods = new ArrayList<String>();
		List<String> allPublicMethods = getAllPublicMethodName(c);
		List<String> allMethodsWithOwn = getAllMethodNameWithOwn(c);
		for(String tempMethod : allPublicMethods){
			if(allMethodsWithOwn.contains(tempMethod)){
				methods.add(tempMethod);
			}
		}
		return methods;
		
	}
	
	
//	public static List<String> getAllPublicMethodNameWithOut
	
	
}
