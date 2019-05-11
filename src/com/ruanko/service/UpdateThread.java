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

	private final static int TIMEOUT = 5 * 1000; // ���ӷ�������ʱ��Ϊ5��
	private final static int DELAY_TIME = 5 * 60 * 1000; // ÿ��5���Ӹ���һ��

	private List<Channel> channelList;		//����Ƶ���б������ں�̨��ʱ��������

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
					//���������û�и���xml�ļ�������Ҫ���£�������һ��Ƶ�������Ƿ���Ҫ����
					if (!hasNewRss(conn, file)) {
						continue;
					}
					//�������ӷ������ĳ�ʱʱ��
					conn.setConnectTimeout(TIMEOUT);
					//���ӷ������ķ�����
					int code = conn.getResponseCode();
					//200�������ӷ������ɹ�
					if (code == 200) {
						//��ȡ��������������
						InputStream in = conn.getInputStream();
						//��ȡ���������ص�ͷ�ļ��е��ı����ͣ������еı��뷽ʽ��utf-8����gb2312
						String str = conn.getHeaderField("Content-Type");
						System.out.println(str);
						//Ĭ�ϱ�����Ϊutf-8
						String encoding = "utf-8";
						//���ͷ�ļ��а�����charset����ַ�����˵�����������ص�ͷ�ļ����б��뷽ʽ
						if (str.contains("charset")) {
							int index = str.indexOf("=") + 1;
							//ȡ�����еı��룬����ΪĬ�ϱ���
							encoding = str.substring(index);
						}
						//����תΪxml�ļ���������
						streamToXML(in, file, encoding,channel.getName());
						in.close();
					}
				}
				//5���Ӻ��ٲ鿴��������û�и�������
				JOptionPane.showMessageDialog(null, "�����б���³ɹ�");
				Thread.sleep(DELAY_TIME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��һ��������ת��Ϊxml�ļ�������
	 * @param in	����������
	 * @param file		�����ļ�����
	 * @param encoding	��������Ӧ�ı���
	 */
	private void streamToXML(InputStream in, File file, String encoding,String channelName) {

		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {
			//����һ���ַ���������������뷽ʽΪutf-8
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "utf-8"));
			//����һ���ַ��������������󣬲�����Ӧ�ı��뷽ʽ����
			reader = new BufferedReader(new InputStreamReader(in, encoding));
			//�ȶ�ȡ��һ�У��ɴ��������е�gb2312��Ϊutf-8
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
//			JOptionPane.showMessageDialog(null, channelName+"����������");
		} catch (Exception e) {
			System.out.println("���������ļ�ʧ��");
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
	 *            �����ļ������ӵ�ַ
	 * @param file
	 *            �����ļ�����
	 * @return ����true˵����Ҫ���£�����false����Ҫ����
	 */
	private boolean hasNewRss(HttpURLConnection conn, File file) {
		if((!file.exists())||file.length()<=0){
			return true;
		}
		long current = System.currentTimeMillis();
		// ��÷��������һ�ε��޸�ʱ��
		long httpLastModified = conn.getHeaderFieldDate("Last-Modified",
				current);
		// ��ñ����ļ�����޸�ʱ��
		long fileLastModified = file.lastModified();
		if (httpLastModified > fileLastModified) {
			return true;
		}
		return false;
	}
}
