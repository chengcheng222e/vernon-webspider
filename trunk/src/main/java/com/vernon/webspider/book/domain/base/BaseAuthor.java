package com.vernon.webspider.book.domain.base;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class BaseAuthor {

    // ------------------------------ FIELDS ------------------------------

    private int authorId;
    private String name;
    private int type;
    private int hits;

    // --------------------- GETTER / SETTER METHODS ---------------------

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
