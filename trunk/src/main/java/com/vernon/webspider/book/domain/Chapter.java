package com.vernon.webspider.book.domain;

import com.vernon.webspider.book.domain.base.BaseChapter;
import com.vernon.webspider.core.util.DateUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Vernon.Chen
 * Date: 13-12-19
 * Time: 下午6:15
 * To change this template use File | Settings | File Templates.
 */
public class Chapter extends BaseChapter{
// ------------------------------ FIELDS ------------------------------

    private String content;
    private String bookName;

    // --------------------- GETTER / SETTER METHODS ---------------------

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // -------------------------- OTHER METHODS --------------------------

    /**
     * 新章节
     *
     * @return boolean
     */
    public boolean isNew() {
        return null != getUpdateTime() && DateUtil.date2String(getUpdateTime(), "MM-dd")
                .equals(DateUtil.date2String(DateUtil.getCurrentDate(), "MM-dd"));
    }

    /**
     * 获取抓取地址缩写
     *
     * @return String
     */
    public String getShortSpiderUrl() {
        if (getSpiderUrl().length() > 28) {
            return StringUtils
                    .replace(getSpiderUrl(), StringUtils.substring(getSpiderUrl(), 15, getSpiderUrl().length() - 10),
                            "...");
        }
        return getSpiderUrl();
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "bookName='" + bookName + '\'' +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}
