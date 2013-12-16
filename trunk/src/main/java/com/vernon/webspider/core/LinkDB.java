package com.vernon.webspider.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/16/13
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public class LinkDB {


    /**
     * 已访问的url集合
     */
    private Set<String> visitedUrls = new HashSet<String>();
    /**
     * 待访问的url队列
     */
    private CustomizeQueue<String> unVisitedUrls = new CustomizeQueue<String>();

    public CustomizeQueue<String> getUnVisitedUrls() {
        return unVisitedUrls;
    }

    /**
     * 添加已访问url
     *
     * @param url String
     */
    public void addVisitedUrl(String url) {
        visitedUrls.add(url);
    }

    /**
     * 删除已访问url
     *
     * @param url String
     */
    public void removeVisitedUrl(String url) {
        visitedUrls.remove(url);
    }

    /**
     * 获取待访问url的首个url
     *
     * @return String
     */
    public String unVisitedUrlsDeQueue() {
        return unVisitedUrls.deQueue();
    }

    /**
     * 获取已访问url的个数
     *
     * @return int
     */
    public int getVisitedUrlsSize() {
        return visitedUrls.size();
    }

    /**
     * 待访问url是否为空
     *
     * @return boolean
     */
    public boolean unVisitedUrlsIsEmpty() {
        return unVisitedUrls.isEmpty();
    }

    /**
     * 添加待访问url,并保证url只被访问一次
     *
     * @param url String
     */
    public void addUnvisitedUrl(String url) {
        if (null != url && !url.trim().equals("") && !visitedUrls.contains(url) && !unVisitedUrls.contains(url))
            unVisitedUrls.enQueue(url);
    }

}
