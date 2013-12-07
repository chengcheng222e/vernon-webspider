package com.vernon.webspider.core.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.vernon.webspider.core.LinkFilter;
import com.vernon.webspider.core.http.Charset;

/**
 * 提取需抓取的连接地址
 * 
 * @author Vernon.Chen
 * @date 2012-8-3
 */
public class HtmlParserUtil {
    private final static Logger LOGGER = Logger.getLogger(HtmlParserUtil.class);
    
    /**
     * 获取指定地址或源码上的链接,filter 用来过滤链接
     *
     * @param urlOrSource 地址或源码
     * @param filter      链接过滤
     * @param encoding    编码
     * @return Set<String>
     */
    public static Set<String> extractLinks(String urlOrSource, LinkFilter filter, String encoding) {
        Set<String> links = new HashSet<String>();
        try {
            Parser parser = new Parser(urlOrSource);
            parser.setEncoding(encoding);
            NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
            // 得到所有经过过滤的标签
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);
            for (int i = 0, n = list.size(); i < n; i++) {
                Node tag = list.elementAt(i);
                // <a>标签
                if (tag instanceof LinkTag) {
                    LinkTag link = (LinkTag) tag;
                    // href
                    String linkUrl = link.getLink();
                    if (filter.accept(linkUrl)) {
                        links.add(linkUrl);
                    }
                }
            }
        } catch (ParserException e) {
            LOGGER.error("extractLinks()方法提取链接地址失败!", e);
        }
        return links;
    }


	public static void main(String[] args) {
		String urlOrSource = "http://dzh.mop.com/leftListData.do?rowNum=100&page=0&order=lastReply&mainplate=0";
		String filter = "(?i)http://dzh.mop.com/\\w+/\\d+/\\d+/(.*).shtml";
		Set<String> links = HtmlParserUtil.extractLinks(urlOrSource, new LinkFilter(filter), Charset.UTF8.toString());

		LOGGER.info("links size: " + links.size());
		for (String link : links) {
			System.out.println(link);
		}
	}
}
