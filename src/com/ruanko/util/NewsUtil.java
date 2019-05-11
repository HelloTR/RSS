package com.ruanko.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import com.ruanko.model.News;

public class NewsUtil {

	public static void showTable(List<News> newsList,DefaultTableModel dtmTableModel) {
		// ��ձ�������
		while (dtmTableModel.getRowCount() > 0) {
			dtmTableModel.removeRow(dtmTableModel.getRowCount() - 1);
		}
		// �������������б�����Ӧ������������ʾ�������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (News news : newsList) {
			// ��ָ����ʱ���ʽ����õ�ǰ����
			Date date = new Date();
			String currentDate = sdf.format(date);
			// �����ŵı��⣬��ǰ���ڣ��������ں�������ʾ�ڱ����
			String[] data = new String[] { news.getTitle(), currentDate,
					news.getPubDate(), news.getAuthor() };
			dtmTableModel.addRow(data);
		}
	}
	/**
	 * �ѡ�Wed, 3 Jun 2015 07:59:50 GMT�����͵�ʱ��ת��Ϊ 2015-06-03 15:59:50���ָ�ʽ
	 * @param date
	 * @return
	 */
	public static String dateChange(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date newDate = new Date(date);
			return sdf.format(newDate);
	}
	/**
	 * �ж�ʱ���ʽ�Ƿ���ȷ������2015-06-03 15:59:50
	 * @param date
	 * @return
	 */
	public static boolean isTrueDate(String date){
		String regex="^\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}";
		return date.matches(regex)?true:false;
	}
}
