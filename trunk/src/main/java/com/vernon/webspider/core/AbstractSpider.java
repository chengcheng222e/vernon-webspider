package com.vernon.webspider.core;

import com.vernon.webspider.core.collection.Pair;
import com.vernon.webspider.core.http.Browser;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.http.HttpClientUtil;
import com.vernon.webspider.core.http.URLWrap;

import java.net.MalformedURLException;

/**
 * 抓取抽象类
 * 
 * @author Vernon.Chen
 * @date 2012-8-3
 */
public abstract class AbstractSpider
	implements ISpider {

	protected String urlAddress = null;
	protected URLWrap urlWrap = null;
	int timeout = 30000;

	public AbstractSpider(String urlAddress) {
		this.urlAddress = urlAddress;
		try {
			urlWrap = new URLWrap(urlAddress);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取UrlWrap包裹的地址页源码
	 *
	 * @param charset 编码
	 * @param browser 浏览器
	 * @return String
	 */
	@Override
	public String spiderPageInfo(Charset charset, Browser browser) {
		String pageInfo = "";

		if (charset == null) {
			charset = Charset.UTF8;
		}
		if (browser == null) {
			browser = Browser.IE7_ON_WINXP;
		}

		if (urlWrap != null) {
			Pair<Integer, String> page = HttpClientUtil.getStringByGET(urlWrap, charset, browser, timeout);
			if (page != null && 200 == page.getKey()) {
				pageInfo = page.getValue();
			}
		}

		return pageInfo;
	}

	/**
	 * 获取urlAddress地址页源码
	 *
	 * @param urlAddress url地址
	 * @param charset    编码
	 * @param browser    浏览器
	 * @return String
	 */
	public String spiderPageInfo(String urlAddress, Charset charset, Browser browser) {
		String pageInfo = "";
		URLWrap wrap = null;
		if (charset == null) {
			charset = Charset.UTF8;
		}
		if (browser == null) {
			browser = Browser.IE7_ON_WINXP;
		}

		try {
			wrap = new URLWrap(urlAddress);
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		}
		if (wrap != null) {
			Pair<Integer, String> page = HttpClientUtil.getStringByGET(wrap, charset, browser, timeout);
			if (page != null && 200 == page.getKey()) {
				pageInfo = page.getValue();
			}
		}

		return pageInfo;
	}

	public URLWrap getUrlWrap() {
		return urlWrap;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	//获取内容
	public abstract String spiderContent(String pageInfo);

	//解析html消息
	public abstract void parserMsg(String pageInfo);
}
