package com.ejiahe.app.license.uitls;

 import com.alibaba.fastjson.JSONObject;
 import com.google.common.base.Strings;
 import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.*;

@SuppressWarnings("rawtypes")
public class LicenseUtil {

	public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	public static final String YYYYMMDDWWHHMM="yyyyMMddWWHHmm";
	public static final String YYYYMMDDHHMMSS="yyyyMMddHHmmss";
	public static final String YYYYMMDDHHMMSSS="yyyyMMddHHmmsss";
	public static final String ZONE_TIME_ID_UTC = "UTC";
	public static final String YYYY_MM_DD_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ssZ";

	public static final String YearMonthDayHourMinitue = "MM月dd日 HH:mm";

	public static Logger logger = LoggerFactory.getLogger(LicenseUtil.class);


	/**
	 * 日期(时间)转化为字符串.
	 *
	 * @param formater
	 *            日期或时间的格式.
	 * @param aDate
	 *            java.util.Date类的实例.
	 * @return 日期转化后的字符串.
	 */
	public static String date2String(String formater, Date aDate) {
		if (formater == null || "".equals(formater))
			return null;
		if (aDate == null)
			return null;
		return (new SimpleDateFormat(formater)).format(aDate);
	}

	public static Date strToDateWithEnglish(String dateStr){
		try {
			return new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).parse(dateStr);
		} catch (ParseException e) {

		}
		return null;
	}

	public static Date strToDate(String formater,String dateStr){
		if (Strings.isNullOrEmpty(formater)|| Strings.isNullOrEmpty(dateStr))
			return null;
		try {
			return (new SimpleDateFormat(formater)).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}


	/**
	 * 判断对象是否为空，对象适用于：集合，字符序列，Map，一维数组，通用对象
	 * @param obj
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:06:19
	 */
	public static boolean isEmpty(Object obj){
		if(obj == null) return true;
		if(obj instanceof Collection){
			return ((Collection) obj).isEmpty();
		}else if(obj instanceof CharSequence){
			return ((CharSequence) obj).toString().trim().length() == 0 || obj.toString().trim().equalsIgnoreCase("null")||"".equals(obj.toString().trim());
		}else if(obj instanceof Map){
			return ((Map) obj).isEmpty();
		}else if(obj instanceof Object[]){
			return ((Object []) obj).length == 0;
		}
		return false;	
	}
	
	/**
	 * 判断对象是否为空
	 * @param objs
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:25
	 */
	public static boolean isEmpty(Object ...objs){
		for(Object obj : objs){
			if(isEmpty(obj) == false)
				return false;
		}
		return true;
	}
	
	/**
	 * 判断对象是否不为空
	 * @param obj
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:34
	 */
	public static boolean isNotEmpty(Object obj){
		return isEmpty(obj) == false;
	}
	
	/**
	 * 判断对象是否不为空，必须所有元素都不为空
	 * @param objs
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:41
	 */
	public static boolean isNotEmpty(Object ...objs){
		for(Object obj : objs){
			if(isEmpty(obj))
				return false;
		}
		return true;
	}
	/**
	 * 时间开始
	 * @return
	 * @author focus
	 * @date 2015年9月8日
	 * @time 下午2:30:43
	 */
	public static long getstartTime(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 耗时
	 * @param startTime
	 * @return
	 * @author focus
	 * @date 2015年9月8日
	 * @time 下午2:30:37
	 */
	public static String elapse(long startTime){
		return " 耗时: " + (getstartTime() - startTime ) + " 毫秒  ";
	}
	
	/**
	 * 转换Bean 成为josn字符串
	 * @param bean
	 * @return
	 * @author focus
	 * @date 2015年10月10日
	 * @time 下午4:17:55
	 */
	public static String parseBeanToJosnStr(Object bean){
		return  JSONObject.toJSONString(bean);
	}
	
	/**
	 * 转换JsonStr 为Map
	 * @param jsonStr
	 * @return
	 * @author focus
	 * @date 2015年10月9日
	 * @time 下午1:58:57
	 */
	public static Map<String,String> parseJsonStrToMap(String jsonStr){
		
		try {
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);

			Map<String,String> map = new HashMap<String, String>();
			for(Object key : jsonObject.keySet()){
				map.put(String.valueOf(key), jsonObject.getString(String.valueOf(key)));
			}
			return map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 转换Map 为指定的Bean
	 * @param clzz
	 * @param map
	 * @return
	 * @author focus
	 * @date 2015年10月9日
	 * @time 下午2:08:56
	 */
	public static <T>T parseMapToBean(Class<T> clzz,Map<String,Object> map){
		
		T t = null;
		try {
			t = clzz.newInstance();
			Field[] fields = clzz.getDeclaredFields();
			for(Field field : fields){
				
				field.setAccessible(true);
				String fieldName = field.getName();
				Class fieldType = field.getType();
				
				if(map.containsKey(fieldName)){
					Object value = map.get(fieldName);
					if(value.getClass().equals(fieldType)){
						field.set(t, value);
					}
				}
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}
	

	public static String parseMapToJson(Map<String,?> map){
		if(map == null) return null;
		return JSONObject.toJSONString(map);
	}

	/**
	 * 写文件
	 * @param fileContent
	 * @param file
	 * @author focus
	 * @date 2015年10月10日
	 * @time 上午10:40:13
	 */
	public static void writeFile(String fileContent,File file){
		
		PrintWriter write = null;
		try {
			write = new PrintWriter(file);
			write.print(fileContent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			write.flush();
			write.close();
		}
	}
	
	public static File getFile(){
		File dir = new File("/license");
		File file = new File (dir,"lisence.dat");
		if(file.exists() == false){
			try {
				if(dir.exists() == false){
					dir.mkdir();
				}
				file.createNewFile();
				logger.info("文件创建成功");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(" 文件路径为： " + file.getAbsolutePath());
		return file;
	}
	/**
	 * 读文件
	 * @param file
	 * @return
	 * @author focus
	 * @date 2015年10月10日
	 * @time 上午10:40:05
	 */
	public static StringBuffer readFile(File file){
		
		StringBuffer buf = new StringBuffer();
		BufferedReader read = null;
		try {
			read = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str = null;
			while ((str = read.readLine()) != null) {
				buf.append(str);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			try {
				read.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buf;
	}
	
	/**
     * BASE64解密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return BaseEncoding.base64()
				.decode(key);
    }
 
    /**
     * BASE64加密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return BaseEncoding.base64().encode(key);
    }

	public static <T>List<T> page(List<T> list,int startIndex,int pageSize){

		if(list == null) return Collections.EMPTY_LIST;
		int size = list.size();

		if(startIndex > size) return Collections.EMPTY_LIST;
		pageSize = pageSize + startIndex < size ? pageSize : size - startIndex;
		return list.subList(startIndex,startIndex + pageSize);
	}
//	public static void main(String[] args) throws Exception {
//		Map<String,String> map = new HashMap<String,String>();
//
//		map.put("abc","ers");
//		map.put("bb","dd");
//
//		System.out.println(parseMapToJson(map));
//	}

	public static Long getNextDate(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return c.getTimeInMillis();
	}
}
