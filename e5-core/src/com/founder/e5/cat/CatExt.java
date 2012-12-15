package com.founder.e5.cat;

import java.io.Serializable;

/**
 * 分类扩展属性
 * 一个分类经常有多个别名，用扩展属性管理其别名
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
	 * 这个属性只做显示用，不对其修改
	 * 它的修改由后台触发器完成
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
	 * 取得扩展属性所属的分类ID
	 * @return 分类ID
	 */
	public int getCatID()
	{
		return catID;
	}
	/**
	 * 设置扩展属性所属的分类ID
	 * @param catID 分类ID
	 */
	public void setCatID(int catID)
	{
		this.catID = catID;
	}
	/**
	 * 取得扩展属性所属的分类类型
	 * @return 分类类型
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * 设置扩展属性所属的分类类型
	 * @param catType 分类类型
	 */
	public void setCatType(int catType)
	{
		this.catType = catType;
	}
	/**
	 * 取得分类扩展属性取值
	 * @return 扩展属性取值
	 */
	public String getExtName()
	{
		return extName;
	}
	/**
	 * 设置分类扩展属性取值
	 * @param extName 取值
	 */
	public void setExtName(String extName)
	{
		this.extName = extName;
	}
	/**
	 * 取得扩展属性类型
	 * @return 扩展属性类型
	 */
	public int getExtType()
	{
		return extType;
	}
	/**
	 * 设置扩展属性类型
	 * @param extType 扩展属性类型
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
	 * 取得父分类ID
	 * @return 父分类ID
	 */
	public int getParentID()
	{
		return parentID;
	}
	/**
	 * 设置父分类ID
	 * @param parentID 父分类ID
	 */
	public void setParentID(int parentID)
	{
		this.parentID = parentID;
	}
	/**
	 * 取得扩展属性级联名称
	 * @return 扩展属性级联名称
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