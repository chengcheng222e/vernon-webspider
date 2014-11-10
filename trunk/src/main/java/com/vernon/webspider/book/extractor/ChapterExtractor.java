package com.vernon.webspider.book.extractor;

import com.vernon.webspider.book.domain.Chapter;
import com.vernon.webspider.core.Extractor;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/26/13
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public class ChapterExtractor extends Extractor {

    protected Chapter chapter;

    public ChapterExtractor() {
    }

    public ChapterExtractor(String encoding) {
        super(encoding);
    }

    @Override
    public Object extract() throws Exception {
        return null;
    }


    // --------------------------- setter / getter methods ---------------------

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}
