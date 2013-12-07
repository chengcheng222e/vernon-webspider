package com.vernon.webspider.core.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vernon.webspider.core.collection.Pair;

/**
 * URL地址的包裹器
 * 
 * @author Vernon.Chen
 *
 */
public class URLWrap {
	
	// ---------------------------------- FIELD NAME ----------------------------------
	
	/** 地址 */
	private URL url = null;

	/** 参数 */
	private Map<String, String> parameters = new HashMap<String, String>();

	// ---------------------------------- CONSTRUCT METHOD -----------------------------
	
	/**
	 * construct method
	 * 
	 * @param urlStr
	 * @throws MalformedURLException
	 */
	public URLWrap(String urlStr) throws MalformedURLException {
		if (urlStr != null && urlStr.length() > 0) {
			this.url = new URL(urlStr);
		}
		List<Pair<String, String>> pairs = getURLParameters();
		if (pairs != null) {
			for (Pair<String, String> pair : pairs) {
				parameters.put(pair.getKey(), pair.getValue());
			}
		}
	}

	// ---------------------------------- OTHER METHOD -----------------------------
	
	/**
	 * 获取参数列表
	 * 
	 * @return
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public void putParameter(String key, String value) {
		parameters.put(key, value);
	}

	/**
	 * 删除参数
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String removeParameter(String key, String value) {
		return parameters.remove(key);
	}

	/**
	 * get root and path by url string
	 * 
	 * @return
	 */
	public String getURLRootAndPath() {
		return this.getURLRoot() + this.getPath();
	}

	/**
	 * get file name by url string
	 * 
	 * @return
	 */
	public String getFile() {
		return url.getFile();
	}

	/**
	 * get protocol by url string
	 * 
	 * @return
	 */
	public String getProtocol() {
		return url.getProtocol();
	}

	/**
	 * get host address by url string
	 * 
	 * @return
	 */
	public String getHost() {
		return url.getHost();
	}

	/**
	 * get query parameter by url string
	 * 
	 * @return
	 */
	public String getQuery() {
		return url.getQuery();
	}

	/**
	 * get port by url string
	 * 
	 * @return
	 */
	public int getPort() {
		return url.getPort() > 0 ? url.getPort() : 80;
	}

	/**
	 * get path by url string
	 * 
	 * @return
	 */
	public String getPath() {
		return url.getPath();
	}

	/**
	 * get filename by url string
	 * 
	 * @return
	 */
	public String getFileName() {
		String retStr = "";
		String file = this.getFile();
		if (file.lastIndexOf("/") == -1) {
			return "";
		}
		if (file.lastIndexOf("?") != -1) {
			retStr = file.substring(file.lastIndexOf("/") + 1, file.indexOf("?"));
		} else {
			retStr = file.substring(file.lastIndexOf("/") + 1, file.length());
		}
		return retStr;
	}

	/**
	 * get url base by url string
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getURLBase() {
		String urlBase = "";
		urlBase += this.getProtocol() + "://";
		urlBase += this.getHost();
		if (this.getPort() != -1 && this.getPort() != 80) {
			urlBase += ":" + this.getPort();
		}
		String urlfile = this.getFile();
		int pos = urlfile.lastIndexOf("/") > urlfile.lastIndexOf("\\") ? urlfile.lastIndexOf("/") : urlfile
				.lastIndexOf("\\");
		if (pos < 0) {
			pos = urlfile.length();
		}
		urlBase += urlfile.substring(0, pos) + "/";
		return urlBase;
	}

	/**
	 * get url parameter by url string
	 * 
	 * @return
	 */
	private List<Pair<String, String>> getURLParameters() {
		List<Pair<String, String>> retList = new ArrayList<Pair<String, String>>();
		if (this.getQuery() != null && this.getQuery().length() > 0) {
			String[] strs = this.getQuery().split("&");
			for (int i = 0; i < strs.length; i++) {
				String[] tmpStr = strs[i].split("=");
				if (tmpStr.length < 2) {
					continue;
				}
				Pair<String, String> pair = new Pair<String, String>(tmpStr[0], tmpStr[1]);
				retList.add(pair);
			}
		}
		return retList;
	}

	/**
	 * get path and query string by url string
	 * 
	 * @return
	 */
	public String getPathAndQuery() {
		return this.getPath() + "?" + this.getQuery();
	}

	/**
	 * get root url by url string
	 * 
	 * @return
	 */
	public String getURLRoot() {
		String urlRoot = "";
		urlRoot += this.getProtocol() + "://";
		urlRoot += this.getHost();
		if (this.getPort() != -1 && this.getPort() != 80) {
			urlRoot += ":" + this.getPort();
		}
		return urlRoot;
	}

	/**
	 * 获取域名
	 * 
	 * @return
	 */
	public String getDomain() {
		String domain = this.getHost();
		if (domain.replaceAll("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", "").length() > 0) {
			if (domain.replaceAll("(.*?)\\.com.cn", "").length() == 0) {
				String tmp = domain.replaceAll("(.*?)\\.com.cn", "$1");
				if (tmp.lastIndexOf(".") > 0) {
					tmp = tmp.substring(tmp.lastIndexOf(".") + 1);
				}
				domain = tmp + ".com.cn";
			} else if (domain.replaceAll("(.*?)\\.net.cn", "").length() == 0) {
				String tmp = domain.replaceAll("(.*?)\\.com.cn", "$1");
				if (tmp.lastIndexOf(".") > 0) {
					tmp = tmp.substring(tmp.lastIndexOf(".") + 1);
				}
				domain = tmp + ".net.cn";
			} else {
				String end = "";
				String start = "";
				if (domain.lastIndexOf(".") > 0) {
					end = domain.substring(domain.lastIndexOf(".") + 1);
					start = domain.substring(0, domain.lastIndexOf("."));
				}

				if (start.lastIndexOf(".") > 0) {
					start = start.substring(start.lastIndexOf(".") + 1);
				}
				domain = start + "." + end;
			}

		}
		return domain;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode += this.url.hashCode();
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof URLWrap)) {
			return false;
		}
		return obj.hashCode() == this.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("URLWrap:\r\n");
		sb.append("\turl\t").append(this.url);
		return sb.toString();
	}

	public static void main(String[] args) {
		try {
//			String urlStr = "http://www.link.tx.com.cn/frdlink/fout.jsp?id=2042&go=111" ;
			String urlStr = "http://www.dianziq.com/wenda/user/userAction!getUser.action?currentId=23160" ;
			URLWrap urlWrap = new URLWrap(urlStr);
			System.out.println("urlStr:" + urlStr);
			System.out.println("protocol:" + urlWrap.getProtocol());
			System.out.println("hostname:" + urlWrap.getHost());
			System.out.println("port:" + urlWrap.getPort());
			System.out.println("path:" + urlWrap.getPath());
			System.out.println("query:" + urlWrap.getQuery());
			System.out.println("PathAndQuery:" + urlWrap.getPathAndQuery());
			System.out.println("domain:" + urlWrap.getDomain());
			System.out.println("urlBase:" + urlWrap.getURLBase());
			System.out.println("urlRoot:" + urlWrap.getURLRoot());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
