/**
 * ChapterUrlSpiderTask.java,2010-10-27
 *
 * Copyright 2010 A8 Digital Music Holdings Limited. All Rights Reserved.
 */
package com.vernon.webspider.book.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vernon.webspider.book.domain.Book;
import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.core.LinkFilter;
import com.vernon.webspider.core.task.Task;

/**
 * 小说链接地址抓取
 * 
 * @author Vernon.Chen
 *
 */
public class ChapterUrlSpiderTask
	implements Task {

	private static final Logger LOGGER = Logger.getLogger("ChapterUrlSpiderTask");
	private SiteId site;

	public ChapterUrlSpiderTask(SiteId site) {
		this.site = site;
	}

	@Override
	public void execute() throws Exception {
		try {
			LOGGER.info("chapterUrlSpiderPersistTask start!");
			List<Book> bookList = new ArrayList<Book>();
			
			//TODO
		
			boolean result;
			Book book = bookList.get(0);
			LOGGER.info(" step 1: get spider book id: " + book.getBookId());
			String chapterSpiderUrl = book.getMenuSpiderUrl();
			if (null == chapterSpiderUrl) {
				LOGGER.info("menu url is null!");
				return;
			}
			LOGGER.info(" step 2: chapter spider url: " + chapterSpiderUrl + ", book id: " + book.getBookId());
			String regex;
			LinkFilter filter;
			Set<String> chapterSpiderUrls;
			if (site.equals(SiteId.QIQISHU)) {
				regex = "(?i)\\d+\\.shtml";
				filter = new LinkFilter(regex);
				regex = "http://www.77shu.com/html/book/\\d+/\\d+/(\\d+).shtml";
			}
			String chapterUrl;
			LOGGER.info("chapterUrlSpiderPersistTask over!");
		} catch (Exception e) {
			LOGGER.info("doExecute() error!", e);
		}
	}

}
