/*
 * $Logfile$
 * $Revision$
 * $Date$
 * $Author$
 * $History$
 *
 * Copyright (c) 2006, 北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.enp.ao;

public class Attachment {

	//文件类型，取扩展名
	private String type;
	private long length;
	private String filename;
	private String filecode;
	private String filepath;
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilecode() {
		return filecode;
	}
	public void setFilecode(String filecode) {
		this.filecode = filecode;
	}
	
}
