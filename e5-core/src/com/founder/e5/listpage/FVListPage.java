package com.founder.e5.listpage;

/**
 * 文件夹列表方式对象
 * @created 2009-8-26
 * @author Gong Lijie
 * @version 1.0
 */
public class FVListPage {
	int fvID;	//文件夹ID
	String pages;	//列表ID的字符串
	ListPage[] pageList;	//列表对象数组
	
	public FVListPage(int fvID, String pages, ListPage[] pageList) {
		super();
		
		this.fvID = fvID;
		this.pages = pages;
		this.pageList = pageList;
	}
	
	public int getFvID() {
		return fvID;
	}
	public void setFvID(int fvID) {
		this.fvID = fvID;
	}
	public ListPage[] getPageList() {
		return pageList;
	}
	public void setPageList(ListPage[] pageList) {
		this.pageList = pageList;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	
}
