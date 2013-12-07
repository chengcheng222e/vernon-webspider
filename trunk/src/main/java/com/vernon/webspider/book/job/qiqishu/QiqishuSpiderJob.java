package com.vernon.webspider.book.job.qiqishu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vernon.webspider.book.extractor.qiqishu.QiqishuBookExtractor;
import com.vernon.webspider.book.extractor.qiqishu.QiqishuSpiderPageExtractor;
import com.vernon.webspider.book.job.BookSpiderTask;
import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.LinkFilter;
import com.vernon.webspider.core.SpiderJob;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.util.HtmlParserUtil;
import com.vernon.webspider.core.util.ThreadUtil;

/**
 * 亲亲小说网抓取工作
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public class QiqishuSpiderJob
		extends SpiderJob {

	private static final Logger LOGGER = Logger.getLogger("QiqishuSpiderJob");
	private boolean run = false;

	@Override
	protected void doExecute() {
		if (!run) {
			QiqishuBookExtractor extractor = new QiqishuBookExtractor(Charset.GBK.getValue());
			BookSpiderTask bookSpiderTask = new BookSpiderTask(extractor);
//			TaskExecutor.addTask(bookSpiderTask, 5000);
			run = true;
		}
		// QIQISHU 最近更新2页
		List<String> spiderUrls = new ArrayList<String>();
		for (int i = 1 ; i < 3 ; i++) {
			spiderUrls.add(SiteId.QIQISHU.getDomain() + "/page_lastupdate_" + i +".html");
		}
		Extractor extractor = new QiqishuSpiderPageExtractor(Charset.GBK.getValue());
		Integer pageSize = 0;
		for (String spiderUrl : spiderUrls) {
			LOGGER.info("spider url: " + spiderUrl);
			try {
				extractor.loadPageNotProxy(spiderUrl, Charset.GBK);
				pageSize = Integer.parseInt((String) extractor.extract());
			} catch (Exception e) {
				LOGGER.error(spiderUrl + " extract failed!");
			}
		}
		String srcUrl = spiderUrls.get(0);
		srcUrl = srcUrl.substring(0, srcUrl.length() - 1);
		String visitUrl;
		Set<String> links; 
		// http://www.77shu.com/detail_11150.html
		LinkFilter bookFilter = new LinkFilter(SiteId.QIQISHU.getDomain() + "/detail_\\d+.html");
		for (int i = 1; i <= pageSize; i++) {
			visitUrl = srcUrl + i;
			// 获取该页面的所有连接
			links = HtmlParserUtil.extractLinks(visitUrl, bookFilter, Charset.GBK.getValue());
			for (String link : links) {
				LOGGER.info(link);
			}
			ThreadUtil.sleep(1000);
		}
	}

	public static void main(String[] args) {
		QiqishuSpiderJob job = new QiqishuSpiderJob();
		job.doExecute();
	}
}
