package com.founder.e5.listpage;

/**
 * 对应稿件列表方式的定义
 * @author LuZuowei
 *
 */
public class ListPage 
{
	private int 	docTypeID;				//列表对应的文档类型ID
	private int 	listID;					//列表的ID
	private String 	listName;			    //列表的名称
	private String	listBuilderName;		//解释该PageList的解析器的名称
	private String	ListXML;				//获取列表信息定义的XML定义
	private String  pathXSL;				//兼容老格式时的XSL路径
	private String  pathJS;					//复杂模板方式的JS路径
	private String  pathCSS;				//复杂模板方式时的CSS路径
	private String  templateSlice;			//复杂模板方式时的模板片的定义方式
	
	public ListPage() 
	{
		super();
	}

	public int getDocTypeID() {
		return docTypeID;
	}

	public void setDocTypeID(int docTypeID) {
		this.docTypeID = docTypeID;
	}

	public String getListBuilderName() {
		return listBuilderName;
	}

	public void setListBuilderName(String listBuilderName) {
		this.listBuilderName = listBuilderName;
	}

	public int getListID() {
		return listID;
	}

	public void setListID(int listID) {
		this.listID = listID;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getListXML() {
		return ListXML;
	}

	public void setListXML(String listXML) {
		ListXML = listXML;
	}

	public String getPathCSS() {
		return pathCSS;
	}

	public void setPathCSS(String pathCSS) {
		this.pathCSS = pathCSS;
	}

	public String getPathJS() {
		return pathJS;
	}

	public void setPathJS(String pathJS) {
		this.pathJS = pathJS;
	}

	public String getPathXSL() {
		return pathXSL;
	}

	public void setPathXSL(String pathXSL) {
		this.pathXSL = pathXSL;
	}

	public String getTemplateSlice() 
	{
		return templateSlice;
	}

	public void setTemplateSlice(String templateSlice) 
	{
		this.templateSlice = templateSlice;
	}
	
	public boolean isOld()
	{
		if (pathXSL != null && !"".equals(pathXSL))
			return true;
		else
			return false;
	}
}
