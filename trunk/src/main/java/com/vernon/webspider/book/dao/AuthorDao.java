package com.vernon.webspider.book.dao;

import com.vernon.webspider.book.domain.Author;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public interface AuthorDao {

    public Author findByName(@Param("name") String name);

    /**
     * 存储作者,并且返回authorId
     *
     * @param name
     * @param type
     * @return
     */
    public int save(@Param("name") String name, @Param("type") Author.AuthorType type);

}
