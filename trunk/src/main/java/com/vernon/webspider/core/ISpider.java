package com.vernon.webspider.core;

import com.vernon.webspider.core.http.Browser;
import com.vernon.webspider.core.http.Charset;


/**
 * 抓取类接口
 * 
 * @author Vernon.Chen
 * @date 2012-8-3
 */
public interface ISpider {
	
	String spiderPageInfo(Charset charset, Browser browser);

}
