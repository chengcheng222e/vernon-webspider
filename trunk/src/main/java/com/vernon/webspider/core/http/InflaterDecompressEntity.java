/**
 * 
 */
package com.vernon.webspider.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * Gzip解压使用
 * 
 * @author Vernon.Chen
 *
 */
class InflaterDecompressEntity
		extends HttpEntityWrapper {

	public InflaterDecompressEntity(HttpEntity entity) {
		super(entity);
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {

		InputStream wrappedin = wrappedEntity.getContent();

		return new InflaterInputStream(wrappedin);
	}

	@Override
	public long getContentLength() {
		return -1;
	}

}
