package com.vernon.webspider.book.extractor.qiqishu;

import com.vernon.webspider.book.domain.Chapter;
import com.vernon.webspider.book.extractor.ChapterExtractor;
import com.vernon.webspider.core.http.Charset;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import org.htmlparser.visitors.ObjectFindingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/26/13
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class QiqishuChapterExtractor extends ChapterExtractor {

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
    private boolean getChapterContent(Chapter chapter) throws ParserException {
        NodeFilter conentFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "content"));
        NodeList contentNodes = this.getParser().parse(conentFilter);
        if (contentNodes.size() > 1) { // 只能找到1个
            LOGGER.info("章节提取出错!");
        }
        String contentHtml = contentNodes.elementAt(0).toHtml();
        String content = this.getFilterText(contentHtml);
        chapter.setContent(content);
        chapter.setLen(content.length());
        chapter.setImg(false);
        return true;
    }

    /**
     * 获取章节标题
     *
     * @param chapter
     * @return
     */
    private boolean getChapterTitle(Chapter chapter) throws ParserException {
        HtmlPage htmlPage = new HtmlPage(this.getParser());
        this.getParser().visitAllNodesWith(htmlPage);
        String title = htmlPage.getTitle();
        chapter.setTitle(title);
        return true;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.77shu.com/view/12/12660/3892792.html";
        QiqishuChapterExtractor extractor = new QiqishuChapterExtractor(Charset.GBK.getValue());
        extractor.loadPageNotProxy(url);
        Chapter chapter = new Chapter();
        extractor.setChapter(chapter);
        chapter = extractor.extract();
        LOGGER.info(chapter.toString());
    }
}
