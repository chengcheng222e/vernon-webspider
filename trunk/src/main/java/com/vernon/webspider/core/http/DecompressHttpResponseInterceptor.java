/**
 * 
 */
package com.vernon.webspider.core.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * 压缩格式拦截器
 * 
 * @author Vernon.Chen
 *
 */
class DecompressHttpResponseInterceptor
	implements HttpResponseInterceptor {

	/*
	 * (non-Javadoc)
	 * @see org.apache.http.HttpResponseInterceptor#process(org.apache.http.HttpResponse, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public void process(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		HttpEntity entity = response.getEntity();
		Header ceheader = entity.getContentEncoding();
		if (ceheader != null) {
			HeaderElement[] codecs = ceheader.getElements();
			for (int i = 0; i < codecs.length; i++) {
				if (codecs[i].getName().equalsIgnoreCase("gzip")) {
					response.setEntity(new GzipDecompressEntity(response.getEntity()));
				} else if (codecs[i].getName().equalsIgnoreCase("inflater")) {
					response.setEntity(new InflaterDecompressEntity(response.getEntity()));
				}
			}
		}
	}

}
