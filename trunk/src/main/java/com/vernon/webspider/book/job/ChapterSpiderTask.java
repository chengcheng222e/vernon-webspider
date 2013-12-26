package com.vernon.webspider.book.job;

import com.vernon.webspider.book.domain.Chapter;
import com.vernon.webspider.book.extractor.ChapterExtractor;
import com.vernon.webspider.book.extractor.qiqishu.QiqishuChapterExtractor;
import com.vernon.webspider.book.service.spider.ChapterSpiderService;
import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.task.Task;
import com.vernon.webspider.core.util.Constant;
import com.vernon.webspider.core.util.QueuesHolder;
import com.vernon.webspider.core.util.ThreadUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.rmic.iiop.Constants;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class ChapterSpiderTask implements Task {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChapterSpiderTask.class);
    private static ChapterSpiderService chapterSpiderService = new ChapterSpiderService();
    private static final int SIZE = 20;

    @Override
    public void execute() throws Exception {
        LOGGER.info(" chapterSpiderTask start !");

        try {
            List<Chapter> chapterList = chapterSpiderService.getListForSpider(0, SIZE);
            if (chapterList == null || chapterList.size() == 0) {
                LOGGER.info(" chapter is null or size eq 0 ");
                return;
            }
            LOGGER.info(" step 1 chapter list size : " + chapterList.size());
            ChapterExtractor extractor = null;
            for (Chapter chapter : chapterList) {
                if (!chapter.isOnOff()) {
                    LOGGER.info("chapter on_off(" + chapter.isOnOff() + ")!");
                }
                ThreadUtil.sleep(500);
                if (StringUtils.isBlank(chapter.getSpiderUrl())) {
                    LOGGER.info("chapter id : {}, book id: {} has not spider url!", chapter.getChapterId(), chapter.getBookId());
                    continue;
                }

                try {
                    if (chapter.getSpiderSite() == SiteId.QIQISHU.ordinal()) {
                        extractor = new QiqishuChapterExtractor(Charset.GBK.getValue());
                        extractor.loadPageNotProxy(chapter.getSpiderUrl());
                    }
                    extractor.setChapter(chapter);
                    chapter = (Chapter) extractor.extract();
                    if (chapter == null) {
                        continue;
                    }
                } catch (Exception e) {
                    // TODO 记录日志
                    continue;
                }

                if (!QueuesHolder.getQueue(Constant.QUEUE_SPIDER_CHAPTER).contains(chapter)) {
                    QueuesHolder.getQueue(Constant.QUEUE_SPIDER_CHAPTER).offer(chapter);// 放入
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.info(" chapterSpiderTask over !");
    }
}
