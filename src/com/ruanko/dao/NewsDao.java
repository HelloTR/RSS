package com.ruanko.dao;

import java.util.List;

import com.ruanko.model.News;

public interface NewsDao {

	public boolean saveNews(List<News> newsList);
}
