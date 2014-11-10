package com.vernon.webspider.core.util;

import com.vernon.webspider.core.http.Charset;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 2/17/14
 * Time: 13:01
 * To change this template use File | Settings | File Templates.
 */
public class Member21cspExtractor extends com.vernon.webspider.core.Extractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Member21cspExtractor.class);

    public Member21cspExtractor(String encoding) {
        super(encoding);
    }

    @Override
    public Object extract() throws Exception {
        try {
            NodeFilter titleFilter = new AndFilter(new TagNameFilter("table"),
                    new AndFilter(new HasAttributeFilter("width", "764"),
                            new AndFilter(new HasAttributeFilter("cellpadding", "0"),
                                    new HasAttributeFilter("cellspacing", "0"))));
            NodeList titleNodes = this.getParser().parse(titleFilter);
            if (null == titleNodes || titleNodes.size() > 1) {
                return false;
            }
            if (null == titleNodes.elementAt(0)) {
                return false;
            }
            String text = titleNodes.elementAt(0).toHtml();
            Document doc = Jsoup.parse(text);
            Element element = doc.body();
            Elements elements = element.select("tr");
            if (elements != null && elements.size() > 0) {
                for (Element tr : elements) {
                    Elements tds = tr.select("td");
                    if (tds != null && tds.size() > 0) {
                        String[] params = new String[5];
                        for (int j = 0; j < tds.size(); j ++) {
                            params[j] = tds.get(j).text();
                        }
                        LOGGER.info("{}         {}          {}          {}          {}", params);
                    }
                }
            }
            return true;
        } catch (ParserException e) {
            throw new ParserException("解析title失败!", e);
        }
    }

    public static void main(String[] args) throws Exception {
        com.vernon.webspider.core.Extractor extractor = new Member21cspExtractor(Charset.GB2312.getValue());
        for (int page = 1; page <= 117; page ++) {
            extractor.loadPageNotProxy("http://member.21csp.com.cn/Memberlist_" + page + ".html", Charset.GB2312);
            extractor.extract();
        }
        System.out.println("========== OVER ===========");
    }
}
