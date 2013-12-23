package com.vernon.webspider.book.service.spider;

import com.vernon.webspider.book.dao.AuthorDao;
import com.vernon.webspider.book.dao.base.DaoFactory;
import com.vernon.webspider.book.domain.Author;
import org.apache.ibatis.session.SqlSession;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class AuthorSpiderService {

    /**
     * 根据名字获取作者
     *
     * @param name
     * @return
     */
    public Author getByName(String name) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            AuthorDao dao = session.getMapper(AuthorDao.class);
            return dao.findByName(name);
        } finally {
            session.close();
        }
    }

    /**
     * 创建作者
     *
     * @param name
     * @param type
     * @return
     */
    public int create(String name, Author.AuthorType type) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            AuthorDao dao = session.getMapper(AuthorDao.class);
            return dao.save(name, type);
        } finally {
            session.close();
        }
    }
}
