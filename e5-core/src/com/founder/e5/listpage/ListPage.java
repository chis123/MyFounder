package com.founder.e5.listpage;

/**
 * ��Ӧ����б�ʽ�Ķ���
 * @author LuZuowei
 *
 */
public class ListPage 
{
	private int 	docTypeID;				//�б��Ӧ���ĵ�����ID
	private int 	listID;					//�б��ID
	private String 	listName;			    //�б������
	private String	listBuilderName;		//���͸�PageList�Ľ�����������
	private String	ListXML;				//��ȡ�б���Ϣ�����XML����
	private String  pathXSL;				//�����ϸ�ʽʱ��XSL·��
	private String  pathJS;					//����ģ�巽ʽ��JS·��
	private String  pathCSS;				//����ģ�巽ʽʱ��CSS·��
	private String  templateSlice;			//����ģ�巽ʽʱ��ģ��Ƭ�Ķ��巽ʽ
	
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
