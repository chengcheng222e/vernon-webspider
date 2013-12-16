/**
 * QiqishuUpdateExtractor.java,2010-10-28
 *
 * Copyright 2010 A8 Digital Music Holdings Limited. All Rights Reserved.
 */
package com.vernon.webspider.book.extractor.qiqishu;

import java.util.Date;

import com.vernon.webspider.core.util.DateUtil;
import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.vernon.webspider.core.Extractor;

/**
 * 亲亲小说网每日更新页解析
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public class QiqishuUpdateExtractor
		extends Extractor {

	private static final Logger LOGGER = Logger.getLogger(QiqishuUpdateExtractor.class);

	public QiqishuUpdateExtractor(String encoding) {
		super(encoding);
	}

	@Override
	public String extract() throws ParserException {
        NodeFilter filter = new AndFilter(
                new TagNameFilter("div"),
                new HasAttributeFilter("class", "book_list")
        );
        NodeList nodes = this.getParser().parse(filter);
		if (null == nodes || nodes.size() > 1) {
			LOGGER.info("提取最近更新div失败");
			return null;
		}
		String divHtml = nodes.elementAt(0).toHtml();
		this.loadSourceCode(divHtml);
		filter = new TagNameFilter("li");
		nodes = this.getParser().parse(filter);
		if (null == nodes) {
			LOGGER.info("提取最近更新div失败");
			return null;
		}
		StringBuilder text = new StringBuilder();
		String now = DateUtil.date2String(new Date(), "MM-dd");
		for (int i = 0, n = nodes.size(); i < n; i++) {
			String nodeHtml = nodes.elementAt(i).toHtml();
			if (nodeHtml.contains(now)) {
				text.append(nodeHtml).append("\n");
			}
		}
		return text.toString();
	}

}
