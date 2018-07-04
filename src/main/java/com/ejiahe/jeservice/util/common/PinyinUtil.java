package com.ejiahe.jeservice.util.common;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @className:PinyingUtil.java  
 * @classDescription:拼音操作工具类
 */  
  
public class PinyinUtil {   
  
    /**  
     * 将字符串转换成拼音列表(全拼小写,忽略音标)
     *   
     * @param src  
     * @return  
     */  
    public static List<String> stringToPinyin(String src) {   
        return stringToPinyin(src, false , true , "");   
    }
    
    /**
     * 将字符串转换成拼音列表(全拼小写,忽略音标)
     * @param src
     * @param wordSeparator - 字符的拼音的分隔符
     * @return
     */
    public static List<String> stringToPinyin(String src , String wordSeparator){
    	return stringToPinyin(src, false , true , wordSeparator);   
    }
    
    /**  
     * 将字符串转换成拼音列表(小写,忽略音标), 自定义是否只抽取拼音首字母
     *   
     * @param src  
     * @param isOnlyFirst - 是否只抽取拼音首字母
     * @return  
     */  
    public static List<String> stringToPinyin(String src , boolean isOnlyFirst) {   
        return stringToPinyin(src, isOnlyFirst , true , "");
    }
    
    /**  
     * 将字符串转换成拼音列表(可自定义大小写, 是否只抽取拼音首字母)
     *   
     * @param src
     * @param isOnlyFirstString 是否只抽取拼音首字母
     * @param isLowerCase - 是否为小写形式
     * @param wordSeparator - 字符拼音间的分隔符
     * @return
     */  
    public static List<String> stringToPinyin(String src, 
    		boolean isOnlyFirstString, boolean isLowerCase, String wordSeparator) {   
        // 判断字符串是否为空
        if ("".equals(src) || null == src) {   
            return Collections.emptyList();
        }
        
        if(wordSeparator == null){
        	wordSeparator = "";
        }
        
        char[] srcChar = src.toCharArray();   
        int srcCount = srcChar.length;  
        
        //抽取字符串的所有单字符多音字集合
        List<List<String>> originalCharPinyins = new ArrayList<List<String>>(srcCount);
        for (int i = 0; i < srcCount; i++) {   
        	originalCharPinyins.add(charToPinyin(srcChar[i], isOnlyFirstString ,isLowerCase));  
        }
        
        //对各个单字符多音字集合进行排列组合
        List<String> resultPinyinList = getStringArraysRankList(originalCharPinyins, wordSeparator);
        
        return resultPinyinList;   
    }   
  
    /**
     * 将单个字符转换成拼音(全拼或者首字母),忽略音标
     * @param src
     * @param isOnlyFirst - 是否只提取拼音首字母
     * @param isLowerCase - 是否为小写形式
     * @return
     */
    public static List<String> charToPinyin(char src , boolean isOnlyFirst , boolean isLowerCase) {
        // 创建汉语拼音处理类   
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
        // 输出设置，大小写，音标方式   
        if(isLowerCase){
        	defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        }
        else{
        	defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE); 
        }
         
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
  
        List<String> pinyinList = new ArrayList<String>();
  
        // 如果是中文   
        if (src > 128) {   
            try {   
                // 转换得出结果   
                String[] strs = PinyinHelper.toHanyuPinyinStringArray(src,   
                        defaultFormat);
                
        		for(int i=0 , length=strs.length ; i<length ; i++){
        			String tempStr = null;
        			//进行条件过滤(首字母, 大小写)
                    if(isOnlyFirst){
                    	tempStr = strs[i].substring(0, 1);
                    }
                    else{
                    	tempStr = strs[i];
                    }
                    
                    //过滤不重复的
            		if(!pinyinList.contains(tempStr)){
            			pinyinList.add(tempStr);
            		}
            	}
  
            } catch (Exception e) {   
//                System.out.println("忽略不规范字符:" + src);
                pinyinList.add("");
            }   
        } else {   
        	pinyinList.add(String.valueOf(src));   
        }
        
       return pinyinList;
  
    }   
  

    /**  
     * 将字符串数组转换成字符串  
     * @param str   
     * @param separator 各个字符串之间的分隔符  
     * @return  
     */  
    public static String stringListToString(List<String> str, String separator) {   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0 , length = str.size(); i < length; i++) {   
            sb.append(str.get(i));   
            if (length != (i + 1)) {   
                sb.append(separator);   
            }   
        }   
        return sb.toString();   
    }   
    
    /**  
     * 简单的将各个字符数组之间用逗号连接起来  
     * @param str  
     * @return  
     */  
    public  static String stringListToString(List<String> str){   
        return stringListToString(str,",");   
    }   
    /**  
     * 将字符数组转换成字符串  
     * @param str   
     * @param separator 各个字符串之间的分隔符  
     * @return  
     */  
    public static String charArrayToString(char[] ch, String separator) 
{   
        StringBuffer sb = new StringBuffer();   
        
        for (int i = 0; i < ch.length; i++) {   
            sb.append(ch[i]);   
            if (ch.length != (i + 1)) {   
                sb.append(separator);   
            }   
        }   
        return sb.toString();   
    }   
       
    /**  
     * 将字符数组转换成字符串  
     * @param str   
     * @return  
     */  
    public static String charArrayToString(char[] ch) {   
        return charArrayToString(ch," ");   
    }   
  
 
    
    
    /**
     * 对若干个字符列表进行有序的组合排列,
     * @param originalStrs - 准备进行组合排列的若干个字符串列表(有序)
     * @return
     */
    private static List<String> getStringArraysRankList(List<List<String>> originalStrs, String separator){
    	List<String> rankResultList = new ArrayList<String>();
    	
    	//目前排列到的层次
    	int rankLevel = 0;
    	rankStringArrays(originalStrs , rankResultList , rankLevel , null , separator, 10);
    	
		return rankResultList;
    }
    		
    /**
     * 对若干个字符列表进行组合排列
     * @param originalStrs - 原始要进行组合排列的若干个字符串列表
     * @param rankResultList - 所有有序的排列组合的列表结合
     * @param rankLevel - 当前排列的层级,最上层为0级
     * @param prevLevelStr - 上一层组装生成的字符串
     */
    private static void rankStringArrays(List<List<String>> originalStrs , List<String> rankResultList ,
    		int rankLevel , String prevLevelStr , String seperator, int maxCount){
    	
    	if(rankLevel < 0){
    		return ;
    	}
    	
    	//已生成拼音数达到最大拼音数,不再进行组合;
    	if(maxCount > 0 && rankResultList.size() >= maxCount){
    		return;
    	}
    	
    	//是否所有层级已经排列到了
    	if(rankLevel == 0){
    		List<String> firstStrs = originalStrs.get(rankLevel);
    		for(String str : firstStrs){
    			rankStringArrays(originalStrs, rankResultList, rankLevel+1, str, seperator, maxCount);
    		}
    	}
    	else if(rankLevel >= originalStrs.size()){
    		rankResultList.add(prevLevelStr);
    	}
    	else{
    		List<String> strs = originalStrs.get(rankLevel);
    		for(String str : strs){
    			String tempLevelStr = prevLevelStr + seperator +  str;
    			rankStringArrays(originalStrs, rankResultList, rankLevel+1, tempLevelStr, seperator, maxCount);
    		}
    	}
    }
    
    
    /*public static void main(String[] args) {   
    	String china = "猋恏驫嚃";
        System.out.println(stringListToString(stringToPinyin(china)));   
        System.out.println(stringListToString(stringToPinyin(china ,true)));   
        System.out.println(stringListToString(stringToPinyin(china ,true , false))); 
        
        String china2 = "卓力行";
        System.out.println(stringListToString(stringToPinyin(china2)));   
        System.out.println(stringListToString(stringToPinyin(china2 ,true)));   
        System.out.println(stringListToString(stringToPinyin(china2 ,true , false))); 
        
    }   */
  
}  