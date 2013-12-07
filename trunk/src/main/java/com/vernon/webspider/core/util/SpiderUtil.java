package com.vernon.webspider.core.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.vernon.webspider.book.domain.GroupViewFormat;
import com.vernon.webspider.book.domain.ImageInfo;
import com.vernon.webspider.core.collection.Pair;
import com.vernon.webspider.core.http.Browser;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.http.HttpClientUtil;
import com.vernon.webspider.core.http.URLWrap;

/**
 * 抓取工具类,主要用于实现一些html标签的基本解析
 * 
 * @author Vernon.Chen
 * @date 2012-8-3
 */
public class SpiderUtil {

	protected static Logger logger = Logger.getLogger(SpiderUtil.class);

	/**
	 * 获取指定URL地址,指定规则的Source
	 *
	 * @param sourceUrl url
	 * @param encoding  编码
	 * @param filter    NodeFilter
	 * @return String
	 */
	public static String getSource(String sourceUrl, String encoding, NodeFilter filter) {
		try {
			Parser parser = new Parser(sourceUrl);
			parser.setEncoding(encoding);
			NodeList nodeList = parser.extractAllNodesThatMatch(filter);
			StringBuilder total = new StringBuilder();
			for (int i = 0, n = nodeList.size(); i < n; i++) {
				total.append(nodeList.elementAt(i).toHtml());
			}
			return total.toString();
		} catch (ParserException e) {
			logger.error(e);
		}
		return "";
	}

	/**
	 * 获取超链接中的链接地址
	 *
	 * @param str 原内容
	 * @return href String
	 */
	public static String getLink(final String str) {
		String linkUrl = "";
		Parser parser = null;
		NodeFilter filter;
		NodeList nodelist = null;

		try {
			parser = new Parser(str);
			parser.setEncoding("gb2312");
		} catch (ParserException e1) {
			System.err.println("error!");
			logger.error("解析页面失败：" + str + e1);
		}

		filter = new NodeClassFilter(LinkTag.class);
		if (null != parser) {
			try {
				nodelist = parser.extractAllNodesThatMatch(filter);
			} catch (ParserException e) {
				logger.error("解析页面标签失败：" + e);
			}
			if (null != nodelist && nodelist.size() > 0) {
				LinkTag linkTag = (LinkTag) nodelist.elementAt(0);
				linkUrl = linkTag.getLink();
			}
		}

		return linkUrl;
	}

	//获取该文本中所有的超链接

	public static List<String> getLinks(final String s) {
		String regex;
		final List<String> list = new ArrayList<String>();
		regex = "<[a|A][^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</[a|A]>";
		final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}

		return list;
	}

	//删除文本内容中的所有超链接

	public static String removeAllLink(final String s) {
		return removeHrefTag(s, true);
	}

	/**
	 * 去除href标记，分两种情况：1、获取href中的地址；2、获取<a></a>中的值
	 *
	 * @param str
	 * @param removeTag 该标识用于区分上述的两种情况，true表示第2种情况；false表示第1种情况
	 * @return
	 */
	public static String removeHrefTag(String str, boolean removeTag) {
		String result = str;
		String regex;
		//截取超链接字段
		if (!removeTag) {
			//        	regex = "<[a|A][^>]*href=[\"|\'|\\s]";
			//          String temp = str.replaceAll(regex, "");
			//          regex = "[\"|\'|\\s]>([\\s|\\S]*?)</[a|A]>";
			//          result = temp.replaceAll(regex, "");
			regex = "<[a|A][^>]*href=\"(.*)?\">((?!<a)[\\s|\\S])*</[a|A]>";
			Pattern pa = Pattern.compile(regex);
			Matcher ma = pa.matcher(str);
			if (ma.find()) {
				result = ma.group(1);
			}
		}
		//截取<a></a>之间的显示文本
		else {
			regex = "<[a|A][^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>";
			String temp = str.replaceAll(regex, "");
			regex = "\\s*</[a|A]>";
			result = temp.replaceAll(regex, "");
		}

		return result;
	}

	/**
	 * 获取<a></a>中href地址
	 *
	 * @param str
	 * @return String
	 */
	public static String getHrefFromA(String str) {
		String result = str;
		String regex;
		//截取超链接字段
		//        href="http://www.xsjia.com/book/6/6872/"
		regex = "href *= *['\"](.[^\"]*)[\"']* *";
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(str);
		if (ma.find()) {
			result = ma.group(1);
		}
		return result;
	}

	//从内容中获取图片位置信息及文本内容（利用html parser实现）

	public static String getPicAndContent(final String s) {
		StringBuffer result = new StringBuffer();
		Parser parser = null;
		NodeFilter filter;
		NodeList nodelist = null;

		try {
			parser = new Parser(s);
			parser.setEncoding("gb2312");
		} catch (ParserException e) {
			System.err.println("error!");
			logger.error("解析页面失败：" + e);
		}
		filter = new OrFilter(new NodeClassFilter(ParagraphTag.class), new NodeClassFilter(ImageTag.class));
		if (null != parser) {
			try {
				nodelist = parser.extractAllNodesThatMatch(filter);
			} catch (ParserException e) {
				logger.error("解析页面标签失败：" + e);
			}
			if (null != nodelist && nodelist.size() > 0) {
				for (int i = 0; i < nodelist.size(); i++) {
					result.append(nodelist.elementAt(i).toHtml());
				}
			}
		}

		return result.toString();
	}

	//去除段落标记<p></p>

	public static String removeProTag(final String str) {
		String result;
		String regex;
		regex = "(<[(p|P)][^>]*>|</[(p|P)]>)";
		result = str.replaceAll(regex, "");
		return result;
	}

	//去除<span></span>标记

	public static String removeSpanTag(final String str) {
		String result = str;
		String regex = "</?SPAN[^>]*>";
		Pattern pa = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher ma = pa.matcher(str);
		while (ma.find()) {
			result = result.replace(ma.group(), "");
		}
		return result;
	}

	public static String replaceSpanTag(final String str) {
		String result = str;
		String regex = "</?SPAN[^>]*>";
		Pattern pa = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher ma = pa.matcher(str);
		while (ma.find()) {
			result = result.replace(ma.group(), "");
		}

		return result;
	}

	public static String removeDivTag(final String str) {
		String result;
		String regex = "<div(.*)?>(.*)?</div(.*)?>";
		result = str.replaceAll(regex, "");
		return result;
	}

	//获取所有图片的详细参数信息

	public static List<ImageInfo> getAllPicInfo(String str) {
		List<ImageInfo> picInfo = new ArrayList<ImageInfo>();
		List<String> picLoc = deDuping(getAllPic(str));
		ImageInfo image;
		String replaceStr = str;
		URLWrap wrap = null;

		int count = picLoc.size();
		for (String each_str : picLoc) {
			image = new ImageInfo();
			String imageUrl = getPicLocInfo(each_str);
			image.setUrlAddress(imageUrl);
			image.setImageName(getPicName(imageUrl));
			if (count-- > 0) {
				int position = replaceStr.indexOf(each_str);
				image.setPosition(position);
				replaceStr = replaceStr.replace(each_str, "").trim();
			}
			if (null != imageUrl && !("").equals(imageUrl)) {
				try {
					wrap = new URLWrap(imageUrl);
				} catch (MalformedURLException e) {
					System.err.println("error!");
					logger.error("url地址包装错误：" + imageUrl + e);
				}
			}

			if (null != wrap) {
				Pair<Integer, byte[]> imageResult = HttpClientUtil.getByteArrayByGET(wrap, Charset.UTF8,
						Browser.IE7_ON_WINXP, 60000);
				if (null != imageResult && 200 == imageResult.getKey()) {
					byte[] imageByte = imageResult.getValue();
					image.setImageByte(imageByte);
				}
			}
			picInfo.add(image);
		}

		return picInfo;
	}

	//去重

	public static List<String> deDuping(List<String> lists) {
		List<String> removeDuping = new ArrayList<String>();
		for (String list : lists) {
			if (!removeDuping.contains(list)) {
				removeDuping.add(list);
			}
		}

		return removeDuping;
	}

	//获取所有图片的详细参数信息,其中picLoc为图片列表

	public static List<ImageInfo> getAllPicInfo(final List<String> picLoc) {
		List<ImageInfo> picInfo = new ArrayList<ImageInfo>();
		Set<String> imageNames = new HashSet<String>();
		ImageInfo image;
		URLWrap wrap = null;

		for (String each_str : picLoc) {
			image = new ImageInfo();
			each_str = outTag(each_str);
			String imageName = getPicName(each_str);
			if (!imageNames.contains(imageName)) {
				image.setUrlAddress(each_str);
				image.setImageName(imageName);

				if (null != each_str && !("").equals(each_str)) {
					try {
						wrap = new URLWrap(each_str);
					} catch (MalformedURLException e) {
						System.err.println("error!");
						logger.error("url地址包装错误：" + each_str + e);
					}
				}
				if (wrap != null) {
					Pair<Integer, byte[]> imageResult = HttpClientUtil.getByteArrayByGET(wrap, Charset.GB2312,
							Browser.IE7_ON_WINXP, 60000);
					if (null != imageResult && 200 == imageResult.getKey()) {
						byte[] imageByte = imageResult.getValue();
						image.setImageByte(imageByte);
					} else {
						logger.error("图片生成错误：" + each_str);
					}
				}
				picInfo.add(image);
			}
			imageNames.add(imageName);
		}

		return picInfo;
	}

	//获取所有的图片（采用正则实现）

	public static List<String> getAllPic(final String str) {
		List<String> picInfo = new ArrayList<String>();
		String regex;
		regex = "(<[(img|IMG|Img)][^>]*>|</[(img|IMG|Img)]>)";
		final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher ma = pa.matcher(str);
		while (ma.find()) {
			picInfo.add(ma.group());
		}

		return picInfo;
	}

	//获取组图的所有信息 <此程序适用于json格式>
	//适合于2010年09月21日之前的格式：var photoJson = [{showtit:'郭晓冬、赵子琪搞笑婚纱照',showtxt:'',smallpic:'http://img1.gtimg.com/1/100/10099/1009992_200x200_0.jpg','bigpic':'http://img1.gtimg.com/1/100/10099/1009992_550x550_192.jpg'},{showtit:'郭晓冬、赵子琪搞笑婚纱照',showtxt:'',smallpic:'http://img1.gtimg.com/1/100/10099/1009990_200x200_0.jpg','bigpic':'http://img1.gtimg.com/1/100/10099/1009990_550x550_192.jpg'},{showtit:'郭晓冬、赵子琪搞笑婚纱照',showtxt:'',smallpic:'http://img1.gtimg.com/1/100/10099/1009987_200x200_0.jpg','bigpic':'http://img1.gtimg.com/1/100/10099/1009987_550x550_192.jpg'},{showtit:'郭晓冬、赵子琪搞笑婚纱照',showtxt:'',smallpic:'http://img1.gtimg.com/1/100/10099/1009988_200x200_0.jpg','bigpic':'http://img1.gtimg.com/1/100/10099/1009988_550x550_192.jpg'},{showtit:'郭晓冬、赵子琪搞笑婚纱照',showtxt:'',smallpic:'http://img1.gtimg.com/1/100/10099/1009993_200x200_0.jpg','bigpic':'http://img1.gtimg.com/1/100/10099/1009993_550x550_192.jpg'},{showtit:'郭晓冬、赵子琪搞笑婚纱照',showtxt:'',smallpic:'http://img1.gtimg.com/1/100/10099/1009989_200x200_0.jpg','bigpic':'http://img1.gtimg.com/1/100/10099/1009989_550x550_192.jpg'},{showtit:'郭晓冬、赵子琪搞笑婚纱照',showtxt:'',smallpic:'http://img1.gtimg.com/1/100/10099/1009991_200x200_0.jpg','bigpic':'http://img1.gtimg.com/1/100/10099/1009991_550x550_192.jpg'}];

	public static List<ImageInfo> getGroupPicInfo(final String str) {
		List<ImageInfo> picInfo = new ArrayList<ImageInfo>();
		URLWrap wrap = null;
		ImageInfo image;
		String imageUrl;
		String regex = "\\{[^\\{]*\\}";

		List<String> lists = new ArrayList<String>();
		List<GroupViewFormat> listGW = new ArrayList<GroupViewFormat>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			lists.add(m.group());
		}
		//		JSONArray jsonArray = JSONArray.fromObject(str);
		//		List<GroupViewFormat> list = JSONArray.toList(jsonArray, GroupViewFormat.class);
		for (String list : lists) {
			GroupViewFormat gw = JSONUtil.fromJSON(list, GroupViewFormat.class);
			listGW.add(gw);
		}

		for (GroupViewFormat gw : listGW) {
			image = new ImageInfo();
			imageUrl = gw.getBigpic();
			image.setUrlAddress(imageUrl);
			image.setImageName(getPicName(imageUrl));
			image.setPosition(0);
			if (null != imageUrl && !("").equals(imageUrl)) {
				try {
					wrap = new URLWrap(imageUrl);
				} catch (MalformedURLException e) {
					System.err.println("error!");
					logger.error("url地址包装错误：" + imageUrl + e);
				}
			}
			if (wrap != null) {
				Pair<Integer, byte[]> imageResult = HttpClientUtil.getByteArrayByGET(wrap, Charset.GB2312,
						Browser.IE7_ON_WINXP, 30000);
				if (null != imageResult && 200 == imageResult.getKey()) {
					byte[] imageByte = imageResult.getValue();
					image.setImageByte(imageByte);
				} else {
					logger.error("图片生成错误：" + imageUrl);
				}
			}

			picInfo.add(image);
		}

		return picInfo;
	}

	//获取组图新闻的图片(适合于Json格式)

	public static List<ImageInfo> getGroupPicInfo2(final String str) {
		List<ImageInfo> picInfo = new ArrayList<ImageInfo>();
		URLWrap wrap = null;
		ImageInfo image;
		//String regex = "Name:\'smallimgurl\',((?!bigimgurl)[\\s|\\S])*Children:\\[\\{Name:\'(.*?)\', Content:\'(.*?)\', Attributes:\\[\\], Children:\\[\\]\\}\\]\\},\\{Name:\'cnt_article\'(.*?)";
		//modify 2010-12-23 模板变更
		String regex = "\'Name\':\'smallimgurl\',((?!bigimgurl)[\\s|\\S])*\'Children\':\\[\\{\'Name\':\'(.*?)\', \'Content\':\'(.*?)\', \'Attributes\':\\[\\], \'Children\':\\[\\]\\}\\]\\},\\{\'Name\':\'cnt_article\'(.*?)";
		List<String> lists = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			String temp = m.group(3);
			System.out.println(temp);
			if (temp != null && temp.length() > 0) {
				temp = temp.substring(temp.lastIndexOf('\'') + 1);
				if (temp.startsWith("http")) {
					lists.add(temp);
				}
			}
		}
		for (String list : lists) {
			image = new ImageInfo();
			image.setUrlAddress(list);
			image.setImageName(getPicName(list));
			image.setPosition(0);
			if (null != list && !("").equals(list)) {
				try {
					wrap = new URLWrap(list);
				} catch (MalformedURLException e) {
					System.err.println("error!");
					logger.error("url地址包装错误：" + list + e);
				}
			}
			if (wrap != null) {
				Pair<Integer, byte[]> imageResult = HttpClientUtil.getByteArrayByGET(wrap, Charset.GB2312,
						Browser.IE7_ON_WINXP, 30000);
				if (null != imageResult && 200 == imageResult.getKey()) {
					byte[] imageByte = imageResult.getValue();
					image.setImageByte(imageByte);
				} else {
					logger.error("图片生成错误：" + list);
				}
			}
			picInfo.add(image);
		}

		return picInfo;
	}

	//获取组图的所有信息 <此程序适用于腾讯_d.html格式>

	public static List<ImageInfo> getGroupPic(final String str) {
		List<ImageInfo> picInfo = new ArrayList<ImageInfo>();
		Parser parser = null;
		NodeFilter filter;
		NodeList nodelist = null;
		StringBuffer sb = new StringBuffer();
		URLWrap wrap = null;
		ImageInfo image;
		String imageUrl;

		if (null != str) {
			try {
				parser = new Parser(str);
				parser.setEncoding("gb2312");
			} catch (ParserException e) {
				System.err.println("error!");
				logger.error("解析页面失败：" + e);
			}
		}

		//<div id='sImage'>
		filter = new AndFilter(new NodeClassFilter(Div.class), new HasAttributeFilter("id", "sImage"));
		if (null != parser) {
			try {
				nodelist = parser.extractAllNodesThatMatch(filter);
			} catch (ParserException e) {
				logger.error("解析页面标签失败：" + e);
			}
			if (null != nodelist && nodelist.size() > 0) {
				for (int i = 0; i < nodelist.size(); i++) {
					sb.append(nodelist.elementAt(i).toHtml());
				}
			}
		}
		List<String> allPics = getAllPic(sb.toString());
		for (String pic : allPics) {
			imageUrl = getPicLocInfo(pic);
			image = new ImageInfo();
			image.setUrlAddress(imageUrl);
			image.setImageName(getPicName(imageUrl));
			image.setPosition(0);
			if (null != imageUrl && !("").equals(imageUrl)) {
				try {
					wrap = new URLWrap(imageUrl);
				} catch (MalformedURLException e) {
					System.err.println("error!");
					logger.error("url地址包装错误：" + imageUrl + e);
				}
			}
			if (wrap != null) {
				Pair<Integer, byte[]> imageResult = HttpClientUtil.getByteArrayByGET(wrap, Charset.GB2312,
						Browser.IE7_ON_WINXP, 30000);
				if (null != imageResult && 200 == imageResult.getKey()) {
					byte[] imageByte = imageResult.getValue();
					image.setImageByte(imageByte);
				} else {
					logger.error("图片生成错误：" + imageUrl);
				}
			}

			picInfo.add(image);
		}

		return picInfo;
	}

	/**
	 * 去除图片标记<img></img>
	 *
	 * @param str   文本信息
	 * @param first 是否只是匹配第一个
	 * @return String
	 */
	public static String removePicTag(final String str, boolean first) {
		String result;
		String regex;
		regex = "<[IMG|Img|img][^>]+>";
		if (first) {
			result = str.replaceFirst(regex, "");
		} else {
			result = str.replaceAll(regex, "");
		}

		return result;
	}

	public static boolean containsPicTag(final String str) {
		boolean result = false;
		String regex = "<[img|Img|IMG][^>]*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.find()) {
			result = true;
		}
		return result;
	}

	//从内容中获取标签，即所有超链接中的文本内容

	public static List<String> getMarks(final String s) {
		List<String> hrefList = getLinks(s);
		List<String> marks = new ArrayList<String>();
		for (String str : hrefList) {
			String temp = removeHrefTag(str, true);
			marks.add(temp);
		}

		return marks;
	}

	//获取图片所在的位置信息. 如<img src="www.a8.com/123.jpg">，即获取www.a8.com/123.jpg (采用html parser进行实现)

	public static String getPicLocInfo(final String str) {
		String result = null;
		Parser parser = null;
		NodeFilter filter;
		NodeList nodelist = null;

		try {
			parser = new Parser(str);
			parser.setEncoding("gb2312");
		} catch (ParserException e) {
			System.err.println("error!");
			logger.error("解析页面失败：" + e);
		}

		filter = new NodeClassFilter(ImageTag.class);
		if (null != parser) {
			try {
				nodelist = parser.extractAllNodesThatMatch(filter);
			} catch (ParserException e) {
				logger.error("解析页面标签失败：" + e);
			}
			if (null != nodelist && nodelist.size() > 0) {
				ImageTag image = (ImageTag) nodelist.elementAt(0);
				result = image.getImageURL();
			}
		}

		return result;
	}

	//获取所有图片的位置信息

	/**
	 * 获取图片img src地址列表
	 *
	 * @param str 源码
	 * @return List<String>
	 */
	public static List<String> getAllPicLocInfo(final String str) {
		List<String> result = new ArrayList<String>();
		Parser parser = null;
		NodeFilter filter;
		NodeList nodelist = null;

		try {
			parser = new Parser(str);
			parser.setEncoding("gb2312");
		} catch (ParserException e) {
			System.err.println("error!");
			logger.error("解析页面失败：" + e);
		}

		filter = new NodeClassFilter(ImageTag.class);
		if (null != parser) {
			try {
				nodelist = parser.extractAllNodesThatMatch(filter);
			} catch (ParserException e) {
				logger.error("解析页面标签失败：" + e);
			}
			if (null != nodelist && nodelist.size() > 0) {
				for (int i = 0; i < nodelist.size(); i++) {
					ImageTag image = (ImageTag) nodelist.elementAt(i);
					if (image.getImageURL().length() > 0) {
						result.add(image.getImageURL());
					}
				}
			}
		}

		return result;
	}

	//获取图片的名称

	public static String getPicName(final String str) {
		String picName = "";
		if (null != str && str.contains("/")) {
			picName = str.substring(str.lastIndexOf('/') + 1);
		}

		return picName;
	}

	//获取该图片相对于文本的位置

	public int getPicLocPos(final String str) {
		int position;
		String temp = str.toLowerCase();
		position = temp.indexOf("<img");
		return position;
	}

	//获取该新闻的标题

	public static String getTitle(final String str) {
		String regex;
		StringBuffer title = new StringBuffer();
		final List<String> list = new ArrayList<String>();
		regex = "<title>.*?</title>";
		final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		final Matcher ma = pa.matcher(str);
		while (ma.find()) {
			list.add(ma.group());
		}
		for (String aList : list) {
			title.append(aList);
		}

		return outTag(title.toString());
	}

	//去掉标记 <>或</>

	public static String outTag(final String s) {
		return s.replaceAll("<.*?>", "");
	}

	//时间匹配正则  如：2010年05月06日11：02

	public static String getTime(final String s) {
		String result = null;
		String regex = "\\d.*\\w*[:]\\d{2}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if (m.find()) {
			result = m.group();
		}

		return result;
	}

	//提取模板页中的url地址，即待抓取消息的地址列表

	public static List<String> extractLinkUrl(final String str) {
		List<String> listNews = new ArrayList<String>();
		Parser parser = null;
		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList nodelist = null;

		try {
			parser = new Parser(str);
			parser.setEncoding("gb2312");
		} catch (ParserException e1) {
			System.err.println("error!");
			logger.error("解析页面失败：" + e1);
		}

		if (null != parser) {
			try {
				nodelist = parser.extractAllNodesThatMatch(filter);
			} catch (ParserException e) {
				logger.error("解析页面标签失败：" + e);
			}
			if (null != nodelist && nodelist.size() > 0) {
				for (int i = 0; i < nodelist.size(); i++) {
					LinkTag linkTag = (LinkTag) nodelist.elementAt(i);
					String linkUrl = linkTag.getLink();
					if (linkUrl.startsWith("http") && (linkUrl.endsWith("htm") || linkUrl.endsWith("html"))) {
						listNews.add(linkUrl);
					}
				}
			}
		}

		return listNews;
	}

	public static byte[] getPicByBytes(final String imageUrl) {
		byte[] imageByte = null;
		URLWrap wrap = null;

		if (null != imageUrl && !("").equals(imageUrl)) {
			try {
				wrap = new URLWrap(imageUrl);
			} catch (MalformedURLException e) {
				System.err.println("error!");
				logger.error("url地址包装错误：" + imageUrl + e);
			}
		}
		if (null != wrap) {
			Pair<Integer, byte[]> imageResult = HttpClientUtil.getByteArrayByGET(wrap, Charset.GB2312,
					Browser.IE7_ON_WINXP, 3000);
			if (null != imageResult && 200 == imageResult.getKey()) {
				imageByte = imageResult.getValue();
			}
		}

		return imageByte;
	}

	public static String removeScript(final String pageInfo) {
		String result = pageInfo;
		String regex = "<script[^>]*>((?!</script>)[\\s|\\S])*</script>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher ma = p.matcher(pageInfo);
		while (ma.find()) {
			result = result.replace(ma.group(), "");
		}

		return result;
	}

	public static String removeAllScript(final String pageInfo) {
		String result = pageInfo;
		String regex = "<script[^>]*>[\\s|\\S]*</script>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher ma = p.matcher(pageInfo);
		while (ma.find()) {
			result = result.replace(ma.group(), "");
		}

		return result;
	}

	public static String removeOtherScript(final String pageInfo) {
		String result;
		StringBean sb = new StringBean();
		sb.setLinks(false);
		sb.setCollapse(true);
		sb.setReplaceNonBreakingSpaces(true);
		Parser parser = null;
		try {
			parser = new Parser(pageInfo);
			parser.setEncoding("gb2312");
		} catch (ParserException pe) {
			System.err.println("error!");
			logger.error("解析页面失败：" + pageInfo + pe);
		}
		if (null != parser) {
			try {
				parser.visitAllNodesWith(sb);
			} catch (ParserException e) {
				logger.error("解析页面标签失败：" + pageInfo + e);
			}
		}

		result = sb.getStrings();
		return result;
	}

	public static String[] listToArray(List<String> lists) {
		String[] arrays = new String[lists.size()];
		if (lists.size() > 0) {
			for (int i = 0; i < lists.size(); i++) {
				arrays[i] = lists.get(i);
			}
		}

		return arrays;
	}

	public static int computePicGrade(float fp) {
		int picGrade = 0;
		if (fp <= 0.2f) {
			picGrade = 1;
		}
		if (fp > 0.2f && fp <= 0.4f) {
			picGrade = 2;
		}
		if (fp > 0.4f && fp <= 0.6f) {
			picGrade = 3;
		}
		if (fp > 0.6f && fp <= 0.8f) {
			picGrade = 4;
		}
		if (fp > 0.8f && fp <= 1.0f) {
			picGrade = 5;
		}

		return picGrade;
	}

	//将<br>或<br/>替换成相应的字符串

	public static String replaceAllBR(String sourceStr, String replaceChar) {
		String result = sourceStr;
		if (sourceStr.contains("<br>") || sourceStr.contains("<BR>") || sourceStr.contains("<br/>")
				|| sourceStr.contains("<BR/>") || sourceStr.contains("<br />")) {
			result = sourceStr.replaceAll("<[b|B][r|R]\\s*/?>", replaceChar);
		}
		return result;
	}

	public static String replacePTag(String str, String replaceChar) {
		String result = str;
		result = result.replaceAll("<[(p|P)][^>]*>", "");
		result = result.replaceAll("</[p|P]>", replaceChar);

		return result;
	}

	public static String removeStrong(String str) {
		String result = str;
		result = result.replaceAll("</?STRONG>|</?strong>", "");

		return result;
	}

	public static String removeNotation(String str) {
		String result;
		String regex = "<!--.*?-->";
		result = str.replaceAll(regex, "");

		return result;
	}

	public static String removeTStartTag(String str) {
		String result;
		String regex = "</?[t|T][^>]*>";
		result = str.replaceAll(regex, "");
		return result;
	}

	public static String removeChangeLine(String str) {
		String result = str;
		result = result.replace("\r\n", "");
		return result;
	}

	public static String removeFontTag(String str) {
		String result;
		String regex = "</?FONT[^>]*>";
		result = str.replaceAll(regex, "");
		return result;
	}

	public static String removeCenterTag(String str) {
		String result = str;
		String regex = "</?center>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		while (m.find()) {
			result = result.replace(m.group(), "");
		}

		return result;
	}

	public static String removeDiv(String str) {
		String result = str;
		String regex = "</?div[^>]*>|</?DIV[^>]*>";
		result = result.replaceAll(regex, "");

		return result;
	}

	public static String removeObjectTag(String str) {
		String result = str;
		String regex = "<object(.*?)>[\\s|\\S]*</object(.*?)>";
		result = result.replaceAll(regex, "");

		return result;
	}

	public static String removeMultiBlankTag(String str) {
		String result = str;
		//        String regex = "\\s+";
		String regex = "[\\t|\\n|\\x0B|\\f]";
		result = result.replaceAll(regex, " ");
		return result;
	}

	public static String replaceMultiBlankTag(String str) {
		String result = str;
		String regex = "  ";
		result = result.replaceAll(regex, "");
		return result;
	}

	//<?xml:namespace prefix = o ns = "urn:schemas-microsoft-com:office:office" />

	public static String removeXMLTag(String str) {
		String result = str;
		String regex = "<\\?xml(.*)+/>";
		result = result.replaceAll(regex, "");

		return result;
	}

	//<style></style>

	public static String removeStyleTag(String str) {
		String result = str;
		String regex = "<style[^>]*>((?!</style>)[\\s|\\S])*</style>";
		result = result.replaceAll(regex, "");

		return result;
	}

	public static String removeSelectTag(String str) {
		String result = str;
		String regex = "<select[^>]*>((?!</select>)[\\s|\\S])*</select>";
		result = result.replaceAll(regex, "");

		return result;
	}

	//<o:p></o:p>

	public static String removeOTag(String str) {
		String result = str;
		String regex = "</?o:p>";
		result = result.replaceAll(regex, "");

		return result;
	}

	public static Date formatDateTime(String str) {
		Date date = new Date();
		if (null != str && !("").equals(str)) {
			//格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
			try {
				date = sdf.parse(str);
			} catch (ParseException e) {
				logger.error("时间格式转换错误！");
			}
		}

		return date;
	}

	public static String removeIFrameTag(String str) {
		String result = str;
		String regex = "</?iframe[^>]*>";
		result = result.replaceAll(regex, "");

		return result;
	}

	public static String removeBTag(String str) {
		String result = str;
		String regex = "</?[b|B][^>]*>";
		result = result.replaceAll(regex, "");

		return result;
	}

	/**
	 * 获取随机日期
	 *
	 * @param beginDate 起始日期，格式为：yyyy-MM-dd
	 * @param endDate   结束日期，格式为：yyyy-MM-dd
	 * @return Date
	 */
	public static Date randomDate(String beginDate, String endDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return null;
			}
			long date = random(start.getTime(), end.getTime());
			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取随机数
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	public static long random(long begin, long end) {
		return begin + (long) (Math.random() * (end - begin));
	}

	public static void main(String[] args) throws IOException {
		//		String testStr = "<p>adkdsafkdak<a href='123.html'>456</a><script>fdddfdfdfdfasdsf</script><img src='896.jpg'></p>";
		//	    String result = removeOtherScript(testStr);
		//		System.out.println(result);
		//		
		//		byte[] pic = getPicByBytes("http://data.yule.sohu.com/star/pic/star/3/3989/middle.jpg");
		//		FileOutputStream fos = new FileOutputStream(new File("c:\\a.jpg"));
		//		fos.write(pic);

		//		String picInfo = "<IMG alt=\"英国美女主持致敬黛比&#183;摩尔 大胆翻拍怀孕裸照\" src=\"http://img1.gtimg.com/ent/pics/hv1/0/237/517/33678360.jpg\">英国著名美女主持人查内尔&#183;海斯<IMG alt=\"英国美女主持致敬黛比&#183;摩尔 大胆翻拍怀孕裸照\" src=\"http://img1.gtimg.com/ent/pics/hv1/2/237/517/33678362.jpg\">黛比&#183;摩尔1999年的经典怀孕裸照<腾讯娱乐讯 1991年，凭借《人鬼情未了》一片走红的黛比&#183;摩尔在怀孕8个月时，为《名利场》杂志拍摄了一组怀孕裸照，成为好莱坞轰动一时的新闻。日前，英国著名美女主持人查内尔&#183;海斯（Chanelle Hayes）也在怀胎6月之时，为法国《Closer》杂志拍摄了一组挺着大肚的裸照，以此向黛比&#183;摩尔当初是创举“致敬”。现年22岁的查内尔&#183;海斯是前英国真人秀节目《老大哥》（Big Brother）的著名女主持，她凭借自己火辣的双嘴和火辣的身材，成为英国家喻户晓的人物，此前，她和英超的球员有过一段情史，据称，著名足球运动员贝克汉姆还是她的“暗恋者”。2009年3月，查内尔&#183;海斯和真人秀男星杰克&#183;特维德（Jack Tweed）结婚，并在年底成功怀上身孕。此前和英国性感女星露西&#183;皮德尔（Lucy Pinder）曾为男性杂志《坚果》拍摄过一组火爆全裸写真的查内尔&#183;海斯，这次单枪匹马上阵，重现黛比&#183;摩尔的经典之作，赚足了噱头。（比尼）";
		//        List<ImageInfo> images = SpiderUtil.getAllPicInfo(picInfo);
		//        for(ImageInfo image : images) {
		//        	System.out.println(image.getPosition());
		//        	System.out.println(image.getImageName());
		//        }
		//        
		//        boolean result = containsPicTag(picInfo);
		//        System.out.println(result);
		//		
		//        List<String> lists = new ArrayList<String>();
		//        String[] arrays = listToArray(lists);
		//        System.out.println(arrays.length);

		//		String str = "<script>aadkdf<br>dkasdkdak<br/></script>kdkasdkadfKDKASD<BR><br><ASADFADF><Br/>";
		//		String result = replaceAllBR(str, "///");
		//		System.out.println(result);

		//		String str1 = "<dir><script dasdfasdffasdasdfasdf>asdfafsdasdfasdffasdasdds</script>asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf</div>";
		//		String removeScript = removeScript(str1);
		//		System.out.println(removeScript);
		//		
		//		
		//		String str2 = "<IMG alt=杰克逊遗嘱曝光：三名子女各得3亿美元遗产 src=\"http://img1.gtimg.com/ent/pics/hv1/78/228/534/34781568.jpg\"><BR><IMG alt=杰克逊遗嘱曝光：三名子女各得3亿美元遗产 src=\"http://img1.gtimg.com/ent/pics/hv1/78/228/534/34781568.jpg\">";
		//		String removeStrong = removeStrong(str2);
		//		System.out.println(removeStrong);
		//		
		//		String str3 = "2010年05月20日 10:30";
		//		Date date = formatDateTime(str3);
		//		System.out.println(date);
		//		
		//		
		//		String str4 = "<SPAN>ddddsdffdadfasdf</SPAN><kdkfasdfjk>";
		//		System.out.println(removeSpanTag(str4));
		//		
		//		String str5 = "<p>　　<strong>泽尻一双健壮的腿，好比是女摔角手</strong></p><p>　　泽尻英龙华20岁时演出日剧<a class=\"akey\" href=\"http://ent.sina.com.cn/v/j/f/ygshdyl/index.html\" target=\"_blank\">《一公升的眼泪》</a>中患有小脑萎缩症的少女，当时精湛的演技获得一致好评，在剧中楚楚可怜的样子让人看了不舍，不过自从疑似涉入吸毒案被经纪公司开除后，声势急速下滑，就连身材也日渐走样，上月底献唱新歌时，穿着紧身战衣的泽尻却露出一双健壮的腿，壮硕的程度好比是女摔角手。</p>";
		//	    System.out.println(replacePTag(str5, "\r"));
		//		
		//	    String str6 = "[{showtit:'侯耀文遗产案开庭，女儿侯瓒法庭数度落泪',showtxt:'',smallpic:'http://img1.gtimg.com/0/55/5567/556778_200x200_0.jpg','bigpic':'http://img1.gtimg.com/0/55/5567/556778_550x550_0.jpg'},{showtit:'侯耀文女儿侯瓒',showtxt:'',smallpic:'http://img1.gtimg.com/0/55/5567/556775_200x200_0.jpg','bigpic':'http://img1.gtimg.com/0/55/5567/556775_1200x1000_0.jpg'},{showtit:'法庭现场',showtxt:'',smallpic:'http://img1.gtimg.com/0/55/5567/556776_200x200_0.jpg','bigpic':'http://img1.gtimg.com/0/55/5567/556776_1200x1000_0.jpg'},{showtit:'法庭现场',showtxt:'',smallpic:'http://img1.gtimg.com/0/55/5567/556777_200x200_0.jpg','bigpic':'http://img1.gtimg.com/0/55/5567/556777_1200x1000_0.jpg'}]";
		//	    String regex = "\\{[^\\{]*\\}";
		//	    List<String> lists = new ArrayList<String>();
		//	    Pattern p = Pattern.compile(regex);
		//	    Matcher m = p.matcher(str6);
		//	    while(m.find()) {
		//	    	lists.add(m.group());
		//	    }
		//	    
		//	    for(String list : lists) {
		//	    	GroupViewFormat gw = JSONUtil.fromJson(list, GroupViewFormat.class);
		//		    System.out.println(gw.toString());
		//	    }

		//	    String str7  = "4.13-4.19北美票房榜      本周冠军《重返17岁》      《重返17岁》剧照      《重返17岁》剧照      《重返17岁》剧照 ";
		//	    System.out.println(str7);
		//	    String result = removeMultiBlankTag(str7);
		//	    System.out.println(result);
		//	    
		//	    String str8 = "abc   defgh";
		//	    String result8 = str8.replace("  ", "");
		//	    System.out.println(result8);

		//		String divStr = "<DIV>ADFDFDFDFDFS</DIV>";
		//		String result = removeDiv(divStr);
		//		System.out.println(result);
		String str = "{Name:'root',Children:[{Name:'groupimginfo', Content:'', Attributes:[], Children:[{Name:'pageno', Content:'', Attributes:[], Children:[{Name:'', Content:'6', Attributes:[], Children:[]}]},{Name:'groupimg', Content:'', Attributes:[], Children:[{Name:'img', Content:'', Attributes:[], Children:[{Name:'imgname', Content:'', Attributes:[], Children:[{Name:'', Content:'CNBLUE', Attributes:[], Children:[]}]},{Name:'smallimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024849_200x200_0.jpg', Attributes:[], Children:[]}]},{Name:'bigimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024849_550x550_0.jpg', Attributes:[], Children:[]}]},{Name:'cnt_article', Content:'', Attributes:[], Children:[{Name:'', Content:'', Attributes:[], Children:[]}]},{Name:'simg_article_url', Content:'', Attributes:[], Children:[{Name:'text', Content:'http://ent.qq.com/a/20100926/000132.htm', Attributes:[], Children:[]}]}]},{Name:'img', Content:'', Attributes:[], Children:[{Name:'imgname', Content:'', Attributes:[], Children:[{Name:'', Content:'CNBLUE', Attributes:[], Children:[]}]},{Name:'smallimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024850_200x200_0.jpg', Attributes:[], Children:[]}]},{Name:'bigimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024850_550x550_0.jpg', Attributes:[], Children:[]}]},{Name:'cnt_article', Content:'', Attributes:[], Children:[{Name:'', Content:'', Attributes:[], Children:[]}]},{Name:'simg_article_url', Content:'', Attributes:[], Children:[{Name:'text', Content:'http://ent.qq.com/a/20100926/000132_1.htm', Attributes:[], Children:[]}]}]},{Name:'img', Content:'', Attributes:[], Children:[{Name:'imgname', Content:'', Attributes:[], Children:[{Name:'', Content:'CNBLUE', Attributes:[], Children:[]}]},{Name:'smallimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024851_200x200_0.jpg', Attributes:[], Children:[]}]},{Name:'bigimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024851_550x550_0.jpg', Attributes:[], Children:[]}]},{Name:'cnt_article', Content:'', Attributes:[], Children:[{Name:'', Content:'', Attributes:[], Children:[]}]},{Name:'simg_article_url', Content:'', Attributes:[], Children:[{Name:'text', Content:'http://ent.qq.com/a/20100926/000132_2.htm', Attributes:[], Children:[]}]}]},{Name:'img', Content:'', Attributes:[], Children:[{Name:'imgname', Content:'', Attributes:[], Children:[{Name:'', Content:'CNBLUE', Attributes:[], Children:[]}]},{Name:'smallimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024852_200x200_0.jpg', Attributes:[], Children:[]}]},{Name:'bigimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024852_550x550_0.jpg', Attributes:[], Children:[]}]},{Name:'cnt_article', Content:'', Attributes:[], Children:[{Name:'', Content:'', Attributes:[], Children:[]}]},{Name:'simg_article_url', Content:'', Attributes:[], Children:[{Name:'text', Content:'http://ent.qq.com/a/20100926/000132_3.htm', Attributes:[], Children:[]}]}]},{Name:'img', Content:'', Attributes:[], Children:[{Name:'imgname', Content:'', Attributes:[], Children:[{Name:'', Content:'CNBLUE', Attributes:[], Children:[]}]},{Name:'smallimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024853_200x200_0.jpg', Attributes:[], Children:[]}]},{Name:'bigimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024853_550x550_0.jpg', Attributes:[], Children:[]}]},{Name:'cnt_article', Content:'', Attributes:[], Children:[{Name:'', Content:'', Attributes:[], Children:[]}]},{Name:'simg_article_url', Content:'', Attributes:[], Children:[{Name:'text', Content:'http://ent.qq.com/a/20100926/000132_4.htm', Attributes:[], Children:[]}]}]},{Name:'img', Content:'', Attributes:[], Children:[{Name:'imgname', Content:'', Attributes:[], Children:[{Name:'', Content:'CNBLUE', Attributes:[], Children:[]}]},{Name:'smallimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024854_200x200_0.jpg', Attributes:[], Children:[]}]},{Name:'bigimgurl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/102/10248/1024854_550x550_0.jpg', Attributes:[], Children:[]}]},{Name:'cnt_article', Content:'', Attributes:[], Children:[{Name:'', Content:'', Attributes:[], Children:[]}]},{Name:'simg_article_url', Content:'', Attributes:[], Children:[{Name:'text', Content:'http://ent.qq.com/a/20100926/000132_5.htm', Attributes:[], Children:[]}]}]}]},{Name:'last_url', Content:'', Attributes:[], Children:[{Name:'', Content:'http://ent.qq.com/a/20100925/000120.htm', Attributes:[], Children:[]}]},{Name:'last_title', Content:'', Attributes:[], Children:[{Name:'', Content:'拉丁天后夏奇拉纽约演出 化身娇艳肚皮舞娘起舞', Attributes:[], Children:[]}]},{Name:'last_imgUrl', Content:'', Attributes:[], Children:[{Name:'', Content:'http://img1.gtimg.com/1/101/10190/1019034_200x200_0.jpg', Attributes:[], Children:[]}]},{Name:'continuous_play', Content:'', Attributes:[], Children:[{Name:'', Content:'0', Attributes:[], Children:[]}]},{Name:'simg_article_id', Content:'', Attributes:[], Children:[{Name:'text', Content:'20100926000132', Attributes:[], Children:[]}]},{Name:'top_content', Content:'', Attributes:[{Name:'is_firstpage', Children:[{Name:'text', Content:'0', Attributes:[], Children:[]}]}], Children:[]},{Name:'bottom_content', Content:'', Attributes:[{Name:'is_firstpage', Children:[{Name:'text', Content:'0', Attributes:[], Children:[]}]}], Children:[{Name:'text', Content:'<P style=\"TEXT-INDENT: 2em\">腾讯音乐2010年09月25日台北讯 今晚，韩国超人气型男乐团CNBLUE在台大体育馆举办抢听会。原定唱8首作品，因为太High直接大放送2首，但还是让全场爆满的4000名粉丝身陷型男们的帅气光线。 </P><P style=\"TEXT-INDENT: 2em\">人气、买气均大爆发的CNBLUE，因为进场延迟到晚间8时才开始，却丝毫不减粉丝热情，四人一登场就要大家站起来，而粉丝们也超有默契的手持蓝色荧光棒，让台上台下相互辉映成一片蓝海。虽然仅是抢听会，但CNBLUE也被粉丝感动，特别临时加码多唱2曲，最后甚至以日文单曲收尾，不按牌理出牌的演出让主办单位小小慌乱了一下，但却让粉丝欢乐到爆。 </P><P style=\"TEXT-INDENT: 2em\">CNBLUE热唱High过头，四位团员不时放电让全场尖叫声不断。还与粉丝们现场互动，除了回答先前在网络上所募集的问题，还抽出7位幸运粉丝上台让CNBLUE来替她们圆梦。这场见面会光是票房收入就高达台币1500万元，四位团员表示这是他们出道以来办过最大场的演出，喊着迫不急待希望能再来，而四位团员将在26日分批离台。</P><P style=\"TEXT-INDENT: 2em\"><FONT color=#ff0000><STRONG>版权所有，禁止转载！</STRONG></FONT></P>', Attributes:[], Children:[]}]}]}]}/*  |xGv00|8a8f58f7fb8a472b6231ede4cb023cb7 */";
		List<ImageInfo> imageList = getGroupPicInfo2(str);
		for (ImageInfo image : imageList) {
			System.out.println(image.getUrlAddress());
			System.out.println(image.getImageName());
			System.out.println(image.getImageByte().length);
		}

	}
}
