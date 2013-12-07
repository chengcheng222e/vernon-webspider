package com.vernon.webspider.book.domain;

/**
 * 定义组图的解析格式--适合于腾讯网站的组图（json格式）
 * 
 * @author Vernon.Chen
 * @date 2012-8-3
 */
public class GroupViewFormat {

	public String showtit;
	public String showtxt;
	public String smallpic;
	public String bigpic;

	public String getShowtit() {
		return showtit;
	}

	public void setShowtit(String showtit) {
		this.showtit = showtit;
	}

	public String getShowtxt() {
		return showtxt;
	}

	public void setShowtxt(String showtxt) {
		this.showtxt = showtxt;
	}

	public String getSmallpic() {
		return smallpic;
	}

	public void setSmallpic(String smallpic) {
		this.smallpic = smallpic;
	}

	public String getBigpic() {
		return bigpic;
	}

	public void setBigpic(String bigpic) {
		this.bigpic = bigpic;
	}

	@Override
	public String toString() {
		return "GroupViewFormat [showtit=" + showtit + ", showtxt=" + showtxt + ", smallpic=" + smallpic + ", bigpic="
				+ bigpic + "]";
	}

}
