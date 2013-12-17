package com.vernon.webspider.book.dao;

import com.vernon.webspider.book.domain.Book;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/17/13
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
public interface BookDao {

    /**
     * 根据书的URL查询书的信息
     *
     * @param spiderUrl
     * @return
     */
    public Book findBySpiderUrl(@Param("spiderUrl") String spiderUrl) ;


}
