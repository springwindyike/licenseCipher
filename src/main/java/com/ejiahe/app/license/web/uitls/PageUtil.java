package com.ejiahe.app.license.web.uitls;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 分页bean
 *
 * @author liaofei
 * @date 2015年9月2日
 */
public class PageUtil {
	// 分页信息
	private int currentPage;// 当前页
	private int pageSize;//每页显示多少条
	private int countRecord;// 总记录
	private int countPage;// 总页数

	private int startPage;// 页面中的起始页
	private int endPage;// 页面中的结束页
	//private Object data;//数据集
	
	public int SHOW_PAGE = 10;// 页面中显示的总页数,只用双数.
	//默认每页显示的行数
	public static final int DEFAULT_PAGE_SIZE = 10;

	public PageUtil(){};
	
	public PageUtil(int currentPage, int countRecord, int countPage,
			int startPage, int endPage) {
		this.currentPage = currentPage;
		this.countRecord = countRecord;
		this.countPage = countPage;
		this.startPage = startPage;
		this.endPage = endPage;
	}

	/**
	 * 分页的方法
	 * 
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @param countRecord 总记录
	 * @param countPage 总页数
	 * 
	 * */
	public void init(int currentPage, int pageSize, int countRecord, int countPage) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.countRecord = countRecord;
		this.countPage = countPage;
		int temp = (int) (this.SHOW_PAGE / 2);
		if (this.countPage < (temp + 1)) {
			this.startPage = 1; // 页面中起始页就是1
			this.endPage = this.countPage;// 页面中的最终页就是总页数
		} else {
			if (this.currentPage <= temp) {
				this.startPage = 1;
				this.endPage = this.SHOW_PAGE;
				/** 判断页面的最终页是否大于总页数 */
				if (this.endPage >= this.countPage) {
					this.endPage = this.countPage;
				}
			} else {
				this.startPage = this.currentPage - (temp - 1);
				this.endPage = this.currentPage + temp;

				if (this.endPage >= this.countPage) {
					this.endPage = this.countPage;
					if (this.countPage < this.SHOW_PAGE) {
						this.startPage = 1;
					} else {
						this.startPage = this.endPage - (this.SHOW_PAGE - 1);
					}
				}
			}

		}

	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize(){
		return pageSize;
	}

	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	public int getCountRecord() {
		return countRecord;
	}

	public void setCountRecord(int countRecord) {
		this.countRecord = countRecord;
	}

	public int getCountPage() {
		return countPage;
	}

	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	
	
//	public Object getData() {
//		return data;
//	}
//
//	public void setData(Object data) {
//		this.data = data;
//	}

	/**
	 * 分页时把各属性值放入JSONObject作用域里
	 * @param result
	 * @param currentPage
	 * @param countRecord
	 * @param pageSize
	 * @author liaofei
	 * @date 2015年9月6日
	 */
	public void setPageBeanToJson(JSONObject result,
			int currentPage, int countRecord, int pageSize) {
		int countPage = getCountPage(countRecord, pageSize);// 总页数
		init(currentPage, pageSize, countRecord, countPage);
		result.put("currentPage", currentPage);
		result.put("countRecord", countRecord);// String.valueOf(countRecord)
		result.put("countPage", countPage);// String.valueOf(countPage)
		result.put("startPage", getStartPage());
		result.put("endPage", getEndPage());
	}
	/**
	 * 分页时把各属性值放入request作用域里
	 * @param request
	 * @param currentPage
	 * @param countRecord
	 * @param pageSize
	 * @author liaofei
	 * @date 2015年9月2日
	 */
	public void setPageBeanToRequest(HttpServletRequest request,
			int currentPage, int countRecord, int pageSize) {
		int countPage = getCountPage(countRecord, pageSize);// 总页数
		init(currentPage, pageSize, countRecord, countPage);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("countRecord", countRecord);// String.valueOf(countRecord)
		request.setAttribute("countPage", countPage);// String.valueOf(countPage)
		request.setAttribute("startPage", getStartPage());
		request.setAttribute("endPage", getEndPage());
	}
	/**
	 * 取得pageBean的所有属性
	 * @param currentPage
	 * @param pageSize
	 * @param countRecord
	 * @return PageUtil
	 * @author liaofei
	 * @date 2015年9月2日
	 */
	public PageUtil getPageBean(int currentPage, int pageSize, int countRecord) {//, Object data
		if (currentPage == 0) {
			currentPage = 1;
		}
		int countPage = getCountPage(countRecord, pageSize);// 总页数
		init(currentPage, pageSize, countRecord, countPage);
		//setData(data);
		return this;
	}
	/**
	 * 计算分页起始下标，根据当前页和每页的数量
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @author liaofei
	 * @date 2015年9月2日
	 */
	public int getStartRow(int currentPage, int pageSize){
		int rowIndex = 0;
		if(currentPage > 0){
			rowIndex = (currentPage - 1) * pageSize;
		}
		return rowIndex;
	}
	/**
	 * 根据总记录数和每页的数量算出总页数
	 * 
	 * @param countRecord
	 * @param pageSize
	 * @return
	 */
	private int getCountPage(int countRecord, int pageSize) {
		if (countRecord == 0) {
			return 1;
		}
		int countPage = countRecord / pageSize;// 总页数
		int intEnd = countRecord % pageSize;// 末页条数
		if (intEnd > 0) {
			countPage++;
		}
		return countPage;
	}

	public static void createOrderAndLimit(Map<String, Object> params, String orderBy, String sortOrder, Integer offset, Integer limit){
		params.put("orderBy", orderBy);
		params.put("sortOrder", sortOrder);
		params.put("offset", offset);
		params.put("limit", limit);
	}
}
