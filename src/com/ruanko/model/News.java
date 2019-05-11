package com.ruanko.model;

public class News {

	private String title;//新闻标题
	private String link;//新闻链接
	private String author;//新闻作者
	private String guid;//新闻网站
	private String category;//新闻分类
	private String pubDate;//新闻发布时间
	private String comments;//新闻评论
	private String description;//新闻描述
	
	public News(){
		
	}
	
	public News(String title, String link, String author, String guid,
			String category, String pubDate, String comments, String description) {
		this.title = title;
		this.link = link;
		this.author = author;
		this.guid = guid;
		this.category = category;
		this.pubDate = pubDate;
		this.comments = comments;
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
