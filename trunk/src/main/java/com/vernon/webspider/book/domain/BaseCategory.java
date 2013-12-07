package com.vernon.webspider.book.domain;

import java.io.Serializable;

/**
 * 分类
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
@SuppressWarnings("serial")
public class BaseCategory
	implements Serializable {

	private int categoryId; // 类型Id
	private int parentId; // 父级Id
	private String title; // 标题
	private int priority;// 有限次序
	private boolean display; // 是否显示

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	@Override
	public String toString() {
		return "BaseCategory [categoryId=" + categoryId + ", parentId=" + parentId + ", title=" + title + ", priority="
				+ priority + ", display=" + display + "]";
	}

}
