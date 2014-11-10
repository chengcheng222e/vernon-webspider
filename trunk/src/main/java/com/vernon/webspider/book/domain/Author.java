package com.vernon.webspider.book.domain;

import com.vernon.webspider.book.domain.base.BaseAuthor;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 12/23/13
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
public class Author extends BaseAuthor {

    // -------------------------- ENUMERATIONS --------------------------

    public enum AuthorType {
        HOME, HOMELESS
    }

    // -------------------------- INNER CLASSES --------------------------

    /**
     * 缓存对象
     */
    public static class CacheBean {
        private int authorId;
        private int hits;

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

        @Override
        public String toString() {
            return "CacheBean{" + "authorId=" + authorId + ", hits=" + hits + '}';
        }
    }
}
