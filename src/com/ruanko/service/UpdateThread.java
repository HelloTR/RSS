package com.ruanko.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;

import com.ruanko.model.Channel;

public class UpdateThread extends Thread {

	private final static int TIMEOUT = 5 * 1000; // 连接服务器的时间为5秒
	private final static int DELAY_TIME = 5 * 60 * 1000; // 每隔5分钟更新一次

	private List<Channel> channelList;		//新闻频道列表，用于在后台定时更新新闻

	public UpdateThread(List<Channel> channelList) {
		this.channelList = channelList;
	}

	@Override
	public void run() {
		try {
			while (true) {
				for (Channel channel : channelList) {
					URL url = new URL(channel.getUrl());
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					File file = new File(channel.getFilePath());
					//如果服务器没有更新xml文件，则不需要更新，进入下一个频道看看是否需要更新
					if (!hasNewRss(conn, file)) {
						continue;
					}
					//设置连接服务器的超时时间
					conn.setConnectTimeout(TIMEOUT);
					//连接服务器的返回码
					int code = conn.getResponseCode();
					//200代表连接服务器成功
					if (code == 200) {
						//获取服务器的输入流
						InputStream in = conn.getInputStream();
						//获取服务器返回的头文件中的文本类型，看其中的编码方式是utf-8还是gb2312
						String str = conn.getHeaderField("Content-Type");
						System.out.println(str);
						//默认编码设为utf-8
						String encoding = "utf-8";
						//如果头文件中包含有charset这个字符串，说明服务器返回的头文件中有编码方式
						if (str.contains("charset")) {
							int index = str.indexOf("=") + 1;
							//取出其中的编码，并设为默认编码
							encoding = str.substring(index);
						}
						//把流转为xml文件保存起来
						streamToXML(in, file, encoding,channel.getName());
						in.close();
					}
				}
				//5分钟后再查看服务器有没有更新数据
				JOptionPane.showMessageDialog(null, "新闻列表更新成功");
				Thread.sleep(DELAY_TIME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把一个输入流转化为xml文件并保存
	 * @param in	输入流对象
	 * @param file		本地文件对象
	 * @param encoding	输入流对应的编码
	 */
	private void streamToXML(InputStream in, File file, String encoding,String channelName) {

		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {
			//创建一个字符缓存输出流，编码方式为utf-8
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "utf-8"));
			//创建一个字符缓存输入流对象，并按对应的编码方式编码
			reader = new BufferedReader(new InputStreamReader(in, encoding));
			//先读取第一行，由此来把其中的gb2312改为utf-8
			String str = reader.readLine();
			if (str.contains("gb2312")) {
				str = str.replace("gb2312", "utf-8");
			} else if (str.contains("GB2312")) {
				str = str.replace("GB2312", "utf-8");
			}
			writer.write(str);
			while ((str = reader.readLine()) != null) {
				writer.write(str);
			}
//			JOptionPane.showMessageDialog(null, channelName+"更新了内容");
		} catch (Exception e) {
			System.out.println("下载新闻文件失败");
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e2) {
			}
		}

	}

	/**
	 * @param conn
	 *            网络文件的链接地址
	 * @param file
	 *            本地文件对象
	 * @return 返回true说明需要更新，返回false不需要更新
	 */
	private boolean hasNewRss(HttpURLConnection conn, File file) {
		if((!file.exists())||file.length()<=0){
			return true;
		}
		long current = System.currentTimeMillis();
		// 获得服务器最后一次的修改时间
		long httpLastModified = conn.getHeaderFieldDate("Last-Modified",
				current);
		// 获得本地文件最后修改时间
		long fileLastModified = file.lastModified();
		if (httpLastModified > fileLastModified) {
			return true;
		}
		return false;
	}
}
