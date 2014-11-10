package com.vernon.webspider.book.service.spider;

import com.vernon.webspider.book.dao.CoverDao;
import com.vernon.webspider.book.dao.base.DaoFactory;
import com.vernon.webspider.book.domain.Cover;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public class CoverSpiderService {

    public Cover get(int bookId) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            CoverDao dao = session.getMapper(CoverDao.class);
            return dao.find(bookId);
        } finally {
            session.close();
        }
    }

    public int create(Cover cover) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            CoverDao dao = session.getMapper(CoverDao.class);
            return dao.save(cover);
        } finally {
            session.close();
        }
    }


    public boolean modifySpiderUrlAndState(int bookId, String spiderUrl, boolean spiderState) {
        SqlSession session = DaoFactory.getSqlSessionFactory().openSession();
        try {
            CoverDao dao = session.getMapper(CoverDao.class);
            return dao.updateSpiderUrlAndState(bookId, spiderUrl, spiderState) > 0 ? true : false;
        } finally {
            session.close();
        }
    }
}
