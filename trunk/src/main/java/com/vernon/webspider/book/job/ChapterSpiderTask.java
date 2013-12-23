package com.vernon.webspider.book.job;

import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.core.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class ChapterSpiderTask implements Task {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChapterSpiderTask.class);

    @Override
    public void execute() throws Exception {
        LOGGER.info(" chapterSpiderTask start !");



        LOGGER.info(" chapterSpiderTask over !");
    }
}
