package com.ruanko.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ruanko.dao.NewsDao;
import com.ruanko.dao.impl.FileDaoImpl;
import com.ruanko.model.Channel;
import com.ruanko.model.News;
import com.ruanko.util.NewsUtil;

public class RSSService {

	//新闻频道列表
	private List<Channel> channelList;
	//新闻列表
	private List<News> newsList;
	//操作新闻的dao
	private NewsDao rssDao;
	
	public RSSService(){
		rssDao=new FileDaoImpl();
	}
	
	/**
	 * 
	 * @return	新闻频道列表
	 */
	public List<Channel> getChannelList(){
		if(channelList==null){
			channelList=new ArrayList<Channel>();
			
			Channel channel1=new Channel();
			channel1.setName("新浪-科技新闻");
			channel1.setFilePath("NewsFiles/rss_news_science.xml");
			channel1.setUrl("http://rss.sina.com.cn/tech/rollnews.xml");
			
			Channel channel2=new Channel();
			channel2.setName("腾讯-国内新闻");
			channel2.setFilePath("NewsFiles/rss_news_country.xml");
			channel2.setUrl("http://news.qq.com/newsgn/rss_newsgn.xml");
			
			Channel channel3=new Channel();
			channel3.setName("新浪-体育新闻");
			channel3.setFilePath("NewsFiles/rss_news_sports.xml");
			channel3.setUrl("http://rss.sina.com.cn/news/allnews/sports.xml");
			
			Channel channel4=new Channel();
			channel4.setName("新浪-社会新闻");
			channel4.setFilePath("NewsFiles/rss_news_shehui.xml");
			channel4.setUrl("http://rss.sina.com.cn/news/society/focus15.xml");
			
			channelList.add(channel1);
			channelList.add(channel2);
			channelList.add(channel3);
			channelList.add(channel4);
		}
		return channelList;
	}
	
	/**
	 * 加载本地新闻文件
	 * @param filePath	本地新闻文件路径
	 * @return	文档对象Document
	 */
	public Document load(String filePath){
		try {
			Document doc=null;
			//参数false表示不需要验证xml文件
			SAXBuilder builder=new SAXBuilder(false);
			File file=new File(filePath);
			doc=builder.build(file);
			return doc;
		} catch (Exception e) {
			System.out.println("文件不存在或格式错误，无法解析");
			return null;
		}
	}
	
	/**
	 * 解析文档对象Document
	 * @param doc		本地新闻文件转化而来的Document对象
	 * @return			新闻列表
	 */
	public List<News> parse(Document doc){
		if (doc!=null) {
			newsList = new ArrayList<News>();
			//获取根节点rss
			Element root = doc.getRootElement();
			//获取rss节点下的channel节点
			Element channel = root.getChild("channel");
			//获取channel节点下的所有item节点
			List<Element> itemList = channel.getChildren("item");
			for (Element item : itemList) {
				News news = itemToNews(item);
				newsList.add(news);
			}
			return newsList;
		}else{
			return null;
		}
	}
	
	/**
	 * 把新闻实体类转化为字符串
	 * @param news
	 * @return
	 */
	public String newsToString(News news){
		String content=null;
		content="标题："
		+news.getTitle()
		+"\r\n"
		+"链接："
		+news.getLink()
		+"\r\n"
		+"作者："
		+news.getAuthor()
		+"\r\n"
		+"发布时间："
		+news.getPubDate()
		+"\r\n\r\n"
		+"主体内容："
		+news.getDescription()
		+"\n"
		+ "-------------------------------------------------------------------------------------------------\r\n\r\n";
		return content;
	}
	
	/**
	 * 把xml文件下的新闻节点转换为News对象
	 * @param item	新闻节点
	 * @return
	 */
	public News itemToNews(Element item){
		//获取节点内容
		String title=item.getChildText("title").trim();
		String link=item.getChildText("link").trim();
		String author=item.getChildText("author").trim();
		//由于有些网站没有guid节点，所以在此处理
		String guid=null;
		try {
			guid=item.getChildText("guid").trim();
		} catch (Exception e) {
		}
		String category=item.getChildText("category").trim();
		//Wed, 3 Jun 2015 07:59:50 GMT
		String pubDate=item.getChildText("pubDate").trim();
		if(!NewsUtil.isTrueDate(pubDate)){
			pubDate=NewsUtil.dateChange(pubDate);
		}
		String comments=item.getChildText("comments").trim();
		String description=item.getChildText("description").trim();
		
		News news=new News(title, link, author, guid, category, pubDate, comments, description);
		return news;
	}
	
	/**
	 * 获取新闻列表
	 * @param filePath	本地新闻文件路径
	 * @return	新闻列表
	 */
	public List<News> getNewsList(String filePath){
		Document doc=load(filePath);
		newsList=parse(doc);
		return newsList;
	}
	
	/**
	 * 保存新闻
	 * @param newsList	新闻列表
	 * @return	返回true表示保存成功，返回false表示保存失败
	 */
	public boolean saveNews(List<News> newsList){
		return rssDao.saveNews(newsList);
	}
}
