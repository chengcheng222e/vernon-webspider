package com.vernon.webspider.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 链接过滤
 * 
 * @author Vernon.Chen
 * @date 2013-4-8
 */
public class LinkFilter {

	private String regex;

	public LinkFilter(String regex) {
		this.regex = "(?i)" + regex;
	}

	/**
	 * 是否符合规则
	 * 
	 * @param url
	 * @return
	 */
	public boolean accept(String url) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	public static void main(String[] args) {
		String link = "http://www.77shu.com/view/10/10350/3254630.html" ;
		LinkFilter filter = new LinkFilter("http://www.77shu.com/view/\\d+/\\d+/\\d+\\.html");
		boolean res = filter.accept(link);
		System.out.println(res);
	}
}
