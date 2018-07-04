package com.ejiahe.app.license.uitls;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VOS号码匹配通配符 转 正则表达式 进行匹配
 * <p>
 * 备注: 不支持D通配符
 * 
 * @author mailingfeng
 *
 */
public class DialRegExUtil {

	/**
	 * 匹配多段
	 * @param source 原始字符串
	 * @param dailRegexs VOS号码匹配通配符，允许多个，逗号分隔
	 * @return
	 */
	public static boolean matchs(String source, String dailRegexs){
		if(Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(dailRegexs)){
			return false;
		}

		String[] patterns = dailRegexs.replaceAll("，", ",").split(",");
		for(String pattern : patterns){
			if(match(source, pattern)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 匹配单段
	 * @param source 原始字符串
	 * @param dailRegex VOS号码匹配通配符
	 * @return
	 */
	public static boolean match(String source, String dailRegex){
		
		String sRegex = dailRegex2StandardRegex(dailRegex.replace('?', 'X'));
		//System.out.println("StandarRegex: " + sRegex);
		return source.matches(sRegex);
	}


	/**
	 * 转成标准的正则表达式
	 * @param dailRegex
	 * @return
	 */
	private static String dailRegex2StandardRegex(String dailRegex){
		
		
		char[] chars = dailRegex.toCharArray();
		StringBuilder sRegex = new StringBuilder();
		
		for(char temp : chars){
			
			switch (temp) {
			case '*':
				sRegex.append("\\*");
				break;

			case '+':
				sRegex.append("\\+");
				break;
			
			case 'x':
			case 'X':
				sRegex.append("[0-9]");
				break;

			case '@':
				sRegex.append("[0-9]*");
				break;
				
			case 'D':
				//不支持D
				break;
				
			default:
				sRegex.append(temp);
				break;
			}
		}
		
		return sRegex.toString();
	}

	/**
	 * 匹配多段叠加费用，判断是否以某段开头，如果是，则找出结束后的下一个索引
	 * @param source 原始字符串
	 * @param dailRegexs VOS号码匹配通配符，允许多个，逗号分隔
	 * @return 匹配命中正则后的下一个索引，若没命中则返回-1
	 */
	public static int matchRatePlusRegexs(String source, String dailRegexs){
		if(Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(dailRegexs)){
			return -1;
		}

		String[] patterns = dailRegexs.replaceAll("，", ",").split(",");
		for(String pattern : patterns){
			String regexPattern = pattern;
			if(!pattern.endsWith("@")){
				regexPattern = pattern + "@";
			}
			if(match(source, regexPattern)) {
				return matchEndIndex(source, pattern);
			}
		}
		return -1;
	}
	/**
	 * 匹配命中正则后的下一个索引，常用于做后续的字符串截取
	 * @param source
	 * @param dailRegex
	 * @return
	 */
	public static int matchEndIndex(String source, String dailRegex){
		String sRegex = dailRegex2StandardRegex(dailRegex.replace('?', 'X'));
		Pattern p = Pattern.compile(sRegex);
		Matcher matcher = p.matcher(source);
		if(matcher.find()){
//			System.out.println(matcher.group());
//			System.out.println(matcher.start());
			return matcher.end();
		}else{
			return -1;
		}
	}

    public static void main(String[] args) throws Exception {
//		String test = "07561113#%*+123456";
//		String regex = "0?56XXX[1-3]#%*+";
		String test = "1365712";
//		String regex = "^136X.*";

		String regex1 = "136[5-9]";
		Pattern p = Pattern.compile(regex1);
		Matcher matcher = p.matcher(test);
		if(matcher.find()){
			System.out.println(matcher.group());

			System.out.println(matcher.start());
			System.out.println(matcher.end());
		}

//		System.out.println(test + " match " + regex + " is:" + DialRegExUtil.matchs(test, regex));
//		System.out.println(DialRegExUtil.dailRegex2StandardRegex(regex));
//		System.out.println(DialRegExUtil.match(test, "^136X.*"));
		System.out.println(test.matches("136[5-9][0-9]*"));
	}

}
