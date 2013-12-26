package com.vernon.webspider.book.dao;

import com.vernon.webspider.book.domain.Chapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 获取章节
     *
     * @param fromIndex
     * @param limit
     * @return
     */
    public List<Chapter> getListForSpider(@Param("pageIndex")int fromIndex, @Param("limit") int limit);
}
