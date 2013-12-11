/**
 *
 */
package com.vernon.webspider.core.http;

import com.vernon.webspider.core.collection.Pair;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.*;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http客户端的工具类,用于内网的数据通讯及外网的数据抓取使用
 * 
 * @author Vernon.Chen
 *
 */
public class HttpClientUtil {

	private static HttpResponseInterceptor decompressInterceptor = new DecompressHttpResponseInterceptor();

	private static HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler();

	private static ResponseHandler<Pair<Integer, byte[]>> byteArrayResponseHandler = new ByteArrayResponseHandler();

	private static final int DEFAULT_MAX_CONNECTION = 200;

	private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

	private static final int DEFAULT_SO_TIMEOUT = 15000;

	private static HttpClient httpClient = null;

	/**
	 * 静态初始化
	 */
	static {
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, DEFAULT_MAX_CONNECTION);
		ConnManagerParams.setTimeout(params, DEFAULT_CONNECTION_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, DEFAULT_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, DEFAULT_SO_TIMEOUT);
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		// 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		DefaultHttpClient client = new DefaultHttpClient(cm, params);
		client.setHttpRequestRetryHandler(retryHandler);
		client.addResponseInterceptor(decompressInterceptor);
		httpClient = client;
	}
	// 自动释放
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			/*
			 * (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				if (httpClient != null) {
					httpClient.getConnectionManager().shutdown();
				}
			}

		});

	}

	/**
	 * 释放HttpClient连接
	 * @param requestBase 请求对象
	 */
	private static void abortConnection(HttpRequestBase requestBase) {
		if (requestBase != null) {
			requestBase.abort();
		}

	}

	/**
	 * 参数整合
	 * 
	 * @param parameters 参数
	 * @return List<NameValuePair>
	 */
	private static List<NameValuePair> getNameValuePairs(Map<String, String> parameters) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> map : parameters.entrySet()) {
			pairs.add(new BasicNameValuePair(map.getKey(), map.getValue()));
		}
		return pairs;
	}

	private static Header[] getInitHeaders(URLWrap wrap) {
		return new Header[] { new BasicHeader("Accept", "*/*"), new BasicHeader("Accept-Language", "zh-cn,zh;q=0.5"),
				new BasicHeader("Accept-Encoding", "gzip, deflate"),
				new BasicHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7"),
				new BasicHeader("Referer", wrap.getURLRoot()) };
	}

	private static Header[] getInitHeaders(URLWrap wrap, String cookie) {
		return new Header[] { new BasicHeader("Accept", "*/*"), new BasicHeader("Accept-Language", "zh-cn,zh;q=0.5"),
				new BasicHeader("Accept-Encoding", "gzip, deflate"),
				new BasicHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7"),
				new BasicHeader("Referer", wrap.getURLRoot()), new BasicHeader("Cookie", cookie) };
	}

	/**
	 * 获取内容,使用Get方式
	 * 
	 * @param urlWrap 地址包裹器
	 * @param charset 编码
	 * @param browser 浏览器类型
	 * @param timeout 超时时间
	 * @return
	 * @author Vernon.Chen
	 */
	public static Pair<Integer, String> getStringByGET(URLWrap urlWrap, Charset charset, Browser browser, int timeout) {
		if (urlWrap == null) {
			return null;
		}
		// 创建HttpClient实例
		HttpGet get = null;
		// 发送请求，得到响应
		try {
			List<NameValuePair> pairs = getNameValuePairs(urlWrap.getParameters());
			String URL = urlWrap.getURLRootAndPath();
			URL += "?";
			if (charset == null) {
				charset = Charset.UTF8;
			}
			if (pairs != null && pairs.size() > 0) {
				URL += URLEncodedUtils.format(pairs, charset.getValue());
			}
			get = new HttpGet(URL);
            // 暂时就用默认的 getInitHeaders
//            get.setHeader(HttpHeaders.USER_AGENT, browser.getUA());
//            get.setHeader(HttpHeaders.CONTENT_ENCODING, charset.getValue());
//            get.setHeader(HttpHeaders.TIMEOUT, timeout + "");
//            get.setHeader(HttpHeaders.REFERER, urlWrap.getURLRootAndPath());
            get.setHeaders(getInitHeaders(urlWrap));
			return httpClient.execute(get, new StringResponseHandler(charset));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {// 记得一定要释放资源
			abortConnection(get);
		}
		return null;
	}

	/**
	 * 获取内容,使用POST方式
	 * 
	 * @param urlWrap 地址包裹器
	 * @param data post参数数据
	 * @param charset 编码
	 * @param browser 浏览器类型
	 * @param timeout 超时时间
	 * @return
	 */
	public static Pair<Integer, String> getStringByPOST(URLWrap urlWrap, String data, Charset charset, Browser browser,
			int timeout) {
		if (urlWrap == null) {
			return null;
		}
		HttpPost post = null;
		// 发送请求，得到响应
		try {

			List<NameValuePair> pairs = getNameValuePairs(urlWrap.getParameters());
			String URL = urlWrap.getURLRootAndPath();
			URL += "?";
			if (charset == null) {
				charset = Charset.UTF8;
			}
			if (pairs != null && pairs.size() > 0) {
				URL += URLEncodedUtils.format(pairs, charset.getValue());
			}
			post = new HttpPost(URL);
			HttpParams params = post.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			post.setParams(params);
			post.setHeaders(getInitHeaders(urlWrap));

			post.setEntity(new StringEntity(data, charset.getValue()));// post数据
			return httpClient.execute(post, new StringResponseHandler(charset));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(post);
		}
		return null;
	}

	/**
	 * 获取内容,使用POST方式
	 * 
	 * @param urlWrap 地址包裹器
	 * @param data post参数数据
	 * @param charset 编码
	 * @param browser 浏览器类型
	 * @param timeout 超时时间
	 * @return
	 */
	public static Pair<Integer, String> getStringByPOST(URLWrap urlWrap, byte[] data, Charset charset, Browser browser,
			int timeout) {
		if (urlWrap == null) {
			return null;
		}
		HttpPost post = null;
		// 发送请求，得到响应
		try {

			List<NameValuePair> pairs = getNameValuePairs(urlWrap.getParameters());
			String URL = urlWrap.getURLRootAndPath();
			URL += "?";
			if (charset == null) {
				charset = Charset.UTF8;
			}
			if (pairs != null && pairs.size() > 0) {
				URL += URLEncodedUtils.format(pairs, charset.getValue());
			}
			post = new HttpPost(URL);
			HttpParams params = post.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			post.setParams(params);
			post.setHeaders(getInitHeaders(urlWrap));
			post.setEntity(new ByteArrayEntity(data));
			return httpClient.execute(post, new StringResponseHandler(charset));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(post);
		}
		return null;
	}

	/**
	 * 获取内容,使用POST方式
	 * 
	 * @param urlWrap 地址包裹器
	 * @param charset 编码
	 * @param browser 浏览器类型
	 * @param timeout 超时时间
	 * @return
	 */
	public static Pair<Integer, String> getStringByPOST(URLWrap urlWrap, Charset charset, Browser browser, int timeout) {
		if (urlWrap == null) {
			return null;
		}
		UrlEncodedFormEntity formEntity;
		HttpPost post = new HttpPost(urlWrap.getURLRootAndPath());
		// 发送请求，得到响应
		try {
			if (charset == null) {
				formEntity = new UrlEncodedFormEntity(getNameValuePairs(urlWrap.getParameters()));
			} else {
				formEntity = new UrlEncodedFormEntity(getNameValuePairs(urlWrap.getParameters()), charset.getValue());
			}
			HttpParams params = post.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset == null ? Charset.UTF8.getValue()
					: charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			post.setParams(params);
			post.setHeaders(getInitHeaders(urlWrap));
			post.setEntity(formEntity);

			return httpClient.execute(post, new StringResponseHandler(charset));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(post);
		}
		return null;
	}

	/**
	 * 获取byte数组,使用GET方式
	 * 
	 * @param urlWrap
	 * @param charset
	 * @param browser
	 * @param timeout
	 * @return
	 */
	public static Pair<Integer, byte[]> getByteArrayByGET(URLWrap urlWrap, Charset charset, Browser browser, int timeout) {
		if (urlWrap == null) {
			return null;
		}
		HttpGet get = null;
		// 发送请求，得到响应
		try {
			List<NameValuePair> pairs = getNameValuePairs(urlWrap.getParameters());
			String URL = urlWrap.getURLRootAndPath();
			URL += "?";
			if (charset == null) {
				charset = Charset.UTF8;
			}
			if (pairs != null && pairs.size() > 0) {
				URL += URLEncodedUtils.format(pairs, charset.getValue());
			}
			get = new HttpGet(URL);
			HttpParams params = get.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			get.setParams(params);
			get.setHeaders(getInitHeaders(urlWrap));
			return httpClient.execute(get, byteArrayResponseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(get);
		}
		return null;
	}

	/**
	 * 获取Byte数组使用POST方式
	 * 
	 * @param urlWrap
	 * @param charset
	 * @param browser
	 * @param timeout
	 * @return
	 */
	public static Pair<Integer, byte[]> getByteArrayByPOST(URLWrap urlWrap, Charset charset, Browser browser,
			int timeout) {
		if (urlWrap == null) {
			return null;
		}
		UrlEncodedFormEntity formEntity;
		HttpPost post = new HttpPost(urlWrap.getURLRootAndPath());
		// 发送请求，得到响应
		try {

			if (charset == null) {
				formEntity = new UrlEncodedFormEntity(getNameValuePairs(urlWrap.getParameters()));
			} else {
				formEntity = new UrlEncodedFormEntity(getNameValuePairs(urlWrap.getParameters()), charset.getValue());
			}
			HttpParams params = post.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset == null ? Charset.UTF8.getValue()
					: charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			post.setParams(params);
			post.setHeaders(getInitHeaders(urlWrap));
			post.setEntity(formEntity);

			return httpClient.execute(post, byteArrayResponseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(post);
		}
		return null;
	}

	/**
	 * 获取Byte数组使用POST方式
	 * 
	 * @param urlWrap
	 * @param data
	 * @param charset
	 * @param browser
	 * @param timeout
	 * @return
	 */
	public static Pair<Integer, byte[]> getByteArrayByPOST(URLWrap urlWrap, byte[] data, Charset charset,
			Browser browser, int timeout) {
		if (urlWrap == null) {
			return null;
		}
		HttpPost post = null;
		// 发送请求，得到响应
		try {
			List<NameValuePair> pairs = getNameValuePairs(urlWrap.getParameters());
			String URL = urlWrap.getURLRootAndPath();
			URL += "?";
			if (charset == null) {
				charset = Charset.UTF8;
			}
			if (pairs != null && pairs.size() > 0) {
				URL += URLEncodedUtils.format(pairs, charset.getValue());
			}
			post = new HttpPost(URL);
			HttpParams params = post.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			post.setParams(params);
			post.setHeaders(getInitHeaders(urlWrap));
			post.setEntity(new ByteArrayEntity(data));
			return httpClient.execute(post, byteArrayResponseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(post);
		}
		return null;
	}

	/**
	 * 获取Byte数组使用POST方式
	 * 
	 * @param urlWrap
	 * @param data
	 * @param charset
	 * @param browser
	 * @param timeout
	 * @return
	 */
	public static Pair<Integer, byte[]> getByteArrayByPOST(URLWrap urlWrap, String data, Charset charset,
			Browser browser, int timeout) {
		if (urlWrap == null) {
			return null;
		}
		HttpPost post = null;
		// 发送请求，得到响应
		try {
			List<NameValuePair> pairs = getNameValuePairs(urlWrap.getParameters());
			String URL = urlWrap.getURLRootAndPath();
			URL += "?";
			if (charset == null) {
				charset = Charset.UTF8;
			}
			if (pairs != null && pairs.size() > 0) {
				URL += URLEncodedUtils.format(pairs, charset.getValue());
			}
			post = new HttpPost(URL);
			HttpParams params = post.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			post.setParams(params);
			post.setHeaders(getInitHeaders(urlWrap));
			post.setEntity(new StringEntity(data, charset.getValue()));
			return httpClient.execute(post, byteArrayResponseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(post);
		}
		return null;
	}

	@Test
	public void testGetStringByGET() {
		URLWrap urlWrap;
		try {
			urlWrap = new URLWrap("http://dzh.mop.com/shzt/20120729/0/3lFF33I2a42f41FF.shtml?a=1&b=3");
			Pair<Integer, String> pair = HttpClientUtil.getStringByGET(urlWrap, Charset.UTF8, Browser.IE7_ON_WINXP,
					3000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取内容,使用GET方式
	 * 
	 * @param urlWrap 地址包裹器
	 * @param cookies cookie值
	 * @param charset 编码
	 * @param browser 浏览器类型
	 * @param timeout 超时时间
	 * @return
	 */
	public static Pair<Integer, String> getStringByGet(URLWrap urlWrap, String cookies, Charset charset,
			Browser browser, int timeout) {
		if (urlWrap == null) {
			return null;
		}
		// 创建HttpClient实例
		HttpGet get = null;
		// 发送请求，得到响应
		try {
			List<NameValuePair> pairs = getNameValuePairs(urlWrap.getParameters());
			String URL = urlWrap.getURLRootAndPath();
			URL += "?";
			if (charset == null) {
				charset = Charset.UTF8;
			}
			if (pairs != null && pairs.size() > 0) {
				URL += URLEncodedUtils.format(pairs, charset.getValue());
			}
			get = new HttpGet(URL);
			HttpParams params = get.getParams();
			params.setParameter(CoreProtocolPNames.USER_AGENT, browser.getUA());
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset.getValue());
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
			get.setParams(params);
			get.setHeaders(getInitHeaders(urlWrap, cookies));
			return httpClient.execute(get, new StringResponseHandler(charset));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			abortConnection(get);
		}
		return null;
	}

	public static void main(String[] args) throws MalformedURLException {
		String cookies = "anonymid=hdh2g6q3a918l5; _r01_=1; __utma=151146938.1293754952.1363232379.1364963702.1365726753.3; __utmz=151146938.1365726753.3.3.utmcsr=photo.renren.com|utmccn=(referral)|utmcmd=referral|utmcct=/getalbumprofile.do; depovince=GUZ; jebecookies=59c3caad-4f5e-47e3-8bf3-a15d20d0cc20|||||; ick_login=807f3578-4bde-492b-a7df-a11fcb1a02b0; loginfrom=null; XNESSESSIONID=58f35fe9eb3b; vip=1; _de=DD09E06DEBC5E32BC5BC13FF67F7809D696BF75400CE19CC; p=9e1bb1c308afeaee346c0c938e1d95636; ap=234505346; first_login_flag=1; t=9d20b3d42f03cbc59eb5f7f5b19552306; societyguester=9d20b3d42f03cbc59eb5f7f5b19552306; id=234505346; xnsid=2be7bcb";
		URLWrap urlWrap = new URLWrap("http://www.renren.com/234505346/profile?v=info&act=edit");
		Pair<Integer, String> pair = HttpClientUtil.getStringByGet(urlWrap, cookies, Charset.UTF8,
				Browser.FIREFOX_ON_WINXP, 60000);
		if (null != pair && 200 == pair.getKey()) {
			String result = pair.getValue();
			System.out.println(result);
		}
	}
}
