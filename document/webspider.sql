/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50613
 Source Host           : localhost
 Source Database       : webspider

 Target Server Type    : MySQL
 Target Server Version : 50613
 File Encoding         : utf-8

 Date: 12/19/2013 17:21:52 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `book_author`
-- ----------------------------
DROP TABLE IF EXISTS `book_author`;
CREATE TABLE `book_author` (
  `author_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作者ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  `type` tinyint(4) DEFAULT NULL COMMENT '是否是本站作者 0:本站作者, 1:外站作者',
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `book_book`
-- ----------------------------
DROP TABLE IF EXISTS `book_book`;
CREATE TABLE `book_book` (
  `book_id` int(11) DEFAULT NULL COMMENT '小说ID',
  `category_id` int(11) DEFAULT NULL COMMENT '所属分类',
  `title` varchar(50) DEFAULT NULL COMMENT '小说标题',
  `author_id` int(11) DEFAULT NULL COMMENT '作者ID',
  `tag` varchar(255) DEFAULT NULL COMMENT '标签',
  `role` varchar(32) DEFAULT NULL COMMENT '主角',
  `intro` varchar(1000) DEFAULT NULL COMMENT '小说简介',
  `len` int(11) DEFAULT NULL COMMENT '字数',
  `recom` int(11) DEFAULT NULL COMMENT '推荐',
  `hits` int(11) DEFAULT NULL COMMENT '点击',
  `price` smallint(6) DEFAULT NULL COMMENT '定价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state` tinyint(4) DEFAULT NULL COMMENT '写作进程',
  `vip` bit(1) DEFAULT NULL COMMENT '是否是VIP作品',
  `authorized_state` tinyint(4) DEFAULT '0' COMMENT '授权状态 0:转载作品',
  `original` bit(1) DEFAULT NULL COMMENT '是否是原创',
  `chapter_num` smallint(6) DEFAULT NULL COMMENT '章节数',
  `last_chapter_id` int(11) DEFAULT NULL COMMENT '最近更新章节',
  `update_time` datetime DEFAULT NULL COMMENT '最近更新时间',
  `spider_state` bit(1) DEFAULT NULL COMMENT '更新状况',
  `display` bit(1) DEFAULT NULL COMMENT '是否显示',
  `spider_url` varchar(255) DEFAULT NULL COMMENT '抓取地址',
  `menu_spider_url` varchar(255) DEFAULT NULL,
  `spider_site` varchar(255) DEFAULT NULL,
  `on_off` bit(1) DEFAULT NULL COMMENT '抓取开关',
  `check_level` tinyint(4) DEFAULT NULL COMMENT '审核级别',
  `check_time` datetime DEFAULT NULL COMMENT '审核时间'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `book_category`
-- ----------------------------
DROP TABLE IF EXISTS `book_category`;
CREATE TABLE `book_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父分类ID',
  `title` varchar(32) DEFAULT NULL COMMENT '标题',
  `priority` int(11) DEFAULT NULL COMMENT '排序',
  `dispaly` bit(1) DEFAULT NULL COMMENT '是否显示',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;
