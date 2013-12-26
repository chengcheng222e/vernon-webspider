package com.vernon.webspider.book.job;

import com.vernon.webspider.book.domain.Chapter;
import com.vernon.webspider.book.service.spider.ChapterSpiderService;
import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.core.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        List<Chapter> chapterList = chapterSpiderService.getListForSpider(0, SIZE);
        if (chapterList == null || chapterList.size() == 0) {
            LOGGER.info(" chapter is null or size eq 0 ");
            return;
        }
        LOGGER.info(" step 1 chapter list size : " + chapterList.size());

        for (Chapter chapter : chapterList) {
            if (!chapter.isOnOff()) {
                LOGGER.info("chapter on_off(" + chapter.isOnOff() + ")!");
            }


        }

        LOGGER.info(" chapterSpiderTask over !");
    }
}
