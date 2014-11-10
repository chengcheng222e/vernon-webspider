package com.vernon.webspider.core.util;

import com.vernon.webspider.core.Extractor;
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
public class GzlyMiitGovCnExtractor1 extends Extractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GzlyMiitGovCnExtractor1.class);

    public GzlyMiitGovCnExtractor1(String encoding) {
        super(encoding);
    }

    @Override
    public Object extract() throws Exception {
        try {
            NodeFilter titleFilter = new AndFilter(new TagNameFilter("table"), new HasAttributeFilter("class", "list-table"));
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
                        String[] params = new String[7];
                        for (int j = 0; j < tds.size(); j ++) {
                            params[j] = tds.get(j).text();
                        }
                        LOGGER.info("{}         {}          {}          {}          {}          {}          {}", params);
                    }
                }
            }
            return true;
        } catch (ParserException e) {
            throw new ParserException("解析title失败!", e);
        }
    }

    public static void main(String[] args) throws Exception {
        Extractor extractor = new GzlyMiitGovCnExtractor1(Charset.UTF8.getValue());
        for (int page = 0; page <= 95; page ++) {
            extractor.loadPageNotProxy("http://gzly.miit.gov.cn:8080/datainfo/miit/miit_jczzqymd.jsp?page=" + page, Charset.UTF8);
            extractor.extract();
        }
    }
}
