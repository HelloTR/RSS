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
	 * @param newsList   Ҫ����������б�
	 * @return ����true���������ųɹ�������false����������ʧ��
	 */
	public boolean saveNews(List<News> newsList) {
		boolean flag=true;
		File file=new File(filePath);
		BufferedWriter bw=null;
		RSSService rssService=new RSSService();
		try {
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true), "utf-8"));
			//���������б����ݣ����������ݱ��浽�ļ���
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
