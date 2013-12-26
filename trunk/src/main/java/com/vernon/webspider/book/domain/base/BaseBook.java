package com.vernon.webspider.book.domain.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 书籍信息
 *
 * @author Vernon.Chen
 * @date 2012-8-6
 */
public class BaseBook implements Serializable {

    private int bookId; // 书ID
    private int categoryId;// 种类ID
    private String title;// 标题
    private int authorId;// 作者ID
    private String tag;// 标签
    private String role;// 角色
    private String intro;// 介绍
    private int len;// 长度
    private int recom;// 推荐
    private int hits;// 点击数
    private int price;// 价格
    private Date createTime;// 创建时间
    private int state;// 状态
    private boolean vip;// 是否是VIP
    private int authorizedState;//
    private boolean original;// 来源, 内网|外网
    private int chapterNum;// 章节数
    private int lastChapterId;//最后的章节数
    private Date updateTime;// 更新时间
    private boolean spiderState;// 抓取状态
    private boolean display;// 是否显示
    private String spiderUrl;// 抓取的网页地址
    private String menuSpiderUrl;
    private int spiderSite;// 书来源的编号,比如2:亲亲小说
    private boolean onOff;// 开关
    private int checkLevel;// 检查等级
    private Date checkTime;// 检查时间
    private boolean inClient;// 是否在客户端


    public boolean isInClient() {
        return inClient;
    }

    public void setInClient(boolean inClient) {
        this.inClient = inClient;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getAuthorizedState() {
        return authorizedState;
    }

    public void setAuthorizedState(int authorizedState) {
        this.authorizedState = authorizedState;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getLastChapterId() {
        return lastChapterId;
    }

    public void setLastChapterId(int lastChapterId) {
        this.lastChapterId = lastChapterId;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getMenuSpiderUrl() {
        return menuSpiderUrl;
    }

    public void setMenuSpiderUrl(String menuSpiderUrl) {
        this.menuSpiderUrl = menuSpiderUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRecom() {
        return recom;
    }

    public void setRecom(int recom) {
        this.recom = recom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getSpiderSite() {
        return spiderSite;
    }

    public void setSpiderSite(int spiderSite) {
        this.spiderSite = spiderSite;
    }

    public String getSpiderUrl() {
        return spiderUrl;
    }

    public void setSpiderUrl(String spiderUrl) {
        this.spiderUrl = spiderUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date time) {
        this.updateTime = time;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public boolean isSpiderState() {
        return spiderState;
    }

    public void setSpiderState(boolean spiderState) {
        this.spiderState = spiderState;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    @Override
    public String toString() {
        return "BaseBook{" +
                "authorId=" + authorId +
                ", bookId=" + bookId +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", role='" + role + '\'' +
                ", intro='" + intro + '\'' +
                ", len=" + len +
                ", recom=" + recom +
                ", hits=" + hits +
                ", price=" + price +
                ", createTime=" + createTime +
                ", state=" + state +
                ", vip=" + vip +
                ", authorizedState=" + authorizedState +
                ", original=" + original +
                ", chapterNum=" + chapterNum +
                ", lastChapterId=" + lastChapterId +
                ", updateTime=" + updateTime +
                ", spiderState=" + spiderState +
                ", display=" + display +
                ", spiderUrl='" + spiderUrl + '\'' +
                ", menuSpiderUrl='" + menuSpiderUrl + '\'' +
                ", spiderSite=" + spiderSite +
                ", onOff=" + onOff +
                ", checkLevel=" + checkLevel +
                ", checkTime=" + checkTime +
                ", inClient=" + inClient +
                '}';
    }
}
