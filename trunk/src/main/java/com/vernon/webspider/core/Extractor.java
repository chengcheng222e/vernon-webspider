package com.vernon.webspider.core;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import com.vernon.webspider.core.collection.Pair;
import com.vernon.webspider.core.http.Browser;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.http.HttpClientUtil;
import com.vernon.webspider.core.http.URLWrap;
import com.vernon.webspider.core.util.BCConvertUtil;
import com.vernon.webspider.core.util.TextFilterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网页信息解析基类
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public abstract class Extractor {

	/** 分隔符 */
	public static final String SEPARATOR = "==========================";

	/** 换行 */
	public static final String NEW_LINE = "\r\n";

	/** 当前正在处理的url */
	private String inputUrl;

	/** HtmlParser实例 */
	private Parser parser;

	/** 编码 */
	private String encoding;

    private static final Logger logger = LoggerFactory.getLogger(Extractor.class);

	protected Extractor() {
		this.encoding = Charset.GBK.getValue();
	}

	protected Extractor(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 获取正在处理的文件的路径
	 *
	 * @return String
	 */
	public String getInputUrl() {
		return inputUrl;
	}

	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}

	public Parser getParser() {
		return parser;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 抽象方法,供子类实现解析网页
	 *
	 * @return Object
	 * @throws Exception Exception
	 */
	public abstract Object extract() throws Exception;

	/**
	 * 装载需要的网页
	 *
	 * @param url 网页地址
	 * @throws org.htmlparser.util.ParserException 页面解析失败
	 */
	public void loadPageNotProxy(String url) throws ParserException {
		try {
			inputUrl = url;
			parser = new Parser(url);
			parser.setEncoding(encoding);
		} catch (ParserException e) {
			throw new ParserException("装载网页地址失败!" + e.getMessage());
		}
	}

	/**
	 * 装载需要的网页
	 *
	 * @param url 网页地址
	 * @param charset 编码
	 * @throws org.htmlparser.util.ParserException 页面解析失败
	 */
    public void loadPageNotProxy(String url, Charset charset) throws ParserException {
        try {
            inputUrl = url;
            URLWrap wrap = new URLWrap(getInputUrl());
            Pair<Integer, String> pair = HttpClientUtil.getStringByGET(wrap, charset, Browser.FIREFOX_ON_WINXP, 10000);
            if (null != pair && 200 == pair.getKey()) {
                String result = pair.getValue();
                loadSourceCode(result);
            } else {
                logger.error("loadPageNotProxy error : pair.getKey() = {}, url = {}", pair.getKey(), inputUrl);
            }
        } catch (ParserException e) {
            throw new ParserException("装载网页地址失败!" + e.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 装载需要的网页
	 *
	 * @param url 网页地址
	 * @param charset 编码
	 * @param cookies cookie值
	 * @throws org.htmlparser.util.ParserException 页面解析失败
	 */
	public void loadPageNotProxy(String url, String cookies, Charset charset) throws ParserException {
		try {
			inputUrl = url;
			URLWrap wrap = new URLWrap(getInputUrl());
			Pair<Integer, String> pair = HttpClientUtil.getStringByGet(wrap, cookies, charset,
					Browser.FIREFOX_ON_WINXP, 6000);
			if (null != pair && 200 == pair.getKey()) {
				String result = pair.getValue();
				loadSourceCode(result);
			}
		} catch (ParserException e) {
			throw new ParserException("装载网页地址失败!" + e.getMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 装载源码
	 *
	 * @param sourceCode 源代码
	 * @throws org.htmlparser.util.ParserException 页面解析失败
	 */
	public void loadSourceCode(String sourceCode) throws ParserException {
		try {
			parser = new Parser(sourceCode);
			parser.setEncoding(encoding);
		} catch (ParserException e) {
			throw new ParserException("装载源码失败!" + e.getMessage());
		}
	}

	/**
	 * 获取过滤后的内容
	 *
	 * @param source 源字符串
	 * @return String
	 */
	protected String getFilterText(String source) {
		if (null == source || source.length() == 0) {
			return "";
		}
		String text = source.replaceAll("(?i)<\\s*br\\s*/\\s*>", "///").replaceAll("(?i)<\\s*br\\s*>", "///")
				.replaceAll("//////", "///").replaceAll("<p>", "///");
		text = BCConvertUtil.SBC2DBC(text);
		text = TextFilterUtil.filterHTML(text);
		text = text.replace("&nbsp;", "").replace(NEW_LINE, "").replace("\r", "").replace("\n", "").trim();
		return text;
	}

	/**
	 * 使用正则表达式来匹配并获取网页中的字符串
	 *
	 * @param regex    表达式
	 * @param inputStr 输入内容
	 * @param index    组号
	 * @return String
	 */
	protected String getProp(String regex, String inputStr, int index) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.find()) {
			return matcher.group(index);
		}
		return "";
	}
}
