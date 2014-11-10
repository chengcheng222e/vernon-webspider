package com.vernon.webspider.book.service.spider;

import com.vernon.webspider.book.dao.ChapterDao;
import com.vernon.webspider.book.dao.base.DaoFactory;
import com.vernon.webspider.book.domain.Chapter;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/17/13
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
public class ChapterSpiderService {


    /**
     * 获取章节信息
     *
     * @param bookId
     * @param spiderUrl
     * @return
     */
    public Chapter get(int bookId, String spiderUrl) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            ChapterDao dao = session.getMapper(ChapterDao.class);
            return dao.find(bookId, spiderUrl);
        } finally {
            session.close();
        }
    }

    /**
     * 获取可以抓取的章节信息
     *
     * @param pageIndex
     * @param limit
     * @return
     */
    public List<Chapter> getListForSpider(int pageIndex, int limit) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            ChapterDao dao = session.getMapper(ChapterDao.class);
            return dao.getListForSpider(pageIndex * limit, limit);
        } finally {
            session.close();
        }
    }
}
