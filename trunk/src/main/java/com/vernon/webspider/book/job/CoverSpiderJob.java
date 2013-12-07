package com.vernon.webspider.book.job;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.vernon.webspider.book.domain.Cover;
import com.vernon.webspider.book.extractor.CoverExtractor;
import com.vernon.webspider.core.SpiderJob;
import com.vernon.webspider.core.task.Task;
import com.vernon.webspider.core.task.TaskExecutor;
import com.vernon.webspider.core.util.StringUtil;
import com.vernon.webspider.core.util.ThreadUtil;

/**
 * 封面抓取工作
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public class CoverSpiderJob
		extends SpiderJob {

	private static final Logger LOGGER = Logger.getLogger("CoverSpiderJob");

	private Task spiderCoverTask = new Task() {
		@Override
		public void execute() throws Exception {
			LOGGER.info("spiderCoverTask start!");
			try {
				//bookId,name,filePath,fileName,fileSize,fileType,spiderUrl,spiderState,createTime,checkLevel,checkTime
				Cover cover = new Cover();
				cover.setSpiderUrl("http://www.77shu.com/files/article/image/0/221/221s.jpg");
				CoverExtractor extractor = new CoverExtractor();
				ThreadUtil.sleep(500);
				// 封面抓取地址
				if (StringUtil.isEmpty(cover.getSpiderUrl())) {
					LOGGER.info("book(" + cover.getBookId() + ") cover has not spider url!");
					return;
				}
				// 这里是干嘛???
				extractor.setInputUrl(cover.getSpiderUrl());
				extractor.setCover(cover);
				try {
					cover = extractor.extract();
					if (null == cover) {
						return;
					}
				} catch (MalformedURLException e) {
					LOGGER.error("cover(" + cover.getBookId() + ") from(" + cover.getSpiderUrl() + ") extract failed!",
							e);
					return;
				}

				LOGGER.info("spiderCoverTask over!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private boolean run = false;

	@Override
	protected void doExecute() {
		try {
			if (!run) {
				TaskExecutor.addTask(spiderCoverTask, 10000);// 添加一个任务
				run = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CoverSpiderJob job = new CoverSpiderJob();
		job.doExecute();// 线程
	}

}
