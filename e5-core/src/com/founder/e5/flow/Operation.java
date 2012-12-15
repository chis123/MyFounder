package com.founder.e5.flow;

/**
 * 操作模块类
 * @created 04-8-2005 14:16:59
 * @author Gong Lijie
 * @version 1.0
 */
public class Operation {
	/**操作模块的窗口方式常量：对话框方式。只对IE起作用，以window.showModalDialog打开。*/
	public static final int WINDOW_DIALOG = 0;
	/**操作模块的窗口方式常量：独立窗口方式。以window.open打开。*/
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
	
	/**操作模块的窗口方式*/
	private int callMode;
	private int showType;
	private int dealCount;
	private int height;
	private int width;
	
	/**操作模块的窗口方式
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
	/**操作URL
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
	/**操作的处理文档个数
	 * <br/>0：不处理选中文档
	 * <br/>1：只处理一个文档
	 * <br/>2：可处理多个文档
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
	/**操作描述
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
	/**操作所属的文档类型ID
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
	/**操作窗口高
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
	/**操作ID
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
	/**操作名称
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
	/**操作是否需要对选中的文档加锁
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
	/**操作是否需要做流程记录
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
	/**操作前是否需要提示：确定要做××操作吗？
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
	/**操作后是否需要刷新文档列表
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
	/**操作窗口是否可调整大小
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
	/**操作按钮的显示方式，按位表示：
	 * <br/>最低位用1和0表示是否可显示在工具栏上
	 * <br/>次位用1和0表示是否可显示在右键菜单上
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
	/**操作窗口的宽
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