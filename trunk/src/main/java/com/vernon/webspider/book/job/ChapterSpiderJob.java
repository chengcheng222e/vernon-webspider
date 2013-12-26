package com.vernon.webspider.book.job;

import com.vernon.webspider.core.SpiderJob;
import com.vernon.webspider.core.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/20/13
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ChapterSpiderJob extends SpiderJob {

    private final static Logger logger = LoggerFactory.getLogger(ChapterSpiderJob.class);

    private Task chapterSpiderTask = new ChapterSpiderTask();
    private Task chapterPersistTask = new ChapterPersistTask();


    @Override
    protected void doExecute() {

    }
}
