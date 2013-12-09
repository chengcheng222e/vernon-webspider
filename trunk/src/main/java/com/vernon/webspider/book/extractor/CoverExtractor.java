/**
 * ImageExtractor.java,2010-10-25
 *
 * Copyright 2010 A8 Digital Music Holdings Limited. All Rights Reserved.
 */
package com.vernon.webspider.book.extractor;

import com.vernon.webspider.book.domain.Cover;
import com.vernon.webspider.core.collection.Pair;
import com.vernon.webspider.core.http.Browser;
import com.vernon.webspider.core.http.Charset;
import com.vernon.webspider.core.http.HttpClientUtil;
import com.vernon.webspider.core.http.URLWrap;
import com.vernon.webspider.core.util.ResourceUtil;
import com.vernon.webspider.core.util.ThreadUtil;
import com.vernon.webspider.core.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;

/**
 * 图片抓取分析类
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public class CoverExtractor
		extends Extractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoverExtractor.class);

	private Cover cover;
	private ResourceUtil.Type type;

	@Override
	public Cover extract() throws MalformedURLException {
		LOGGER.info("CoverExtractor extract!");
		try {
            // 当前执行的 URL
			URLWrap wrap = new URLWrap(getInputUrl());
			Pair<Integer, byte[]> result = HttpClientUtil.getByteArrayByGET(wrap, Charset.GB2312, Browser.IE7_ON_WINXP,
					60000);
			if (null != result && 200 == result.getKey()) {
				LOGGER.info("getByteArrayByGET from url: " + getInputUrl() + " success!");
				byte[] imageBytes = result.getValue();
				if (imageBytes.length >= 1000) {
					File srcFile = ResourceUtil.writeResource(getInputUrl(), null != type ? type
							: ResourceUtil.Type.BOOK, imageBytes);

					//此处应当加一个线程休眠时间，待数据写入之后再进行压缩
					ThreadUtil.sleep(1000);
					if (null != srcFile && srcFile.exists()) {
						// **************************************
						// **************************************
						// **************************************
						// **************************************
						// **************************************
						// **************************************
						// **************************************
					}
				}
			} else {
				LOGGER.error("图片生成错误: " + getInputUrl());
			}
		} catch (MalformedURLException e) {
			LOGGER.info("图片url(" + getInputUrl() + ")地址包装错误: " + e);
		}
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}

	public void setType(ResourceUtil.Type type) {
		this.type = type;
	}
}
