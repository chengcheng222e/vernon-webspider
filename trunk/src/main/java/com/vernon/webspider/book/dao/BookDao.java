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
    public Book findBySpiderUrl(@Param("spiderUrl") String spiderUrl);

    /**
     * 修改书的抓取状态
     *
     * @param bookId
     * @param spiderState
     * @return
     */
    public int modifySpiderState(@Param("bookId") int bookId, @Param("spiderState") boolean spiderState);

    /**
     * 保存
     *
     * @param book
     * @return
     */
    public int save(@Param("book") Book book);

    /**
     * 修改
     *
     * @param book
     * @return
     */
    public int update(@Param("book") Book book);
}
