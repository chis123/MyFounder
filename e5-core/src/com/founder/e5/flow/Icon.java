package com.founder.e5.flow;

/**
 * ͼ����
 * @created 04-8-2005 14:17:07
 * @author Gong Lijie
 * @version 1.0
 */
public class Icon {
	/**ͼ���ϵͳĿ¼��һ�������á�*/
	public static final String iconPath = "Icons/";
	
	private int ID;
	private String fileName;
	private String description;
	private String format;
	private String size;

	/**
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
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName()
	{
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	/**
	 * @return Returns the format.
	 */
	public String getFormat()
	{
		return format;
	}
	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}
	/**
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
	/**
	 * @return Returns the size.
	 */
	public String getSize()
	{
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(String size)
	{
		this.size = size;
	}
	/**
	 * ͼ���URL��ַ����ͼ��·��+ͼ���ļ���������
	 * @return Returns the url.
	 */
	public String getUrl()
	{
		return Icon.iconPath + getFileName();
	}
}