package com.vernon.webspider.book.extractor.qiqishu;

import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.http.Charset;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 亲亲小说页面拔取
 * 
 * @author Vernon.Chen
 *
 */
public class QiqishuSpiderPageExtractor
		extends Extractor {

    private static final Logger logger = LoggerFactory.getLogger(QiqishuSpiderPageExtractor.class);

	public QiqishuSpiderPageExtractor(String encoding) {
		super(encoding);
	}

	@Override
	public Object extract() throws ParserException {
		// <td class="mypager" valign="bottom" align="left" nowrap="true" width="40%">
		try {
			NodeFilter fontFilter = new AndFilter(
					new TagNameFilter("td"), 
					new AndFilter(
							new HasAttributeFilter("class", "mypager"), 
							new AndFilter(
									new HasAttributeFilter("align", "left"), 
									new HasAttributeFilter("width", "40%")
							)
					)
			);
			NodeList fontNodes = this.getParser().parse(fontFilter);
			if (null == fontNodes || fontNodes.size() > 1) {
				return null;
			}
			if (null == fontNodes.elementAt(0)) {
				return null;
			}
			String text = fontNodes.elementAt(0).toHtml();
			text = this.getProp("<font color=blue><b>(\\d+)</b></font>", text, 1);

            logger.info("text : " + text);
			return text;
		} catch (ParserException e) {
			throw new ParserException("解析页码失败!", e);
		}
	}

    public static void main(String[] args) {
        QiqishuSpiderPageExtractor extractor = new QiqishuSpiderPageExtractor(Charset.GBK.getValue());

//		try {
//			extractor.loadPageNotProxy("http://www.77shu.com/page_lastupdate_1.html", Charset.GBK);
//			extractor.extract();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        String regex = "<font color=blue><b>(\\d+)</b></font>";
        String text = "<font color=blue><b>23</b></font>12313123131241241<font color=blue><b>88</b></font>";
        text = extractor.getProp(regex, text, 1);
        System.out.println(text);
    }

}
