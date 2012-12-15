package com.founder.e5.config;

/**
 * 配置文件中的多服务器信息
 * 用于缓存刷新时，要多服务器同时刷新
 * @created 2006-4-12
 * @author Gong Lijie
 * @version 1.0
 */
public class InfoServer {
	private String name;
	private String url;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
