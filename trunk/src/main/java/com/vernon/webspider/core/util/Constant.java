/**
 * 
 */
package com.vernon.webspider.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 全局常量定义
 * 
 * @author Vernon.Chen
 *
 */
@SuppressWarnings("serial")
public class Constant
	implements Serializable {

	public static String DOMAIN = "F:/";

	public static String SPACE_URL = DOMAIN + "/space/";

	public static String RESOURCE_BASE_DIR = "F:";

	public static String RESOURCE_BASE_URL = "F:";

	public static String CSS_BASE_URL = "F:";

	public static String ICON_BASE_DIR = "F:";

	public static String ICON_BASE_URL = "F:";

	private static Properties properties = new Properties();

	static {
		try {

			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("constant.properties");
			if (urls == null || !urls.hasMoreElements()) {
				System.out.println("no constant config find! please put constant.properties  in your classpath");
			}
			if (urls.hasMoreElements()) {
				String filepath = urls.nextElement().getFile();
				System.out.println("constant filepath:" + filepath);
				properties.load(new FileInputStream(new File(filepath)));
				if (properties.getProperty("DOMAIN") != null) {
					DOMAIN = properties.getProperty("DOMAIN");
					System.out.println("init constant.DOMAIN=" + DOMAIN);
				}

				if (properties.getProperty("SPACE_URL") != null) {
					SPACE_URL = properties.getProperty("SPACE_URL");
					System.out.println("init constant.SPACE_URL=" + SPACE_URL);
				}

				if (properties.getProperty("RESOURCE_BASE_DIR") != null) {
					RESOURCE_BASE_DIR = properties.getProperty("RESOURCE_BASE_DIR");
					System.out.println("init constant.RESOURCE_BASE_DIR=" + RESOURCE_BASE_DIR);
				}

				if (properties.getProperty("CSS_BASE_URL") != null) {
					CSS_BASE_URL = properties.getProperty("CSS_BASE_URL");
					System.out.println("init constant.CSS_BASE_URL=" + CSS_BASE_URL);
				}

				if (properties.getProperty("RESOURCE_BASE_URL") != null) {
					RESOURCE_BASE_URL = properties.getProperty("RESOURCE_BASE_URL");
					System.out.println("init constant.RESOURCE_BASE_URL=" + RESOURCE_BASE_URL);
				}

				if (properties.getProperty("ICON_BASE_DIR") != null) {
					ICON_BASE_DIR = properties.getProperty("ICON_BASE_DIR");
					System.out.println("init constant.ICON_BASE_DIR=" + ICON_BASE_DIR);
				}

				if (properties.getProperty("ICON_BASE_URL") != null) {
					ICON_BASE_URL = properties.getProperty("ICON_BASE_URL");
					System.out.println("init constant.ICON_BASE_URL=" + ICON_BASE_URL);
				}

				System.out.println("constant init success!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return getProperty(key, "");
	}

	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}
