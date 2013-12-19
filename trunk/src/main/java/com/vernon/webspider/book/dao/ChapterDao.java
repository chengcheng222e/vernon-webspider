package com.vernon.webspider.book.dao;

import com.vernon.webspider.book.domain.Chapter;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 * User: Vernon.Chen
 * Date: 13-12-19
 * Time: 下午6:09
 * To change this template use File | Settings | File Templates.
 */
public interface ChapterDao {

    /**
     * 查询 Chapter
     *
     * @param bookId
     * @param spiderUrl
     * @return
     */
    public Chapter find(@Param("bookId") int bookId, @Param("spiderUrl") String spiderUrl);
}
