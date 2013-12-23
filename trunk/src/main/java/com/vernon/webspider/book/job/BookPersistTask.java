package com.vernon.webspider.book.job;

import com.vernon.webspider.book.domain.Author;
import com.vernon.webspider.book.domain.Book;
import com.vernon.webspider.book.domain.Cover;
import com.vernon.webspider.book.service.spider.AuthorSpiderService;
import com.vernon.webspider.book.service.spider.BookSpiderService;
import com.vernon.webspider.book.service.spider.CoverSpiderService;
import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.core.task.Task;
import com.vernon.webspider.core.util.Constant;
import com.vernon.webspider.core.util.QueuesHolder;
import com.vernon.webspider.core.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/20/13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class BookPersistTask implements Task {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookPersistTask.class);
    private SiteId siteId;
    private static AuthorSpiderService authorSpiderService = new AuthorSpiderService();
    private static BookSpiderService bookSpiderService = new BookSpiderService();
    private static CoverSpiderService coverSpiderService = new CoverSpiderService();

    public BookPersistTask(SiteId siteId) {
        this.siteId = siteId;
    }

    @Override
    public void execute() throws Exception {
        try {
            LOGGER.info("BookPersistTask start!");
            if (QueuesHolder.getQueue(Constant.QUEUE_SPIDER_BOOK_INFO).isEmpty()) {
                return;
            }
            Book book = (Book) QueuesHolder.getQueue(Constant.QUEUE_SPIDER_BOOK_INFO).remove();
            if (book == null || StringUtils.isBlank(book.getTitle())) {
                return;
            }
            LOGGER.info("step 1: get book({}) from queue success!", book.getSpiderUrl());
            int authorId;
            Author author = authorSpiderService.getByName(book.getAuthorName());
            if (author == null) {
                // 作者存库
                authorId = authorSpiderService.create(book.getAuthorName(), Author.AuthorType.HOMELESS);
            } else {
                authorId = author.getAuthorId();
            }
            LOGGER.info("step 2: author({}) deal success!", authorId);
            Book dbBook = bookSpiderService.getBySpiderUrl(book.getSpiderUrl());
            if (dbBook == null) {
                LOGGER.info("step 3: get book from db by spiderUrl: {} is null!", book.getSpiderUrl());

                LOGGER.info("step 4: no img deal! ");
                int result = bookSpiderService.create(book);
                if (result > 0) {
                    LOGGER.info("step 5: persist book, book id : {}, from url : {} success! ", result, book.getSpiderUrl());
                    if (StringUtils.isNotBlank(book.getImgUrl()) && coverSpiderService.get(result) == null) {
                        //
                        Cover cover = new Cover();
                        coverSpiderService.create(cover);
                    }
                } else {
                    LOGGER.info(" step 5: persist book from url : {} failed! ", book.getSpiderUrl());
                }
            } else {
                LOGGER.info("step 3: get book from db by spiderUrl: {} is exists!", book.getSpiderUrl());
                // 关闭抓取的情况
                if (!dbBook.isOnOff()) {
                    return;
                }
                String dbMd5 = "";
                LOGGER.info("db book : {}, md5: {}", dbBook.getBookId(), dbMd5);
                String spiderMd5 = "";
                LOGGER.info("spider md5: " + spiderMd5);
                if (!StringUtil.equals(dbMd5, spiderMd5)) {
                    LOGGER.info("step 4: no img deal!");
                    boolean result = bookSpiderService.modify(book);
                    if (result) {
                        LOGGER.info(" step 5: update book from url {} success! ", book.getSpiderUrl());
                        Cover cover = coverSpiderService.get(dbBook.getBookId());
                        if (null != cover && null != book.getImgUrl() && !cover.getSpiderUrl().equalsIgnoreCase(
                                book.getImgUrl())) {
                            coverSpiderService.modifySpiderUrlAndState(dbBook.getBookId(), book.getImgUrl(), true);
                        }
                    } else {
                        LOGGER.info(" step 5: update book from url {} failed! ", book.getSpiderUrl());
                    }
                } else {
                    LOGGER.info(" step 4: the book has not update! from url: " + book.getSpiderUrl());
                }
            }
            LOGGER.info("bookPersistTask over!");
        } catch (Exception e) {
            LOGGER.error("execute error!", e);
        }
    }

    // ------------------ setter / getter methods -----------------

    public SiteId getSiteId() {
        return siteId;
    }

    public void setSiteId(SiteId siteId) {
        this.siteId = siteId;
    }
}
