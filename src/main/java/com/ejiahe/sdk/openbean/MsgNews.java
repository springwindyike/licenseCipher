package com.ejiahe.sdk.openbean;

import java.util.Arrays;
import java.util.List;


/**
 * 推送消息中的图文消息块
 * 
 * 
 * @author MaiJingFeng
 */
public class MsgNews implements Msg{
	
	private List<Article> articles;

	public MsgNews(){
		
	}
	
	public MsgNews(Article article){
		articles = Arrays.asList(article);
	}
	
	public List<Article> getArticles() {
		return articles;
	}

	
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	@Override
	public String getMsg_type() {
		return Msg.TYPE_NEWS;
	}
	
	
	
}
