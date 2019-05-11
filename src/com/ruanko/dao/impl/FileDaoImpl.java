package com.ruanko.dao.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.ruanko.dao.NewsDao;
import com.ruanko.model.News;
import com.ruanko.service.RSSService;

public class FileDaoImpl implements NewsDao {

	private static final String filePath="NewsFiles/rss.txt";
	
	/**
	 * @param newsList   要保存的新闻列表
	 * @return 返回true代表保存新闻成功，返回false代表保存新闻失败
	 */
	public boolean saveNews(List<News> newsList) {
		boolean flag=true;
		File file=new File(filePath);
		BufferedWriter bw=null;
		RSSService rssService=new RSSService();
		try {
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true), "utf-8"));
			//遍历新闻列表内容，将新闻内容保存到文件中
			for(News news:newsList){
				bw.write(rssService.newsToString(news));
				bw.flush();
			}
		} catch (Exception e) {
			flag=false;
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

}
