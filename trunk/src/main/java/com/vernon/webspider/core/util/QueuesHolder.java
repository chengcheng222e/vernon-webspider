package com.vernon.webspider.core.util;

import com.google.common.collect.MapMaker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public class QueuesHolder {

    private static ConcurrentMap<String, BlockingQueue> queueMap = new MapMaker().concurrencyLevel(32).makeMap();
    private static int queueSize = Integer.MAX_VALUE;

    /**
     * 根据queueName获得消息队列的静态函数.
     * 如消息队列还不存在, 会自动进行创建.
     *
     * @param queueName queueName
     * @return BlockingQueue
     */
    public static <T> BlockingQueue<T> getQueue(String queueName) {
        BlockingQueue queue = queueMap.get(queueName);
        if (queue == null) {
            BlockingQueue newQueue = new LinkedBlockingQueue(queueSize);
            // 如果之前消息队列还不存在,放入新队列并返回Null.否则返回之前的值.
            queue = queueMap.putIfAbsent(queueName, newQueue);
            if (queue == null) {
                queue = newQueue;
            }
        }
        return queue;
    }

    public static int getQueueLength(String queueName) {
        return getQueue(queueName).size();
    }

    /**
     * 设置每个队列的最大长度, 默认为Integer最大值, 设置时不改变已创建队列的最大长度.
     *
     * @param queueSize 大小
     */
    public static void setQueueSize(int queueSize) {
        QueuesHolder.queueSize = queueSize;
    }
}
