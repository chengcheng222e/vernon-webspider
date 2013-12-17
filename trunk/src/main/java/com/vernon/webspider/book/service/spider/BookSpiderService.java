package com.vernon.webspider.book.service.spider;

import com.vernon.webspider.book.dao.BookDao;
import com.vernon.webspider.book.dao.base.DaoFactory;
import com.vernon.webspider.book.domain.Book;
import org.apache.ibatis.session.SqlSession;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/17/13
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class BookSpiderService {


    /**
     * 根据抓取地址获取书籍信息
     *
     * @param spiderUrl 抓取地址
     * @return Book
     */
    public Book getBySpiderUrl(String spiderUrl) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            BookDao dao = session.getMapper(BookDao.class);
            return dao.findBySpiderUrl(spiderUrl);
        } finally {
            session.close();
        }
    }
}
