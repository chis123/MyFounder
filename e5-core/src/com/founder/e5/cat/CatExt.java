package com.founder.e5.cat;

import java.io.Serializable;

/**
 * ������չ����
 * һ�����ྭ���ж������������չ���Թ��������
 * @created 21-7-2005 16:19:52
 * @author Gong Lijie
 * @version 1.0
 */
public class CatExt implements Serializable {
	private static final long serialVersionUID = -2931060917854048816L;

	private int hashCode = Integer.MIN_VALUE;
	private int catType;
	private int catID;
	private int extType;
	private String extName;
	private int parentID;

	/**
	 * �������ֻ����ʾ�ã��������޸�
	 * �����޸��ɺ�̨���������
	 */
	private String cascadeName;
	public String toString(){
		return (new StringBuffer()
				.append("[extType:").append(extType)
				.append(",catID:").append(catID)
				.append(",catType:").append(catType)
				.append(",extName:").append(extName)
				.append(",parentID:").append(parentID)
				.append("]")
				).toString();
	}

	public CatExt()
	{
		super();

	}
	/**
	 * @param catType
	 * @param catID
	 * @param extType
	 */
	public CatExt(int extType, int catType, int catID)
	{
		super();
		this.catType = catType;
		this.catID = catID;
		this.extType = extType;
	}
	
	/**
	 * @param catType
	 * @param catID
	 * @param extType
	 * @param extName
	 * @param parentID
	 * @param cascadeName
	 */
	public CatExt(int extType, int catType, int catID, String extName,
			int parentID, String cascadeName)
	{
		super();
		this.catType = catType;
		this.catID = catID;
		this.extType = extType;
		this.extName = extName;
		this.parentID = parentID;
		this.cascadeName = cascadeName;
	}
	/**
	 * @param cascadeName The cascadeName to set.
	 */
	public void setCascadeName(String cascadeName)
	{
		this.cascadeName = cascadeName;
	}
	/**
	 * ȡ����չ���������ķ���ID
	 * @return ����ID
	 */
	public int getCatID()
	{
		return catID;
	}
	/**
	 * ������չ���������ķ���ID
	 * @param catID ����ID
	 */
	public void setCatID(int catID)
	{
		this.catID = catID;
	}
	/**
	 * ȡ����չ���������ķ�������
	 * @return ��������
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * ������չ���������ķ�������
	 * @param catType ��������
	 */
	public void setCatType(int catType)
	{
		this.catType = catType;
	}
	/**
	 * ȡ�÷�����չ����ȡֵ
	 * @return ��չ����ȡֵ
	 */
	public String getExtName()
	{
		return extName;
	}
	/**
	 * ���÷�����չ����ȡֵ
	 * @param extName ȡֵ
	 */
	public void setExtName(String extName)
	{
		this.extName = extName;
	}
	/**
	 * ȡ����չ��������
	 * @return ��չ��������
	 */
	public int getExtType()
	{
		return extType;
	}
	/**
	 * ������չ��������
	 * @param extType ��չ��������
	 */
	public void setExtType(int extType)
	{
		this.extType = extType;
	}
	/**
	 * @return Returns the hashCode.
	 */
	public int getHashCode()
	{
		return hashCode;
	}
	/**
	 * @param hashCode The hashCode to set.
	 */
	public void setHashCode(int hashCode)
	{
		this.hashCode = hashCode;
	}
	/**
	 * ȡ�ø�����ID
	 * @return ������ID
	 */
	public int getParentID()
	{
		return parentID;
	}
	/**
	 * ���ø�����ID
	 * @param parentID ������ID
	 */
	public void setParentID(int parentID)
	{
		this.parentID = parentID;
	}
	/**
	 * ȡ����չ���Լ�������
	 * @return ��չ���Լ�������
	 */
	public String getCascadeName()
	{
		return cascadeName;
	}
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CatExt)) return false;

		CatExt mObj = (CatExt) obj;
		if ((getCatID() != mObj.getCatID()) 
			||(getExtType() != mObj.getExtType())) 
				return false;
		else
			return true;
	}


	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			StringBuffer sb = new StringBuffer();
			sb.append(this.getCatID());
			sb.append(":");
			sb.append(this.getExtType());
			sb.append(":");

			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}
}