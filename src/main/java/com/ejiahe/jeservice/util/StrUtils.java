package com.ejiahe.jeservice.util;

import com.google.common.base.Strings;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class StrUtils {

	public static final String DEFAULT_STRING_SEPARATOR = ",";
	
    /**  
     * 将字符串数组转换成字符串  
     * @param str   
     * @param separator 各个字符串之间的分隔符  
     * @return  
     */  
    public static String stringCollectionToString(Collection<String> str, String separator) {   
        StringBuffer sb = new StringBuffer();   
        int i = 0;
//        Iterator<String> iterator = str.iterator();
//        int length = str.size();
//        while(iterator.hasNext()){
//        	sb.append(iterator.next());
//        	if (length != (i + 1)) {   
//                sb.append(separator);   
//            }  
//        }
        
        for(String temp : str){
        	if(Strings.isNullOrEmpty(temp)) continue;
        	
        	if(i != 0){
        		sb.append(separator);
        	}
        	
        	sb.append(temp);
        	i++;
        }
         
        return sb.toString();   
    }   
    
    /**  
     * 简单的将各个字符数组之间用逗号连接起来  
     * @param str  
     * @return  
     */  
    public  static String stringCollectionToString(Collection<String> str){   
        return stringCollectionToString(str,DEFAULT_STRING_SEPARATOR);   
    }  
	
    
    /**
     * 将字符串以,分割成Collection
     * @param str
     * @return
     *
     */
    public static Collection<String> stringToCollection(String str){
    	return stringToCollection(str,DEFAULT_STRING_SEPARATOR);
    }
    
    /**
     * 将字符串以指定分隔符(separtor)分割成Collection
     * @param str
     * @param separtor 分隔字符
     * @return
     *
     */
    public static Collection<String> stringToCollection(String str, String separtor){
    	return strinToList(str,separtor);
    }
    
    public static List<String> stringToList(String str){
    	return strinToList(str,DEFAULT_STRING_SEPARATOR);
    }
    
    public static List<String> strinToList(String str,String separtor){
    	String[] temp = str.split(separtor);
    	List<String> subStrs = new ArrayList<String>();
    	
    	for (int i = 0; i < temp.length; i++){
    		String subStr = temp[i];
    		if (subStr != null && !"".equals(subStr)){
    			subStrs.add(subStr);
    		}
    	}
    	
    	return subStrs;
    	
//    	return Arrays.asList(str.split(separtor));
    }
    
    
    /**
     * 格式化替换字符串;
     * <p>
     * 将参数数组依次填充{}占位符;
     * 若需输出"{}",则需要在{}前加上\\,表示转义;
     * @param srcString 例如:"我是{},你是{}"
     * @param args 例如: {"my","you"}
     * @return 
     *
     */
    public static String arrayFormat(String srcString, Object ...args){
    	return MessageFormatter.arrayFormat(srcString, args);
    }
    
    
    /**
     * 替换字符串.
     * 假如srcString = "您的密码是{pwd}", 
     * values =  Map{ "pwd":"123456" }, 
     * 那么格式化后的字符串为: "您的密码是123456"
     * @param srcString 原始字符串
     * @param values 占位符替换值 
     * @return
     *
     */
    public static String replaceFormat(String srcString, Map<String,String> values){
    	String result = srcString;
    	if(values != null){
    		for(Map.Entry<String, String> entry : values.entrySet()){
    			result = result.replace("{"+ entry.getKey() + "}", entry.getValue());
    		}
    	}
    	return result;
    }
    
    /**
     * 替换字符串.
     * 当hasSeparator为true时，占位字符两侧有分隔符，否则没有
     * 例如，hasSeparator为true时，srcString = "您的密码是{pwd}", 
     * hasSeparator为false时，srcString = "您的密码是pwd"
     * values =  Map{ "pwd":"123456" }, 
     * 那么格式化后的字符串为: "您的密码是123456"
     * @param srcString 原始字符串
     * @param values 占位符替换值 
     * @param hasSeparator 是否有分隔符
     * @return
     *
     */
    public static String replaceFormat(String srcString, Map<String,String> values,
    		boolean hasSeparator){
    	String result = srcString;
    	if(values != null){
    		for(Map.Entry<String, String> entry : values.entrySet()){
    			if (hasSeparator){
    				result = result.replace("{"+ entry.getKey() + "}", entry.getValue());
    			} else {
    				result = result.replace(entry.getKey(), entry.getValue());
    			}
    		}
    	}
    	return result;
    }
    
    /**
     * 把一个字符串转化成Map形式
     * @param str
     * @param elementSeparator map中各个元素的分隔符
     * @param keyValueSeparator 某个map元素中key和value之间的分隔符
     * @return str为null时，返回空map
     */
    public static Map<String, String> strToMap(String str, String elementSeparator,
    		String keyValueSeparator){
    	if (str == null){
    		return Collections.emptyMap();
    	}
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	Collection<String> elements = stringToCollection(str, elementSeparator);
    	for (String element : elements){
    		String[] temp = element.split(keyValueSeparator);
    		if (temp.length == 2){
    			map.put(temp[0], temp[1]);
    		}
    	}
    	
    	return map;
    }
    
    /**
     * 把一个map转化成字符串形式
     * @param map
     * @param elementSeparator map中各个元素的分隔符
     * @param keyValueSeparator 某个map元素中key和value之间的分隔符
     * @return map为null时，返回空字符串
     */
    public static String mapToStr(Map<String, String>map,  String elementSeparator,
    		String keyValueSeparator){
    	StringBuilder str = new StringBuilder();
    	
    	for (String key : map.keySet()){
    		String value = map.get(key);
    		str.append(key);
    		str.append(keyValueSeparator);
    		str.append(value);
    		str.append(elementSeparator);
    	}
    	
    	return str.toString();
    }
    
    /**
	 * 判断字符串是空字符串
	 * 
	 * @param pStr
	 * @return
	 */
	public static boolean isBlankStr(String pStr) {
		return (pStr == null) || (pStr.trim().length() == 0)
				|| (pStr.equalsIgnoreCase("null"));
	}

	/**
	 * 判断字符串不是空字符串
	 * 
	 * @param pStr
	 * @return
	 */
	public static boolean isNotBlank(String pStr) {
		return !isBlankStr(pStr);
	}
    
	/**
	 * 如果为null字符串，则替换为空白字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String ifNullToBlank(String str) {
		if (isBlankStr(str)) {
			return "";
		}
		return str;
	}
	
	
	public static String escapeSql(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("'", "''");
	}
	
	/**
	 * 截取字符串(限定最大字节数)
	 * @param src
	 * @param maxBytes 最大字节数
	 * @param charset 字符编码,若为Null,将取UTF8编码
	 * @return 截取后的字符串, 当maxBytes小于一个字符字节数或编码不识别时, 截取失败,会返回""
	 */
	public static String substringWithMaxBytes(String src, int maxBytes, String charset){
		if(charset == null){
			charset = "UTF-8";
		}
		
		String temp = src;
		try {
			int bytes = temp.getBytes(charset).length;
			int i = 0;
			int length = src.length();
			while(bytes > maxBytes){
				i++;
				if(length - i > 0){
					temp = src.substring(0, length - i);
				}else{
					temp = "";
					break;
				}
				bytes = temp.getBytes(charset).length;
			}
			
		} catch (UnsupportedEncodingException e) {
			//ignore;
			temp = "";
		}
		
		return temp;
	}
	
}
