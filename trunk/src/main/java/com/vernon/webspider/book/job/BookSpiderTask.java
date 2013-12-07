package com.vernon.webspider.book.job;

import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;

import com.vernon.webspider.book.domain.Book;
import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.task.Task;
import com.vernon.webspider.core.util.ThreadUtil;

/**
 * 书籍抓取任务
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public class BookSpiderTask
	implements Task {

	private static final Logger LOGGER = Logger.getLogger("BookSpiderTask");
	private Extractor extractor;

	public BookSpiderTask(Extractor extractor) {
		this.extractor = extractor;
	}

	@Override
	public void execute() throws Exception {
		LOGGER.info("----->BookSpiderTask start!<-----");
		try {
			String visitUrl = "";
			Book book = null;
			LOGGER.info("current visit url: " + visitUrl);
			ThreadUtil.sleep(100);
			try {
				extractor.loadPageNotProxy(visitUrl, Charset.GBK);
				book = (Book) extractor.extract();
			} catch (ParserException e) {
				LOGGER.error("book(" + visitUrl + ") extract failed!");
			}
			LOGGER.info("book(" + book.getSpiderUrl() + ") extract success!");

		} catch (Exception e) {
			LOGGER.error(" execute error! ", e);
		}
	}

	public void setExtractor(Extractor extractor) {
		this.extractor = extractor;
	}
}
