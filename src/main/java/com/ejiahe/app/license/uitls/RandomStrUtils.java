package com.ejiahe.app.license.uitls;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomStrUtils {
	
	private static IdWorker idWorker = new IdWorker();
	
	/**
	 * 该方法返回一个随机递增字符串id,基于时间戳策略 + 随机数;
	 * @return 返回长度为15位的id字符串;
	 */
	public static String getRandomStr(){
		return Long.toString(idWorker.getId());
	}
	
	
	/**
	 * 获取一个随机数,由当前系统日期毫秒数+6位随机数字字符串
	 */
	public static String getRandomStrWithDatetime(){
		return getRandomStrWithDatetime(6);
	}
	
	
	/**
	 * 获取一个随机数,由当前系统日期毫秒数+appendBound位随机数字字符串
	 * @param appendBound 追加随机数字字符的数量
	 * @return
	 */
	public static String getRandomStrWithDatetime(int appendBound){
		StringBuilder builder = new StringBuilder(String.valueOf(System.currentTimeMillis()));
		builder.append(RandomStringUtils.randomNumeric(appendBound));
		return builder.toString();
	}
	
	/**
	 * 返回随机的ASCII码
	 * @param count 位数
	 * @return
	 *
	 */
	public static String getRandomAscii(int count){
		return RandomStringUtils.randomAscii(count);
	}
	
	/**
	 * 返回随机的数字
	 * @param count 位数
	 * @return
	 *
	 */
	public static String getRandomNumber(int count){
		return RandomStringUtils.randomNumeric(count);
	}
	
	/**
	 * 返回随机的字母+数字
	 * @param count
	 * @return
	 *
	 */
	public static String getRandomAlphanumeric(int count){
		return RandomStringUtils.randomAlphanumeric(count);
	}
	
	
}
