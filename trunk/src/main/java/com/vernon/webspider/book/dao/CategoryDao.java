package com.vernon.webspider.book.dao;

import com.vernon.webspider.book.domain.Category;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 * User: Vernon.Chen
 * Date: 13-12-19
 * Time: 下午6:23
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryDao {

    public Category find(@Param("categoryId") int categoryId);
}
