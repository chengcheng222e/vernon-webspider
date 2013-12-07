/**
 *
 */
package com.vernon.webspider.core.util;


import java.io.*;
import java.util.Date;
import java.util.Random;

/**
 * 资源工具
 * 
 * @author Vernon.Chen
 *
 */
@SuppressWarnings("serial")
public class ResourceUtil
        implements Serializable {

    public enum Type {
		SPACE("space"), //
		BBS("bbs"), //
		ALBUM("album"), //
		NEWS("news"), //
		MOVIE("movie"), //
		TV("tv"), //
		PERFORMANCE("performance"), //
		STARS("stars"), //
		CHAT("chat"), //
		IM("im"), //
		MUSIC("music"), //
		BOOK("book"), //
		PICTURE("picture"), //
		ANIME("anime"), //
		PAYMUSIC("paymusic"), //
		OTHER("other");

        private String dir;

        Type(String dir) {
            this.dir = dir;
        }

        /**
         *  @return the dir 
         */
        public String getDir() {
            return this.dir;
        }

    }

    private static final int factor = 16;

    /**
     * 获取资源文件的URI地址
     *
     * @param file 资源文件
     * @return String
     */
    public static String getURL(File file) {
        if (file == null) {
            return null;
        }
        return getURL(file.toString());
    }

    /**
     * 获取资源文件的URI地址
     *
     * @param file 资源文件路径
     * @return String
     */
    public static String getURL(String file) {
        if (file == null) {
            return null;
        }
        if (file.startsWith(Constant.ICON_BASE_DIR)) {
            return IconUtil.getURL(file);
        }
        return file.replace("\\", "/").replace(Constant.RESOURCE_BASE_DIR, Constant.RESOURCE_BASE_URL);
    }

    /**
     * 获取扩展名
     *
     * @param fileName 文件名
     * @return String
     */
    public static String getExt(String fileName) {
        if (fileName == null) {
            return null;
        }
        if (fileName.lastIndexOf(".") < 0) {
            return null;
        }
        fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (fileName.indexOf("?") > 0) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName.toLowerCase();
    }

    /**
     * 获取目录
     *
     * @return String
     */
    public static String getDir() {
        return DateUtil.date2String(new Date(), "yyyyMMdd");
    }

    /**
     * 获取随机目录
     *
     * @param factor 随机种子
     * @return String
     */
    public static String getSplitDir(int factor) {
        return "" + new Random().nextInt(factor);
    }

    /**
     * 获取目录
     *
     * @param fileName 文件路径
     * @return String
     */
    public static String getDir(String fileName) {
        File file = new File(fileName);
        String filepath = file.getPath();
        int ext = filepath.indexOf(".");
        if (ext > 0) {
            filepath = filepath.substring(0, ext);
            int pathSeparator = filepath.lastIndexOf(File.separator);
            if (pathSeparator > 0) {
                filepath = filepath.substring(0, pathSeparator);
            }
        }
        return filepath;
    }

    /**
     * 获取文件名称
     *
     * @return String
     */
    public static String getFileName() {
        return Long.toHexString(System.currentTimeMillis());
    }

    /**
     * 获取文件名
     *
     * @param fileName 文件路径
     * @return String
     */
    public static String getFileName(String fileName) {
        File file = new File(fileName);
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index > 0) {
            return name.substring(0, index);
        }
        return name;
    }

    /**
     * 写文件
     *
     * @param fileName 文件路径
     * @param type     类型
     * @param buff     文件byte[]
     * @return File
     */
    public static File writeResource(String fileName, Type type, byte[] buff) {
        FileOutputStream os = null;
        try {
            File file = getResource(fileName, type);// 这里需要获取资源文件
            if (file == null) {
                return null;
            }
            os = new FileOutputStream(file);
            os.write(buff);
            os.flush();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 写文件
     *
     * @param fileName 文件路径
     * @param type     类型
     * @param in       流
     * @return File
     */
    public static File writeResource(String fileName, Type type, InputStream in) {
        FileOutputStream os = null;
        try {
            if (in == null) {
                return null;
            }
            File file = getResource(fileName, type);
            if (file == null) {
                return null;
            }
            os = new FileOutputStream(file);
            byte[] buff = new byte[1024 * 8];
            int len = 0;
            while ((len = in.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            os.flush();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取资源文件
     *
     * @param fileName 文件名
     * @param type     类型
     * @return File
     */
    public static File getResource(String fileName, Type type) {
        String name = getFileName();
        String dir = getDir();
        String ext = getExt(fileName);
        String split = getSplitDir(factor);
        File pathfile = new File(Constant.RESOURCE_BASE_DIR, type.getDir());
        pathfile = new File(pathfile, dir);
        pathfile = new File(pathfile, ext);
        pathfile = new File(pathfile, split);
        if (!pathfile.exists()) {
            pathfile.mkdirs();
        }
        File file = new File(pathfile, name + "." + ext);
        if (file.exists()) {
            file = getResource(fileName, type);
        }
        return file;
    }

    public static void main(String[] args) {
        File file = ResourceUtil.getResource("x.jpg", Type.ALBUM);
        System.out.println(file.getPath());
    }

}
