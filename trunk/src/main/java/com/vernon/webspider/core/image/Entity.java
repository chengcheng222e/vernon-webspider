/**
 * 
 */
package com.vernon.webspider.core.image;

import java.io.Serializable;

/**
 * 
 * 图片压缩的返回对象
 * 
 * @author Vernon.Chen
 *
 */
@SuppressWarnings("serial")
public class Entity
	implements Serializable {
	
	private String code;
	private String URI;
	private String format;
	private int width;
	private int height;
	private int size;
	private float percent;

	/**
	 * @return the percent
	 */
	public final float getPercent() {
		return this.percent;
	}

	/**
	 * @param percent
	 *            the percent to set
	 */
	public final void setPercent(float percent) {
		this.percent = percent;
	}

	/**
	 * @return the code
	 */
	public final String getCode() {
		return this.code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public final void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the uRI
	 */
	public final String getURI() {
		return this.URI;
	}

	/**
	 * @param uRI
	 *            the uRI to set
	 */
	public final void setURI(String uRI) {
		this.URI = uRI;
	}

	/**
	 * @return the format
	 */
	public final String getFormat() {
		return this.format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public final void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the width
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public final void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public final int getHeight() {
		return this.height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public final void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the size
	 */
	public final int getSize() {
		return this.size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public final void setSize(int size) {
		this.size = size;
	}

}
