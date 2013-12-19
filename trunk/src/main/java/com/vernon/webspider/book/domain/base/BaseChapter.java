package com.vernon.webspider.book.domain.base;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vernon.Chen
 * Date: 13-12-19
 * Time: 下午6:11
 * To change this template use File | Settings | File Templates.
 */
public class BaseChapter {

    // --------------------------- FIELD NAMES ------------------------------
    private int chapterId;
    private int bookId;
    private String title;
    private int len;
    private boolean vip;
    private boolean free;
    private Date updateTime;
    private boolean spiderState;
    private boolean display;
    private boolean img;
    private String imgUrl;
    private String spiderUrl;
    private int spiderSite;
    private boolean onOff;
    private int checkLevel;
    private Date checkTime;

    // --------------------- GETTER / SETTER METHODS ---------------------

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
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

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isImg() {
        return img;
    }

    public void setImg(boolean img) {
        this.img = img;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
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
        return "BaseChapter{" +
                "chapterId=" + chapterId +
                ", bookId=" + bookId +
                ", title='" + title + '\'' +
                ", len=" + len +
                ", vip=" + vip +
                ", free=" + free +
                ", updateTime=" + updateTime +
                ", spiderState=" + spiderState +
                ", display=" + display +
                ", img=" + img +
                ", imgUrl='" + imgUrl + '\'' +
                ", spiderUrl='" + spiderUrl + '\'' +
                ", spiderSite=" + spiderSite +
                ", onOff=" + onOff +
                ", checkLevel=" + checkLevel +
                ", checkTime=" + checkTime +
                '}';
    }
}
