package com.ejiahe.jeservice.util.common;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author ZhangXueJun
 * @date 2015年3月24日
 */
public class BeanUtil {

	/**
	 * 拷贝对象
	 * 
	 * @param from
	 *            源对象
	 * @param to
	 *            目标对象
	 */
	public static void copyProperties(Object from, Object to) {
		try {
			PropertyUtils.copyProperties(to, from);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 获取某项属性值
	 * 
	 * @param bean
	 * @param name
	 * @return
	 */
	public static Object getBeanProperty(Object bean, String name) {
		try {
			return PropertyUtils.getProperty(bean, name);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 在两对象间拷贝参数(会自动跳过from中propertyValue=null的拷贝)
	 * @param from 源对象
	 * @param to 目标对象
	 *
	 */
	@SuppressWarnings("unchecked")
	public static void copyPropertiesSkipNull(Object from, Object to){
		if(from == null){
			return;
		}
		
		Map<String, Object> p = null;
		try {
			p = PropertyUtils.describe(from);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(p == null){
			return;
		}
		
		for(Map.Entry<String,Object> entry : p.entrySet()){
			String name = entry.getKey();
			Object value = entry.getValue();
			if(!name.equals("class") && value != null){
				if(PropertyUtils.isWriteable(to, name)){
					try {
						PropertyUtils.setProperty(to, name, value);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		}
		
	}
	
}
