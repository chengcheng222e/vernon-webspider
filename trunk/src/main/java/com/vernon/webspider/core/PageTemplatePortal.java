package com.vernon.webspider.core;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class PageTemplatePortal
		extends AbstractSpider {
	List<String> listNews = null;

	public PageTemplatePortal(String urlAddress) {
		super(urlAddress);
		listNews = new ArrayList<String>();
	}

	@Override
	public void parserMsg(String pageInfo) {
		String content = spiderContent(pageInfo);
		listNews = extractLinkUrl(content);
		setListNews(listNews);
	}

	/**
	 * 解析列表页内容
	 *
	 * @param pageInfo 列表页地址url
	 * @return String
	 */
	@Override
	public String spiderContent(String pageInfo) {
		return null;
	}

	/**
	 * 提取模板页中的url地址，即待抓取新闻的地址列表
	 *
	 * @param str 模板内容
	 * @return List<String> url地址列表
	 */
	public List<String> extractLinkUrl(final String str) {
		List<String> listNews = new ArrayList<String>();
		Parser parser = null;
		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList nodelist = null;

		try {
			parser = new Parser(str);
			parser.setEncoding("GBK");
		} catch (ParserException e1) {
			e1.printStackTrace();
		}

		if (null != parser) {
			try {
				nodelist = parser.extractAllNodesThatMatch(filter);
			} catch (ParserException e) {
				e.printStackTrace();
			}

			if (null != nodelist && nodelist.size() > 0) {
				for (int i = 0; i < nodelist.size(); i++) {
					LinkTag linkTag = (LinkTag) nodelist.elementAt(i);
					String linkUrl = linkTag.getLink();
					if (linkUrl.startsWith("/") && !linkUrl.equalsIgnoreCase(getUrlWrap().getPath())) {
						String linkTrueUrl = getUrlWrap().getProtocol() + "://" + getUrlWrap().getHost() + linkUrl;
						if (!listNews.contains(linkTrueUrl)) {
							listNews.add(linkTrueUrl);
						}
					}
					if (linkUrl.startsWith("http") && !linkUrl.equalsIgnoreCase(getUrlAddress())
							&& (linkUrl.endsWith("htm") || linkUrl.endsWith("html"))) {
						if (!listNews.contains(linkUrl)) {
							listNews.add(linkUrl);
						}
					}
					if ((linkUrl.matches("\\d{1,}\\.\\w+") || linkUrl.matches("[a-zA-Z]\\d{1,}\\.\\w+"))
							&& (linkUrl.endsWith("html") || linkUrl.endsWith("htm"))) {
						if (!listNews.contains(linkUrl)) {
							String linkTrueUrl = getUrlWrap().getURLBase() + linkUrl;
							listNews.add(linkTrueUrl);
						}
					}
				}

			}
		}

		return listNews;
	}

	public List<String> getListNews() {
		return listNews;
	}

	public void setListNews(List<String> listNews) {
		this.listNews = listNews;
	}
}
