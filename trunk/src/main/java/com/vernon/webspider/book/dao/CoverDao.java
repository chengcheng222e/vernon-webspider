package com.vernon.webspider.book.dao;

import com.vernon.webspider.book.domain.Cover;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
public interface CoverDao {

    public Cover find(@Param("bookId") int bookId);

    public int save(@Param("cover") Cover cover);

    public int updateSpiderUrlAndState(@Param("bookId") int bookId, @Param("spiderUrl") String spiderUrl,
                                       @Param("spiderState") boolean spiderState);
}
