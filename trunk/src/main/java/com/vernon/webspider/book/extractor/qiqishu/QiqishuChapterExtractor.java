package com.vernon.webspider.book.extractor.qiqishu;

import com.vernon.webspider.book.domain.Chapter;
import com.vernon.webspider.book.extractor.ChapterExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/26/13
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class QiqishuChapterExtractor extends ChapterExtractor{

    private final static Logger LOGGER = LoggerFactory.getLogger(QiqishuChapterExtractor.class);

    public QiqishuChapterExtractor(String encoding) {
        super(encoding);
    }

    @Override
    public Chapter extract() throws Exception {
        LOGGER.info(SEPARATOR);
        LOGGER.info(" url : {}", getInputUrl());
        LOGGER.info(" bookId : {}", chapter.getBookId());
        if (!getChapterTitle(chapter)) {
            LOGGER.info(" chapter title is null ");
            return null;
        }
        this.getParser().reset(); // 记得重置
        if (!getChapterContent(chapter)) {
            return null;
        }
        return chapter;
    }


    /**
     * 获取章节内容
     *
     * @param chapter
     * @return
     */
    private boolean getChapterContent(Chapter chapter){
        return false;
    }

    /**
     * 获取章节标题
     *
     * @param chapter
     * @return
     */
    private boolean getChapterTitle(Chapter chapter){
        return false ;
    }
}
