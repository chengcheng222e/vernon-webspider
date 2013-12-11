package com.vernon.webspider.book.extractor.qiqishu;

import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.http.Charset;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * 亲亲小说页面拔取
 * 
 * @author Vernon.Chen
 *
 */
public class QiqishuSpiderPageExtractor
		extends Extractor {

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
			return text;
		} catch (ParserException e) {
			throw new ParserException("解析页码失败!", e);
		}
	}

	public static void main(String[] args) {
		Extractor extractor = new QiqishuSpiderPageExtractor(Charset.UTF8.getValue());
		try {
			extractor.loadPageNotProxy("http://77shu.com/Book/ShowBookList.aspx?page=1", Charset.UTF8);
			extractor.extract();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
