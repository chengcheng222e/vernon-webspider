package com.vernon.webspider.core;

/**
 * 抓取站点（枚举）
 *
 * @author zaniel.zhao
 */
public enum SpiderSite {

	TECENT(1), //腾讯
	SOHU(2), //搜狐
	SINA(3); //新浪

	private final int value;

	SpiderSite(int value) {
		this.value = value;
	}

	/** @return the value */
	public final int getValue() {
		return value;
	}

	public static SpiderSite fromValue(int value) {
		SpiderSite status = SpiderSite.TECENT;
		for (SpiderSite s : SpiderSite.values()) {
			if (value == s.value) {
				status = s;
				break;
			}
		}
		return status;
	}
}
