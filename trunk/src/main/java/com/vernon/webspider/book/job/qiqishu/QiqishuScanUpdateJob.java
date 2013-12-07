/**
 * QiqishuScanUpdateJob.java,2010-10-27
 *
 * Copyright 2010 A8 Digital Music Holdings Limited. All Rights Reserved.
 */
package com.vernon.webspider.book.job.qiqishu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vernon.webspider.book.extractor.qiqishu.QiqishuUpdateExtractor;
import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.LinkFilter;
import com.vernon.webspider.core.SpiderJob;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.task.Task;
import com.vernon.webspider.core.task.TaskExecutor;
import com.vernon.webspider.core.util.HtmlParserUtil;

/**
 * 亲亲小说网扫描更新工作
 * 
 * @author Vernon.Chen
 *
 */
public class QiqishuScanUpdateJob
		extends SpiderJob {

	private final static Logger LOGGER = Logger.getLogger("QiqishuScanUpdateJob");
	private boolean run = false;

	private Task scanUpdateTask = new Task() {
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
				// http://www.77shu.com/view/10/10350/3254630.html
				LinkFilter filter = new LinkFilter("http://www.77shu.com/view/\\d+/\\d+/\\d+\\.html");
				Set<String> bookUrls = HtmlParserUtil.extractLinks(text, filter, Charset.GBK.getValue());
				LOGGER.info("bookUrls size:" + bookUrls.size());
				for (String bookUrl : bookUrls) {
					LOGGER.info("bookUrl is:" + bookUrl);
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
