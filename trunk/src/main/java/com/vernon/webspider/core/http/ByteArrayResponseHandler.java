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
 * 
 * 转换为字节数组返回的响应器
 * 
 * @author Vernon.Chen
 *
 */
class ByteArrayResponseHandler
	implements ResponseHandler<Pair<Integer, byte[]>> {
	/*
	 * (non-Javadoc)
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	public Pair<Integer, byte[]> handleResponse(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			return new Pair<Integer, byte[]>(response.getStatusLine().getStatusCode(), EntityUtils.toByteArray(entity));
		} else {
			return new Pair<Integer, byte[]>(response.getStatusLine().getStatusCode(), null);
		}
	}
}
