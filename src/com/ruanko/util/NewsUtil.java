package com.ruanko.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import com.ruanko.model.News;

public class NewsUtil {

	public static void showTable(List<News> newsList,DefaultTableModel dtmTableModel) {
		// 清空表格的内容
		while (dtmTableModel.getRowCount() > 0) {
			dtmTableModel.removeRow(dtmTableModel.getRowCount() - 1);
		}
		// 遍历新闻内容列表，将相应的新闻内容显示到表格中
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (News news : newsList) {
			// 按指定的时间格式，获得当前日期
			Date date = new Date();
			String currentDate = sdf.format(date);
			// 将新闻的标题，当前日期，发布日期和作者显示在表格中
			String[] data = new String[] { news.getTitle(), currentDate,
					news.getPubDate(), news.getAuthor() };
			dtmTableModel.addRow(data);
		}
	}
	/**
	 * 把“Wed, 3 Jun 2015 07:59:50 GMT”类型的时间转换为 2015-06-03 15:59:50这种格式
	 * @param date
	 * @return
	 */
	public static String dateChange(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date newDate = new Date(date);
			return sdf.format(newDate);
	}
	/**
	 * 判断时间格式是否正确，例如2015-06-03 15:59:50
	 * @param date
	 * @return
	 */
	public static boolean isTrueDate(String date){
		String regex="^\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}";
		return date.matches(regex)?true:false;
	}
}
