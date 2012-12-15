package com.founder.e5.flow;

/**
 * ����ģ����
 * @created 04-8-2005 14:16:59
 * @author Gong Lijie
 * @version 1.0
 */
public class Operation {
	/**����ģ��Ĵ��ڷ�ʽ�������Ի���ʽ��ֻ��IE�����ã���window.showModalDialog�򿪡�*/
	public static final int WINDOW_DIALOG = 0;
	/**����ģ��Ĵ��ڷ�ʽ�������������ڷ�ʽ����window.open�򿪡�*/
	public static final int WINDOW_OPEN = 1;
	
	private int ID;
	private String name;
	private int docTypeID;
	private String codeURL;
	private String description;
	private boolean needLock;
	private boolean needRefresh;
	private boolean needLog;
	private boolean needPrompt;
	private boolean resizable;
	
	/**����ģ��Ĵ��ڷ�ʽ*/
	private int callMode;
	private int showType;
	private int dealCount;
	private int height;
	private int width;
	
	/**����ģ��Ĵ��ڷ�ʽ
	 * @return Returns the callMode.
	 */
	public int getCallMode()
	{
		return callMode;
	}

	/**
	 * @param callMode
	 */
	public void setCallMode(int callMode)
	{
		this.callMode = callMode;
	}
	/**����URL
	 * @return Returns the codeURL.
	 */
	public String getCodeURL()
	{
		return codeURL;
	}
	/**
	 * @param codeURL The codeURL to set.
	 */
	public void setCodeURL(String codeURL)
	{
		this.codeURL = codeURL;
	}
	/**�����Ĵ����ĵ�����
	 * <br/>0��������ѡ���ĵ�
	 * <br/>1��ֻ����һ���ĵ�
	 * <br/>2���ɴ������ĵ�
	 * @return Returns the dealCount.
	 */
	public int getDealCount()
	{
		return dealCount;
	}
	/**
	 * @param dealCount The dealCount to set.
	 */
	public void setDealCount(int dealCount)
	{
		this.dealCount = dealCount;
	}
	/**��������
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**�����������ĵ�����ID
	 * @return Returns the docTypeID.
	 */
	public int getDocTypeID()
	{
		return docTypeID;
	}
	/**
	 * @param docTypeID The docTypeID to set.
	 */
	public void setDocTypeID(int docTypeID)
	{
		this.docTypeID = docTypeID;
	}
	/**�������ڸ�
	 * @return Returns the height.
	 */
	public int getHeight()
	{
		return height;
	}
	/**
	 * @param height The height to set.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	/**����ID
	 * @return Returns the iD.
	 */
	public int getID()
	{
		return ID;
	}
	/**
	 * @param id The iD to set.
	 */
	void setID(int id)
	{
		ID = id;
	}
	/**��������
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**�����Ƿ���Ҫ��ѡ�е��ĵ�����
	 * @return Returns the needLock.
	 */
	public boolean isNeedLock()
	{
		return needLock;
	}
	/**
	 * @param needLock The needLock to set.
	 */
	public void setNeedLock(boolean needLock)
	{
		this.needLock = needLock;
	}
	/**�����Ƿ���Ҫ�����̼�¼
	 * @return Returns the needLog.
	 */
	public boolean isNeedLog()
	{
		return needLog;
	}
	/**
	 * @param needLog The needLog to set.
	 */
	public void setNeedLog(boolean needLog)
	{
		this.needLog = needLog;
	}
	/**����ǰ�Ƿ���Ҫ��ʾ��ȷ��Ҫ������������
	 * @return Returns the needPrompt.
	 */
	public boolean isNeedPrompt()
	{
		return needPrompt;
	}
	/**
	 * @param needPrompt The needPrompt to set.
	 */
	public void setNeedPrompt(boolean needPrompt)
	{
		this.needPrompt = needPrompt;
	}
	/**�������Ƿ���Ҫˢ���ĵ��б�
	 * @return Returns the needRefresh.
	 */
	public boolean isNeedRefresh()
	{
		return needRefresh;
	}
	/**
	 * @param needRefresh The needRefresh to set.
	 */
	public void setNeedRefresh(boolean needRefresh)
	{
		this.needRefresh = needRefresh;
	}
	/**���������Ƿ�ɵ�����С
	 * @return Returns the resizable.
	 */
	public boolean isResizable()
	{
		return resizable;
	}
	/**
	 * @param resizable The resizable to set.
	 */
	public void setResizable(boolean resizable)
	{
		this.resizable = resizable;
	}
	/**������ť����ʾ��ʽ����λ��ʾ��
	 * <br/>���λ��1��0��ʾ�Ƿ����ʾ�ڹ�������
	 * <br/>��λ��1��0��ʾ�Ƿ����ʾ���Ҽ��˵���
	 * @return Returns the showType.
	 */
	public int getShowType()
	{
		return showType;
	}
	/**
	 * @param showType The showType to set.
	 */
	public void setShowType(int showType)
	{
		this.showType = showType;
	}
	/**�������ڵĿ�
	 * @return Returns the width.
	 */
	public int getWidth()
	{
		return width;
	}
	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
}