package com.vernon.webspider.school.extractor.renren;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Project  : RenRen
 * Package  : renren
 * File     : GetDataBase.java
 * Author   : Satikey < lei_d@foxmail.com >
 * Created  : 2010-11-28
 * License  : Apache License 2.0 
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * 
 * Author : Saitkey < lei_d@foxmail.com >
 */
public class GenerateSQL {
	// 构建省的sql文件
	private File province = new File("provice.sql");
	// 构建高校的sql文件
	private File college = new File("college.sql");
	// 构建院系的sql文件
	private File department = new File("department.sql");

	GenerateSQL() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String depUrl = "http://www.renren.com/GetDep.do?id=";
		String allunivs = "http://s.xnimg.cn/a13819/allunivlist.js";
		HttpGet get = new HttpGet(allunivs);
		System.out.println("读取高校信息...");
		StringBuffer sb = new StringBuffer(client.execute(get, responseHandler));
		System.out.println("读取完成...");

		// 对获取的字符串进行处理截取从"provs":到}]},{"id":"01"部分
		String alluinvRegex = "\"provs\":(.*?)]}";
		Pattern pattern = Pattern.compile(alluinvRegex);
		String chn = "";
		Matcher matcher = pattern.matcher(sb.toString());
		matcher.find();
		chn = matcher.group(1);
		// System.out.println(convertFromHex(tmp));

		// 对截取的中国部分按照省市区进行匹配"id":1,"univs" ...... "country_id":0,"name":"台湾"
		String regex2 = "id\":(.*?),\"univs\":(.*?),\"country_id\":0,\"name\":\"(.*?)\"}";
		Pattern pattern2 = Pattern.compile(regex2);
		Matcher matcher2 = pattern2.matcher(chn);
		StringBuilder provsBuilder = new StringBuilder();
		StringBuilder colBuilder = new StringBuilder();
		StringBuilder deparBuilder = new StringBuilder();
		while (matcher2.find()) {
			// 我们项目的sql语句，如果你们数据库不一样，稍微修改一下拉
			provsBuilder.append("insert into province(PROID,PRONAME)values('"
					+ matcher2.group(1) + "','"
					+ convertFromHex(matcher2.group(3)) + "');\n");
			System.out.println("生成-" + convertFromHex(matcher2.group(3))
					+ "-数据库");
			// 取得学校的ID，还有名字 "id":1001,"name":"\u6e05\u534e\u5927\u5b66"
			String colRegex = "id\":(.*?),\"name\":\"(.*?)\"";
			Pattern colPattern = Pattern.compile(colRegex);
			Matcher colMatcher = colPattern.matcher(matcher2.group(2));
			while (colMatcher.find()) {
				colBuilder
						.append("insert into COLLEGE(PROID,COLID,COLNAME)values('"
								+ matcher2.group(1)
								+ "','"
								+ colMatcher.group(1)
								+ "','"
								+ convertFromHex(colMatcher.group(2)) + "');\n");

				System.out.println("生成-" + convertFromHex(colMatcher.group(2))
						+ "-数据库");

				get = new HttpGet(depUrl + colMatcher.group(1));
				ResponseHandler<String> depHandler = new BasicResponseHandler();
				generateDepartment(client.execute(get, depHandler), colMatcher
						.group(1), deparBuilder);
			}

		}
		PrintStream ps = new PrintStream(province);
		ps.print(provsBuilder.toString());
		ps.close();

		PrintStream ps2 = new PrintStream(college);
		ps2.print(colBuilder.toString());
		ps2.close();

		PrintStream ps3 = new PrintStream(department);
		ps3.print(deparBuilder.toString());
		ps3.close();
		System.err.println("\n\n\n完成数据库生成，请打开项目目录查看！");
	}

	// 这个函数用来处理行查询到的高校院系 <option
	// value='&#20013;&#22269;&#35821;&#35328;&#25991;&#23398;&#23398;&#38498;'>&#20013;&#22269;&#35821;&#35328;&#25991;&#23398;&#23398;&#38498;</option>
	public void generateDepartment(String src, String colid, StringBuilder sb) {
		String departRegex = "value='(.+?)'>";// 开始用这个正则表达式"value='(.*?)'>";
		// 后来发现有问题，问题你自己探索吧。
		Pattern pattern = Pattern.compile(departRegex);
		Matcher matcher = pattern.matcher(src);
		while (matcher.find()) {
			sb.append("insert into DEPARTMENT(COLID,DEPNAME)values('" + colid
					+ "','" + convertFromDec(matcher.group(1)) + "');\n");
		}
	}

	public static String convertDec(String src) {
		return Character.toString((char) Integer.parseInt(src, 10));
	}

	public static String convertHex(String src) {
		return Character
				.toString((char) Integer.parseInt(src.substring(2), 16));
	}

	// 转换&#xxxxx;形式Unicode
	private String convertFromDec(String code) {
		StringBuffer sb = new StringBuffer(code);
		int startPos;
		int endPos;
		while ((startPos = sb.indexOf("&#")) > -1) {
			endPos = sb.indexOf(";");
			String tmp = sb.substring(startPos + 2, endPos);
			sb.replace(startPos, endPos + 1, Character.toString((char) Integer
					.parseInt(tmp, 10)));
		}
		return code = sb.toString();
	}

	// 转换16进制的Unicode，
	private String convertFromHex(String code) {
		StringBuffer sb = new StringBuffer(code);
		int pos;
		while ((pos = sb.indexOf("\\u")) > -1) {
			String tmp = sb.substring(pos, pos + 6);
			sb.replace(pos, pos + 6, Character.toString((char) Integer
					.parseInt(tmp.substring(2), 16)));
		}
		return code = sb.toString();
	}

	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		new GenerateSQL();
	}
}
