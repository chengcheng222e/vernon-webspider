package com.vernon.webspider.book.extractor.qiqishu;

import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.vernon.webspider.book.domain.Book;
import com.vernon.webspider.book.util.SiteId;
import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.http.Charset;

/**
 * 亲亲小说抓取
 * 
 * @author Vernon.Chen
 * @date 2012-8-8
 */
public class QiqishuBookExtractor
		extends Extractor {
	private static final Logger LOGGER = Logger.getLogger(QiqishuBookExtractor.class);

	public QiqishuBookExtractor(String encoding) {
		super(encoding);
	}

	// 抓取一本完整的书
	@Override
	public Book extract() throws ParserException {
		Book book = new Book();
		book.setSpiderUrl(getInputUrl());
		book.setSpiderSite(SiteId.QIQISHU.ordinal());
		if (!getBookTitle(book)) {
			return null;
		}
		this.getParser().reset();
		getBookImgUrl(book);// 封面地址
		this.getParser().reset();
		getBookIntro(book);// 书籍信息
		this.getParser().reset();
		if (!getBookInfo(book)) {
			return null;
		}
		return book;
	}

	/**
	 * 解析书籍封面地址
	 *
	 * @param book 书籍对象
	 * @throws ParserException ParserException
	 */
	private void getBookImgUrl(Book book) throws ParserException {
		//        <img width="100" vspace="5" hspace="5" height="125" border="0" align="right" src="http://www.77shu.com/files/article/image/9/9702/9702s.jpg">
		try {
			NodeFilter imgFilter = new AndFilter(new TagNameFilter("img"), new AndFilter(new HasAttributeFilter(
					"vspace", "5"), new AndFilter(new HasAttributeFilter("hspace", "5"), new HasAttributeFilter(
					"height", "125"))));
			NodeList imgNodes = this.getParser().parse(imgFilter);
			if (null == imgNodes || imgNodes.size() > 1) {
				book.setImgUrl(null);
				return;
			}
			if (null == imgNodes.elementAt(0)) {
				return;
			}
			String text = imgNodes.elementAt(0).toHtml();
			String imgUrl = this
					.getProp(
							"<img src=\"(.*)\" border=\"0\" width=\"100\" height=\"125\" align=\"right\" hspace=\"5\" vspace=\"5\" />",
							text, 1);
			if (null == imgUrl || "".equals(imgUrl) || imgUrl.equalsIgnoreCase("/images/noimg.gif")
					|| imgUrl.equals("img/noimg.gif")) {
				book.setImgUrl(null);
				LOGGER.info("book has not img!");
			} else {
				book.setImgUrl(imgUrl.trim());
				LOGGER.info("book imgUrl:" + book.getImgUrl());
			}
		} catch (ParserException e) {
			throw new ParserException("解析imgUrl失败!", e);
		}
	}

	/**
	 * 解析书籍信息
	 *
	 * @param book Book
	 * @return boolean
	 * @throws ParserException ParserException
	 */
	private boolean getBookInfo(Book book) throws ParserException {
		try {
			//        <div id=xinxi>
			NodeFilter divFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "xinxi"));
			NodeList nodes = this.getParser().parse(divFilter);
			if (null == nodes || nodes.size() > 1) {
				return false;
			}
			if (null == nodes.elementAt(0)) {
				return false;
			}
			loadSourceCode(nodes.elementAt(0).toHtml());
			NodeFilter liFilter = new TagNameFilter("li");
			nodes = this.getParser().parse(liFilter);
			for (int i = 0, n = nodes.size(); i < n; i++) {
				//            LOGGER.info(nodes.elementAt(i).toHtml());
				String text = nodes.elementAt(i).toHtml();
				switch (i) {
				case 0: {
					//                    <li><label>书籍作者</label>张建邦</li>
					book.setAuthorName(this.getProp("<li><label>书籍作者</label>(.*)</li>", text, 1).trim());
					//                        LOGGER.info("book author: " + book.getAuthorName());
					break;
				}
				case 1: {
					//                    <li><label>写作进程</label>连载</li>
					String bookStatus = this.getProp("<li><label>写作进程</label>(.*)</li>", text, 1);
					if (bookStatus.contains("完结")) {
						book.setState(Book.State.OVER.ordinal());
					} else if (bookStatus.contains("暂停")) {
						book.setState(Book.State.STOP.ordinal());
					} else {
						book.setState(Book.State.WRITING.ordinal());
					}
					//                        LOGGER.info("book state: " + book.getState());
					break;
				}
				case 3: {
					//                    <li><label>作品大类</label>未知</li>
					book.setCategoryTitle(this.getProp("<li><label>作品大类</label>(.*)</li>", text, 1).trim());
					//                        LOGGER.info("book category: " + book.getCategoryTitle());
					break;
				}
				case 4: {
					//                    <li><label>作品小类</label>未知</li>
					String tag = this.getProp("<li><label>作品小类</label>(.*)</li>", text, 1).trim();
					if ("未知".equals(tag)) {
						book.setTag(book.getCategoryTitle());
					} else {
						book.setTag(tag);
					}
					//                        LOGGER.info("book tag: " + book.getTag());
					break;
				}
				case 8: {
					//                    <li><label>完成字数</label>1163096</li>
					String len = this.getProp("<li><label>完成字数</label>(\\d+)</li>", text, 1).trim();
					book.setLen(Integer.parseInt(len));
					//                        LOGGER.info("book len: " + book.getLen());
					break;
				}
				}
			}
		} catch (ParserException e) {
			throw new ParserException("解析book信息失败", e);
		} catch (NumberFormatException e) {
			book.setLen(0);
		}
		return true;
	}

	/**
	 * 解析书籍简介
	 *
	 * @param book 书籍对象
	 * @throws ParserException ParserException
	 */
	private void getBookIntro(Book book) throws ParserException {
		//        <div id=jianjie>
		//            浩如烟海的岁月中，有多少古老的秘密深邃而又静默地存在着，存在于心灵未曾涉及的“死角”，存在于视野难以窥探的“黑暗”。探寻这些“谜团”的路途，或许异常艰难危险，可当你切实触摸到它们的一瞬间，终将“洞悉真实的本质，超越平庸的凡俗，体验极至的伟岸”。
		//            本书为《鬼吹灯》的姊妹篇，感谢读者的支持！
		//
		//        <p><strong>最新章节：</strong><a href="/Html/Book/96/96502/4332362.shtml">4.3 6无限接近1</a> 2010-10-27 8:36:17</p></div>
		try {
			NodeFilter introFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "jianjie"));
			NodeList introNodes = this.getParser().parse(introFilter);
			if (null == introNodes || introNodes.size() > 1) {
				book.setIntro(null);
				return;
			}
			if (null == introNodes.elementAt(0)) {
				book.setIntro(null);
				return;
			}
			String intro = introNodes.elementAt(0).toHtml();
			intro = intro.replaceAll("<p>(.*)", "");
			intro = intro.replaceAll("<div id=jianjie>", "");
			intro = getFilterText(intro);
			if (intro.length() > 1500) {
				intro = intro.substring(0, 1500);
			}
			book.setIntro(intro);
			//            LOGGER.info("book intro: " + book.getIntro());
		} catch (ParserException e) {
			throw new ParserException("解析intro失败!", e);
		}
	}

	/**
	 * 解析书籍标题
	 *
	 * @param book 书籍对象
	 * @return boolean
	 * @throws ParserException ParserException
	 */
	private boolean getBookTitle(Book book) throws ParserException {
		//<td width="80%" valign="middle" align="center">
		// <span style="font-size:16px; font-weight: bold; line-height: 150%">回到唐朝当皇帝</span>
		//</td>
		try {
			NodeFilter titleFilter = new AndFilter(new TagNameFilter("td"), new AndFilter(new HasAttributeFilter(
					"width", "80%"), new AndFilter(new HasAttributeFilter("valign", "middle"), new HasAttributeFilter(
					"align", "center"))));
			NodeList titleNodes = this.getParser().parse(titleFilter);
			if (null == titleNodes || titleNodes.size() > 1) {
				return false;
			}
			if (null == titleNodes.elementAt(0)) {
				return false;
			}
			String text = titleNodes.elementAt(0).toHtml();
			String title = this.getProp("<span style=\"(.*)\">(.*)</span>", text, 2).trim();
			book.setTitle(title);
			LOGGER.info("book title: " + book.getTitle());

			// 缺少一个menuUrl,现在还不知道这个是干嘛的
			return true;
		} catch (ParserException e) {
			throw new ParserException("解析title失败!", e);
		}
	}

	public static void main(String[] args) throws Exception {
		Extractor extractor = new QiqishuBookExtractor(Charset.GB2312.getValue());
		extractor.loadPageNotProxy("http://www.77shu.com/detail_9702.html");
		extractor.extract();
	}
}
