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

	//����Ƶ���б�
	private List<Channel> channelList;
	//�����б�
	private List<News> newsList;
	//�������ŵ�dao
	private NewsDao rssDao;
	
	public RSSService(){
		rssDao=new FileDaoImpl();
	}
	
	/**
	 * 
	 * @return	����Ƶ���б�
	 */
	public List<Channel> getChannelList(){
		if(channelList==null){
			channelList=new ArrayList<Channel>();
			
			Channel channel1=new Channel();
			channel1.setName("����-�Ƽ�����");
			channel1.setFilePath("NewsFiles/rss_news_science.xml");
			channel1.setUrl("http://rss.sina.com.cn/tech/rollnews.xml");
			
			Channel channel2=new Channel();
			channel2.setName("��Ѷ-��������");
			channel2.setFilePath("NewsFiles/rss_news_country.xml");
			channel2.setUrl("http://news.qq.com/newsgn/rss_newsgn.xml");
			
			Channel channel3=new Channel();
			channel3.setName("����-��������");
			channel3.setFilePath("NewsFiles/rss_news_sports.xml");
			channel3.setUrl("http://rss.sina.com.cn/news/allnews/sports.xml");
			
			Channel channel4=new Channel();
			channel4.setName("����-�������");
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
	 * ���ر��������ļ�
	 * @param filePath	���������ļ�·��
	 * @return	�ĵ�����Document
	 */
	public Document load(String filePath){
		try {
			Document doc=null;
			//����false��ʾ����Ҫ��֤xml�ļ�
			SAXBuilder builder=new SAXBuilder(false);
			File file=new File(filePath);
			doc=builder.build(file);
			return doc;
		} catch (Exception e) {
			System.out.println("�ļ������ڻ��ʽ�����޷�����");
			return null;
		}
	}
	
	/**
	 * �����ĵ�����Document
	 * @param doc		���������ļ�ת��������Document����
	 * @return			�����б�
	 */
	public List<News> parse(Document doc){
		if (doc!=null) {
			newsList = new ArrayList<News>();
			//��ȡ���ڵ�rss
			Element root = doc.getRootElement();
			//��ȡrss�ڵ��µ�channel�ڵ�
			Element channel = root.getChild("channel");
			//��ȡchannel�ڵ��µ�����item�ڵ�
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
	 * ������ʵ����ת��Ϊ�ַ���
	 * @param news
	 * @return
	 */
	public String newsToString(News news){
		String content=null;
		content="���⣺"
		+news.getTitle()
		+"\r\n"
		+"���ӣ�"
		+news.getLink()
		+"\r\n"
		+"���ߣ�"
		+news.getAuthor()
		+"\r\n"
		+"����ʱ�䣺"
		+news.getPubDate()
		+"\r\n\r\n"
		+"�������ݣ�"
		+news.getDescription()
		+"\n"
		+ "-------------------------------------------------------------------------------------------------\r\n\r\n";
		return content;
	}
	
	/**
	 * ��xml�ļ��µ����Žڵ�ת��ΪNews����
	 * @param item	���Žڵ�
	 * @return
	 */
	public News itemToNews(Element item){
		//��ȡ�ڵ�����
		String title=item.getChildText("title").trim();
		String link=item.getChildText("link").trim();
		String author=item.getChildText("author").trim();
		//������Щ��վû��guid�ڵ㣬�����ڴ˴���
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
	 * ��ȡ�����б�
	 * @param filePath	���������ļ�·��
	 * @return	�����б�
	 */
	public List<News> getNewsList(String filePath){
		Document doc=load(filePath);
		newsList=parse(doc);
		return newsList;
	}
	
	/**
	 * ��������
	 * @param newsList	�����б�
	 * @return	����true��ʾ����ɹ�������false��ʾ����ʧ��
	 */
	public boolean saveNews(List<News> newsList){
		return rssDao.saveNews(newsList);
	}
}
