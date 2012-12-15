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

import java.util.ArrayList;
import java.util.List;

public class Article {

	private int articleId;
	private String introTitle;
	private String subTitle;
	private String articleAbstract;
	private String attrDesc;
	private int wordCount;
	private int importance;
	private String keyword;
	private int picCount;
	private String nsdate;
	private String source;
	private String author;
	private String title;
	private String nodeNamePath;
	private String url;
	private String content;
	private String expirationTime;
	private List attachments = new ArrayList();
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getIntroTitle() {
		return introTitle;
	}
	public void setIntroTitle(String introTitle) {
		this.introTitle = introTitle;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getArticleAbstract() {
		return articleAbstract;
	}
	public void setArticleAbstract(String articleAbstract) {
		this.articleAbstract = articleAbstract;
	}
	public String getAttrDesc() {
		return attrDesc;
	}
	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public int getImportance() {
		return importance;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getPicCount() {
		return picCount;
	}
	public void setPicCount(int picCount) {
		this.picCount = picCount;
	}
	public String getNsdate() {
		return nsdate;
	}
	public void setNsdate(String nsdate) {
		this.nsdate = nsdate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNodeNamePath() {
		return nodeNamePath;
	}
	public void setNodeNamePath(String nodeNamePath) {
		this.nodeNamePath = nodeNamePath;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	public List getAttachments() {
		return attachments;
	}
	public void setAttachments(List attachments) {
		this.attachments = attachments;
	}
	public void addAttachment(Attachment attachment) {
		this.attachments.add(attachment);
	}
	
}
