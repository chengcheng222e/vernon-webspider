package com.vernon.webspider.book.domain;


/**
 * 书籍分类
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
@SuppressWarnings("serial")
public class Category
		extends BaseCategory {

	/**
	 * 构建xml字符串
	 *
	 * @return String
	 */
	public String buildXml() {
		StringBuilder xml = new StringBuilder();
		xml.append("<book_category>");
		xml.append("<category_id>").append(getCategoryId()).append("</category_id>");
		xml.append("<category_title>").append(getTitle()).append("</category_title>");
		xml.append("</book_category>");
		return xml.toString();
	}

	public enum BookCategory {
		NONE, // 没有
		XH, // 玄幻魔法
		WX, // 武侠修真
		YQ, // 都市言情
		LS, // 历史军事
		WY, // 网游动漫

		ZT, // 侦探推理
		LY, // 恐怖灵异
		KH, // 科幻小说
		QT;// 其他
	}
}
