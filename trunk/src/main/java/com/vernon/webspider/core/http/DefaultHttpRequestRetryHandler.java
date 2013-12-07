/**
 * 
 */
package com.vernon.webspider.core.http;

import java.io.IOException;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * 异常处理的句柄
 * 
 * @author Vernon.Chen
 *
 */
class DefaultHttpRequestRetryHandler
	implements HttpRequestRetryHandler {
	/*
	 * (non-Javadoc)
	 * @see org.apache.http.client.HttpRequestRetryHandler#retryRequest(java.io.IOException, int, org.apache.http.protocol.HttpContext)
	 */
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		// 设置恢复策略，在发生异常时候将自动重试3次
		// if (executionCount >= 3) {
		// return false;
		// }
		// if (exception instanceof NoHttpResponseException) {
		// return true;
		// }
		// if (exception instanceof SSLHandshakeException) {
		// return false;
		// }
		// HttpRequest request = (HttpRequest) context
		// .getAttribute(ExecutionContext.HTTP_REQUEST);
		// boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
		// if (!idempotent) {
		// return true;
		// }
		return false;
	}
}
