package com.vernon.webspider.book.job;

import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.task.Task;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class ChapterPersistTask implements Task {

    private static final Logger LOGGER = Logger.getLogger(ChapterPersistTask.class);

    @Override
    public void execute() throws Exception {
        LOGGER.info(" chapterPersistTask start !");



        LOGGER.info(" chapterPersistTask over !");

    }
}
