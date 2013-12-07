/**
 * BaseCover.java,2010-10-25
 *
 * Copyright 2010 A8 Digital Music Holdings Limited. All Rights Reserved.
 */
package com.vernon.webspider.book.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 小说封面
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
@SuppressWarnings("serial")
public class BaseCover
	implements Serializable {

	private int bookId; // 书Id
	private String name;// 封面书名
	private String filePath;// 文件路径
	private String fileName;// 文件名
	private int fileSize;// 文件大小
	private String fileType;// 文件类型
	private String spiderUrl; // 文件地址
	private boolean spiderState;// 抓取状态
	private Date createTime;// 创建时间
	private int checkLevel;// 等级
	private Date checkTime;// 审核时间

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getCheckLevel() {
		return checkLevel;
	}

	public void setCheckLevel(int checkLevel) {
		this.checkLevel = checkLevel;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date time) {
		this.checkTime = time;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date time) {
		this.createTime = time;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpiderUrl() {
		return spiderUrl;
	}

	public void setSpiderUrl(String spiderUrl) {
		this.spiderUrl = spiderUrl;
	}

	public boolean isSpiderState() {
		return spiderState;
	}

	public void setSpiderState(boolean spiderState) {
		this.spiderState = spiderState;
	}

	@Override
	public String toString() {
		return "BaseCover [bookId=" + bookId + ", name=" + name + ", filePath=" + filePath + ", fileName=" + fileName
				+ ", fileSize=" + fileSize + ", fileType=" + fileType + ", spiderUrl=" + spiderUrl + ", spiderState="
				+ spiderState + ", createTime=" + createTime + ", checkLevel=" + checkLevel + ", checkTime="
				+ checkTime + "]";
	}

}
