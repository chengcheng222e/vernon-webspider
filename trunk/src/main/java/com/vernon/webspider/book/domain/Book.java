/**
 * Book.java,2010-9-8
 *
 * Copyright 2010 A8 Digital Music Holdings Limited. All Rights Reserved.
 */
package com.vernon.webspider.book.domain;

import com.vernon.webspider.book.domain.base.BaseBook;

import java.io.Serializable;

/**
 * 书籍信息
 * 
 * @author Vernon.Chen
 * @date 2012-8-6
 */
@SuppressWarnings("serial")
public class Book
		extends BaseBook {
	
	private String authorName;
	private String categoryTitle;
	private String imgUrl;

	//是否推荐，后台用(月宫推荐)
	private boolean rank;

	//(爱看书推荐)
	private boolean rankNM;

	//(超过两个月未更新的书进入状态审核)
	private boolean over;

	//判断书籍是否有版权
	public boolean getCopyrights() {
		return getAuthorizedState() > 0;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	public boolean isRank() {
		return rank;
	}

	public void setRank(boolean rank) {
		this.rank = rank;
	}

	public boolean isRankNM() {
		return rankNM;
	}

	public void setRankNM(boolean rankNM) {
		this.rankNM = rankNM;
	}

	/**
	 * 获取抓取站点名
	 *
	 * @return String
	 */
	public String getSpiderSiteName() {
		if (getSpiderSite() == 1) {
			return "小说家";
		} else if (getSpiderSite() == 2) {
			return "亲亲小说";
		} else if (getSpiderSite() == 3) {
			return "16K";
		} else {
			return "未取名";
		}
	}

	/**
	 * 获取写作进程
	 *
	 * @return String
	 */
	public String getStateName() {
		return State.getStateNameFromValue(getState());
	}

	// -------------------------- ENUMERATIONS --------------------------

	public enum AuthorizedState {
		ZHUANZAI, AUTHORIZED
	}

	public enum State {
		OVER {
			@Override
			String getStateName() {
				return "完结";
			}
		},
		WRITING {
			@Override
			String getStateName() {
				return "连载";
			}
		},
		STOP {
			@Override
			String getStateName() {
				return "暂停";
			}
		};

		abstract String getStateName();

		public static String getStateNameFromValue(int value) {
			State state = State.WRITING;
			for (State s : State.values()) {
				if (s.ordinal() == value) {
					state = s;
					break;
				}
			}
			return state.getStateName();
		}
	}

	// -------------------------- INNER CLASSES --------------------------

	/** 缓存对象 */
	public static class CacheBean
		implements Serializable {
		private int bookId;
		private int recom;
		private int hits;

		public int getBookId() {
			return bookId;
		}

		public void setBookId(int bookId) {
			this.bookId = bookId;
		}

		public int getRecom() {
			return recom;
		}

		public void setRecom(int recom) {
			this.recom = recom;
		}

		public int getHits() {
			return hits;
		}

		public void setHits(int hits) {
			this.hits = hits;
		}

		@Override
		public String toString() {
			return "CacheBean{" + "bookId=" + bookId + ", recom=" + recom + ", hits=" + hits + '}';
		}
	}
}
