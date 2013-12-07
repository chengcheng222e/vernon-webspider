package com.vernon.webspider.book.domain;

import java.util.Arrays;
import java.util.Date;

/**
 * 图片信息类
 * 
 * @author Vernon.Chen
 * @date 2012-8-3
 */
public class ImageInfo {

	String imageName; //图片对应的名称
	byte[] imageByte; //图片对应的字节
	String urlAddress; //图片实际所在位置
	int position; //图片相对所在的位置
	int sequence; //图片所在序列号
	String description; //图片描述
	Date publish; //图片上传时间
	String fromUrl; //图片具体位置

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public byte[] getImageByte() {
		return imageByte;
	}

	public void setImageByte(byte[] imageByte) {
		this.imageByte = imageByte;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublish() {
		return publish;
	}

	public void setPublish(Date publish) {
		this.publish = publish;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	@Override
	public String toString() {
		return "ImageInfo [imageName=" + imageName + ", imageByte=" + Arrays.toString(imageByte) + ", urlAddress="
				+ urlAddress + ", position=" + position + ", sequence=" + sequence + ", description=" + description
				+ ", publish=" + publish + ", fromUrl=" + fromUrl + "]";
	}

}
