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

    /**
     * 修改书的抓取状态
     *
     * @param bookId
     * @param spiderState
     * @return
     */
    public boolean modifySpiderState(int bookId, boolean spiderState) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            BookDao dao = session.getMapper(BookDao.class);
            return dao.modifySpiderState(bookId, spiderState) > 0 ? true : false;
        } finally {
            session.close();
        }
    }

    /**
     * 创建
     *
     * @param book
     * @return
     */
    public int create(Book book) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            BookDao dao = session.getMapper(BookDao.class);
            return dao.save(book);
        } finally {
            session.close();
        }
    }

    /**
     * 修改
     *
     * @param book
     * @return
     */
    public boolean modify(Book book) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            BookDao dao = session.getMapper(BookDao.class);
            return dao.update(book) > 0 ? true : false;
        } finally {
            session.close();
        }
    }

}
