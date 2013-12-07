/**
 * 
 */
package com.vernon.webspider.core.http;

/**
 * 字符编码定义
 * 
 * @author Vernon.Chen
 *
 */
public enum Charset {

	GBK("gbk"), UTF8("utf-8"), GB2312("gb2312"), GB18030("gb18030"), ISO8859_1("iso-8859-1");

	private String value = "gbk";

	Charset(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public static Charset fromValue(String value) {
		Charset charset = Charset.GBK;
		if (value == null) {
			return charset;
		}
		for (Charset c : Charset.values()) {
			if (c.value.equalsIgnoreCase(value)) {
				charset = c;
				break;
			}
		}
		return charset;
	}
}
