/**
 * 
 */
package com.vernon.webspider.core.http;

/**
 * 浏览器类型定义
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public enum Browser {
	OPERA8_ON_WINXP() {
		public String getUA() {
			return "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; zh-cn) Opera 8.50";
		}
	},

	IE7_ON_WINXP() {
		public String getUA() {
			return "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; InfoPath.2;)";
		}
	},
	FIREFOX_ON_WINXP {
		public String getUA() {
			return "Mozilla/5.0 (Windows NT 6.1; rv:20.0) Gecko/20100101 Firefox/20.0" ;
		}
	};
	public abstract String getUA();
}
