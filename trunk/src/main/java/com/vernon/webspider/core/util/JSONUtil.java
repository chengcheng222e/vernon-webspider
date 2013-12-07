package com.vernon.webspider.core.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 包含操作 JSON数据的常用方法的工具类。
 * 
 * @author Vernon.Chen
 *
 */
public class JSONUtil {

	private static final Logger logger = Logger.getLogger(JSONUtil.class);

	private static final String EMPTY_JSON = "{}";

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final Map<String, Gson> _CACHE = new HashMap<String, Gson>();

	/**
	 * 获取gson对象
	 * 
	 * @param dataformat
	 * @return
	 */
	protected static final Gson getGson(String dataformat) {
		try {
			Gson g = _CACHE.get(dataformat);
			if (g == null) {
				GsonBuilder builder = new GsonBuilder();
				builder.setDateFormat(dataformat);
				g = builder.create();
				_CACHE.put(dataformat, g);
			}
			return g;
		} catch (Exception e) {
			logger.error("get Gson Object Exception!", e);
		}
		return null;
	}

	/**
	 * Object to JSON
	 * 
	 * @param obj
	 * @return
	 */
	public static final String toJSON(Object obj) {
		return toJSON(obj, DEFAULT_DATE_FORMAT);
	}

	/**
	 * Object to JSON
	 * 
	 * @param obj
	 * @param dateformat
	 * @return
	 */
	public static final String toJSON(Object obj, String dateformat) {
		long t1 = 0, t2 = 0, t3 = 0;
		String json = EMPTY_JSON;
		try {
			t1 = System.currentTimeMillis();
			if (dateformat == null) {
				dateformat = DEFAULT_DATE_FORMAT;
			}
			Gson g = getGson(dateformat);
			t2 = System.currentTimeMillis();
			json = g.toJson(obj);
			t3 = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("Object " + obj.getClass().getName() + " to json string exception!", e);
		}
		if (t3 - t1 > 100) {
			logger.error("tJSOn slow " + (t2 - t1) + "ms " + (t3 - t2) + "ms");
		}
		return json;
	}

	/**
	 * JSON to Object
	 * 
	 * @param <T>
	 * @param json
	 * @param cls
	 * @return
	 */
	public static final <T> T fromJSON(String json, Class<T> cls) {
		return fromJSON(json, cls, DEFAULT_DATE_FORMAT);
	}

	/**
	 * JSON to Object
	 * 
	 * @param <T>
	 * @param json
	 * @param cls
	 * @return
	 */
	public static final <T> T fromJSON(String json, Class<T> cls, String dateformat) {
		long t1 = 0, t2 = 0, t3 = 0;
		T obj = null;
		try {
			t1 = System.currentTimeMillis();
			if (dateformat == null) {
				dateformat = DEFAULT_DATE_FORMAT;
			}
			Gson g = getGson(dateformat);
			t2 = System.currentTimeMillis();
			obj = g.fromJson(json, cls);
			t3 = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("json string to Object " + cls.getName() + " exception！", e);
		}
		if (t3 - t1 > 100) {
			logger.error("fromJSON slow " + (t2 - t1) + "ms " + (t3 - t2) + "ms");
		}
		return obj;
	}

}