package com.vernon.webspider.core.util;

/**
 * 特殊词处理工具类
 * 
 * @author Vernon.Chen
 * @date 2012-8-3
 */
public class SpecialWordUtil {

	public static String removeSpecialWords(String text) {
		String result = text;
		result = result.replace("版权图片，禁止转载，违者必究！", "");
		result = result.replace("版权所有，禁止转载！", "");
		result = result.replace("版权属东方报业集团或ON.CC所有，不得转载！", "");
		result = result.replace("腾讯娱乐独家稿件 转载请注明出处！", "");
		result = result.replace("（在线看影视作品）", "");
		result = result.replace("，不得转载", "");
		result = result.replace("，禁止转载", "");
		result = result.replace("不得转载", "");
		result = result.replace("禁止转载", "");
		result = result.replace("腾讯娱乐讯：", "");
		result = result.replace("腾讯娱乐讯，", "");
		result = result.replace("腾讯娱乐讯", "");
		result = result.replace("新浪娱乐讯", "");
		result = result.replace("搜狐娱乐讯", "");
		result = result.replace("腾讯娱乐", "");
		result = result.replace("已经浏览到最后一张，您可以", "");
		result = result.replace("重新欣赏进入图片站", "");
		result = result.replace("进入娱乐图片站", "");
		result = result.replace("重新欣赏", "");
		result = result.replace("隐藏", "");
		result = result.replace("查看图注", "");
		result = result.replace("看明星八卦、查影讯电视节目，上手机新浪网娱乐频道 ent.sina.cn", "");
		result = result.replace("点击进入组图", "");
		result = result.replace("[我来说两句]", "");
		result = result.replace("点击查看微博", "");
		result = result.replace("视频：", "");
		result = result.replace("腾讯专稿 转载注明", "");
		result = result.replace(" 读完这篇文章后，您心情如何？00000000", "");
		result = result.replace("图片来源：新浪娱乐", "");
		result = result.replace("&gt;&gt;流行音乐更多资讯，点击浏览", "");
		result = result.replace(">>流行音乐更多资讯，点击浏览", "");
		result = result.replace("点击查看幻灯大图>>>>>>", "");
		result = result.replace("点击查看更多", "");
		result = result.replace("点击查看", "");
		result = result.replace(">>上期榜单回顾", "");
		result = result.replace("&gt;&gt;上期榜单回顾", "");
		result = result.replace("[上一页]", "");
		result = result.replace("[下一页]", "");
		result = result.replace(" <SOHU_AD_LAST>", "");
		result = result.replace(">>>点击试听", "");
		return result;
	}

	public static String htmlDiscode(String text) {
		if (null == text || "".equals(text)) {
			return "";
		}
		text = text.replace("&gt;", ">");
		text = text.replace("&lt;", "<");
		text = text.replace("&nbsp;", " ");
		text = text.replace("&quot;", "\"");
		text = text.replace("&mdash;", "—");
		text = text.replace("&bull;", "·");
		text = text.replace("&#39;", "\'");
		text = text.replace("&#039;", "\'");
		text = text.replace("&#183;", "·");
		text = text.replace("&amp;#8226;", "·");
		text = text.replace("&#8212;", "—");
		text = text.replace("&hellip;&hellip;", "……");
		text = text.replace("&ldquo;", "“");
		text = text.replace("&rdquo;", "”");
		text = text.replace("&#29709;", "琍");
		text = text.replace("&#8226;", "•");
		text = text.replace("&#29625;", "玹");
		text = text.replace("&#23301;", "嬅");
		text = text.replace("&#20594;", "偲");
		text = text.replace("&#32613;", "罥");
		text = text.replace("&middot;", "·");
		return text;
	}

	public static void main(String[] args) {
		String text = "<html>&gt;&#8212;asjkdasdfjadffjd&lt;askjadfdfs&nbsp;&quot;kadfkjj&quot;</html>";
		String result = htmlDiscode(text);
		System.out.println(result);
	}
}
