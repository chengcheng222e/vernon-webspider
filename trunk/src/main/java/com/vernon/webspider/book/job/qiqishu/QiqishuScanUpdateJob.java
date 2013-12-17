/**
 * QiqishuScanUpdateJob.java,2010-10-27
 *
 * Copyright 2010 A8 Digital Music Holdings Limited. All Rights Reserved.
 */
package com.vernon.webspider.book.job.qiqishu;

import com.vernon.webspider.book.domain.Book;
import com.vernon.webspider.book.extractor.qiqishu.QiqishuUpdateExtractor;
import com.vernon.webspider.book.service.spider.BookSpiderService;
import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.book.util.TextParseUtil;
import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.LinkDB;
import com.vernon.webspider.core.LinkFilter;
import com.vernon.webspider.core.SpiderJob;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.task.Task;
import com.vernon.webspider.core.task.TaskExecutor;
import com.vernon.webspider.core.util.HtmlParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 亲亲小说网扫描更新工作
 * 
 * @author Vernon.Chen
 *
 */
public class QiqishuScanUpdateJob
		extends SpiderJob {

	private final static Logger LOGGER = LoggerFactory.getLogger(QiqishuScanUpdateJob.class);
	private boolean run = false;
    private BookSpiderService bookSpiderService = new BookSpiderService();

	private Task scanUpdateTask = new Task() {
        private LinkDB linkDB = new LinkDB();

		@Override
		public void execute() throws Exception {
			try {
				LOGGER.info("scanUpdateTask start!");
				// 获取爬取每日更新的url地址
				List<String> spiderUrls = new ArrayList<String>();
				spiderUrls.add("http://www.77shu.com/page_lastupdate_1.html");
				String spiderUrl = spiderUrls.get(0);


				Extractor extractor = new QiqishuUpdateExtractor(Charset.GBK.getValue());
				String text;
				try {
					extractor.loadPageNotProxy(spiderUrl, Charset.GBK);
					text = (String) extractor.extract();
				} catch (Exception e) {
					LOGGER.error(spiderUrl + "extract error!", e);
					return;
				}
				if (null == text || "".equals(text)) {
					LOGGER.error(text + " extract is null!");
					return;
				}
                // LOGGER.info("text:{}",text);
				// 书的地址: http://www.77shu.com/view/12/12442/
				LinkFilter filter = new LinkFilter(SiteId.QIQISHU.getDomain() + "/view/\\d+/\\d+/");
				Set<String> bookUrls = HtmlParserUtil.extractLinks(text, filter, Charset.GBK.getValue());
				LOGGER.info("bookUrls size:" + bookUrls.size());
				for (String bookUrl : bookUrls) {
                    LOGGER.info("bookUrl : {}", bookUrl);
                    linkDB.addUnvisitedUrl(bookUrl);
				}

                Book book;
                String bookUrl;

                while (!linkDB.unVisitedUrlsIsEmpty()) { // 未做处理的才进来
                    bookUrl = linkDB.unVisitedUrlsDeQueue();
                    linkDB.addVisitedUrl(bookUrl);
                    LOGGER.info("update book url : {}", bookUrl);
                    book = bookSpiderService.getBySpiderUrl(bookUrl);
                    if (book != null) {
                        LOGGER.info(" scan book('{}')", book.getAuthorId());
                        // 章节: http://www.77shu.com/view/2/2475/3883029.html
                        String regex = book.getMenuSpiderUrl() + "\\d+.html";
                        // 第一次出现,也就是最新
                        String chapterUrl = TextParseUtil.parse(text, regex, 0, 0);
                        LOGGER.info(" last chapter url: " + chapterUrl);
                        // 判断章节是否存在
                        // 如果存在, 更新说得状态
                    }
                }
				LOGGER.info("scanUpdateTask over!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void doExecute() {
		try {
			if (!run) {
                // 每个10s抓取一次
				TaskExecutor.addTask(scanUpdateTask, 10000);
				scanUpdateTask.execute();
				run = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		QiqishuScanUpdateJob job = new QiqishuScanUpdateJob();
		job.doExecute();
	}
}
