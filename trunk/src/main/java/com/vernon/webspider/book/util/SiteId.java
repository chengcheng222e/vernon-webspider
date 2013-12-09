package com.vernon.webspider.book.util;

import com.vernon.webspider.book.domain.Category;

/**
 * 网站设定,利用枚举方便管理.扩张
 *
 * @author Vernon.Chen
 * @date 2012-8-8
 */
public enum SiteId {
    NONE {
        public int getCategoryId(String name) {
            return 0;
        }

        public String getDomain() {
            return null;
        }
    },

    QIQISHU {
        public static final String host = "http://www.77shu.com";

        public String getDomain() {
            return host;
        }

        public int getCategoryId(String name) {
            // 玄幻魔法
            if (name.equals("玄幻魔法") || name.equals("现代修真") || name.equals("异术超能")
                    || name.equals("异世大陆") || name.equals("奇幻架空") || name.equals("西方奇幻")
                    || name.equals("转世重生") || name.equals("王朝争霸") || name.equals("远古神话")
                    || name.equals("魔法校园") || name.equals("奇幻玄幻") || name.equals("吸血家族")
                    || name.equals("变身情缘") || name.equals("东方玄幻")
                    ) {
                return Category.BookCategory.XH.ordinal();
            }

            // 武侠修真
            else if (name.equals("武侠仙侠") || name.equals("武侠修真") || name.equals("浪子异侠")
                    || name.equals("传统武侠") || name.equals("古典仙侠") || name.equals("历史武侠")
                    || name.equals("谐趣武侠")) {
                return Category.BookCategory.WX.ordinal();
            }

            // 都市言情
            else if (name.equals("快意江湖") || name.equals("千千心结") || name.equals("品味人生")) {
                return Category.BookCategory.YQ.ordinal();
            }

            // 网游动漫
            else if (name.equals("游戏生涯") || name.equals("网游动漫") || name.equals("虚拟网游")
                    || name.equals("足球运动") || name.equals("篮球运动") || name.equals("电子竞技")
                    || name.equals("体育竞技") || name.equals("弈林生涯")) {
                return Category.BookCategory.WY.ordinal();
            }

            // 侦探推理
            else if (name.equals("侦探推理") || name.equals("推理侦探")) {
                return Category.BookCategory.ZT.ordinal();
            }

            // 恐怖灵异
            else if (name.equals("恐怖灵异") || name.equals("灵异神怪") || name.equals("恐怖惊悚")) {
                return Category.BookCategory.LY.ordinal();
            }

            // 历史军事
            else if (name.equals("历史军事") || name.equals("架空历史") || name.equals("战争幻想")
                    || name.equals("三国梦想") || name.equals("历史传记") || name.equals("现代战争")
                    || name.equals("人文历史") || name.equals("特种军旅")) {
                return Category.BookCategory.LS.ordinal();
            }

            // 科幻小说
            else if (name.equals("机器时代") || name.equals("骇客时空") || name.equals("星际战争")) {
                return Category.BookCategory.KH.ordinal();
            }

            // 其他
            else {
                return Category.BookCategory.QT.ordinal();
            }
        }
    };

    /**
     * 获取类型ID
     *
     * @param name
     * @return
     */
    public abstract int getCategoryId(String name);

    /**
     * 获取域名
     *
     * @return
     */
    public abstract String getDomain();
}
