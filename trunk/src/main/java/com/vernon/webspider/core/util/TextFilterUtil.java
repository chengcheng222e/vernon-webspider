/**
 * 
 */
package com.vernon.webspider.core.util;

/**
 * 字符过滤工具
 * 
 * @author Vernon.Chen
 *
 */
public class TextFilterUtil {

	/**
	 * 转换为HTML
	 * 
	 * @param source
	 * @return
	 */
	public static String toHTML(String source) {
		if (source == null || source.length() == 0) {
			return source;
		}
		source = source.replaceAll("&amp;", "&");
		source = source.replaceAll("&gt;", ">");
		source = source.replaceAll("&lt;", "<");
		source = source.replaceAll("&#39;", "'");
		source = source.replaceAll("&#34;", "\"");
		source = source.replaceAll("&#37;", "%");
		source = source.replaceAll("&nbsp;", " ");
		source = source.replaceAll("&apos;", "\'");
		source = source.replaceAll("&quot;", "\"");
		source = source.replaceAll("\\$\\$", "\\$");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			if (c == '\'') {
				sb.append("&#39;");
			} else if (c == '\"') {
				sb.append("&#34;");
			} else if (c == '<') {
				sb.append("&lt;");
			} else if (c == '>') {
				sb.append("&gt;");
			} else if (c == '%') {
				sb.append("&#37;");
			} else if (c == '&') {
				sb.append("&amp;");
			} else if (c == '$') {
				sb.append("＄");
			} else if (c >= 0 && c < 32 || c == 65535) {
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 过滤HTML标记
	 * 
	 * @param source
	 * @return
	 */
	public static String filterHTML(String source) {
		return source.replaceAll("(?i)<.*?>", "");
	}

	/**
	 * 过滤内容中的security值,默认security为32位
	 * 
	 * @param content
	 * @return
	 */
	public static String filterSecurity(String content) {
		if (content == null || content.length() < 32)
			return content;
		return content.replaceAll("([\\&\\?])s=[\\p{ASCII}&&[^&\"\\]]]*", "$1");
	}

	/**
	 * 过滤不可见字符,包括换行空格等
	 * 
	 * @param source
	 * @return
	 */
	public static String filterUnvisibleChar(String source) {
		if (source == null) {
			return source;
		}
		return source.replaceAll("[^\\p{Graph}&&[^\u4e00-\u9fa5]]", "");
	}
}
