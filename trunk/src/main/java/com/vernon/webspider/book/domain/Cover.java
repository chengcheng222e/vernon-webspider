package com.vernon.webspider.book.domain;

import com.vernon.webspider.book.domain.base.BaseCover;
import com.vernon.webspider.core.util.ResourceUtil;

/**
 * 小说封面
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
@SuppressWarnings("serial")
public class Cover
		extends BaseCover {

	/** 书籍名称(后台展示用) */
	private String bookName;

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	/**
	 * 获取封面Url地址
	 *
	 * @return String
	 */
	public final String getUrl() {
		return ResourceUtil.getURL(getFilePath());
	}
}
