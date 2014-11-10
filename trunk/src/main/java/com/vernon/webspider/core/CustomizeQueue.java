package com.vernon.webspider.core;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/16/13
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public class CustomizeQueue<T> {

    private LinkedList<T> queue = new LinkedList<T>();

    public void enQueue(T t) {
        queue.addLast(t);
    }

    public T deQueue() {
        return queue.removeFirst();
    }

    public boolean contains(T t) {
        return queue.contains(t);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public static void main(String[] args) {
        LinkedList<String> queue = new LinkedList<String>();
        queue.add("I am first");
        queue.add("I am second");
        queue.add("I am third");
        String result = queue.removeFirst();
        System.out.println("result : " + result);
    }

}
