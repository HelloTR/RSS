package com.ruanko.model;

public class Channel {

	private String name; // Ƶ������
	private String filePath; // ����Ƶ���ļ�·��
	private String url; // ����Ƶ���ļ�·��

	public Channel() {

	}

	public Channel(String name, String filePath, String url) {
		this.name = name;
		this.filePath = filePath;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return name;
	}
}
