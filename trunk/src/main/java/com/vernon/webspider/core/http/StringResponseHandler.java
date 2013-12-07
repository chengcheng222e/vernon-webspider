/**
 * 
 */
package com.vernon.webspider.core.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.vernon.webspider.core.collection.Pair;

/**
 * 转换为字符串返回的响应器
 * 
 * @author Vernon.Chen
 *
 */
class StringResponseHandler
	implements ResponseHandler<Pair<Integer, String>> {

	private Charset charset = Charset.UTF8;

	StringResponseHandler(Charset charset) {
		if (charset != null) {
			this.charset = charset;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	public Pair<Integer, String> handleResponse(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String cs = EntityUtils.getContentCharSet(entity) == null ? charset.getValue() : EntityUtils
					.getContentCharSet(entity);
			return new Pair<Integer, String>(response.getStatusLine().getStatusCode(), new String(
					EntityUtils.toByteArray(entity), cs));
		} else {
			return new Pair<Integer, String>(response.getStatusLine().getStatusCode(), null);
		}
	}

}
