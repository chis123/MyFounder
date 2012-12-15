package com.founder.e5.cat;

import java.io.Serializable;

/**
 * 分类类型
 * @created 21-7-2005 16:27:49
 * @author Gong Lijie
 * @version 1.0
 */
public class CatType implements Serializable {
	private static final long serialVersionUID = 2305656382709350346L;
	private int catType;
	private String name;
	private int order;
	private String tableName;
	private int property;
	private int property_sub;

	private int hashCode = Integer.MIN_VALUE;

	public String toString () {
		return (new StringBuffer()
				.append("[name:").append(name)
				.append(",order:").append(order)
				.append(",tableName:").append(tableName)
				.append(",property:").append(property)
				.append(",property_sub:").append(property_sub)
				.append(",catType:").append(catType)
				.append("]")
				).toString();
	}
	
	/**
	 * @return Returns the catType.
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * @param catType The catType to set.
	 */
	void setCatType(int catType)
	{
		this.catType = catType;
	}
	/**
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
	/**
	 * @return Returns the order.
	 */
	public int getOrder()
	{
		return order;
	}
	/**
	 * @param order The order to set.
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}
	/**
	 * @return Returns the property.
	 */
	public int getProperty()
	{
		return property;
	}
	/**
	 * @param property The property to set.
	 */
	public void setProperty(int property)
	{
		this.property = property;
	}
	/**
	 * @return Returns the property_sub.
	 */
	public int getProperty_sub()
	{
		return property_sub;
	}
	/**
	 * @param property_sub The property_sub to set.
	 */
	public void setProperty_sub(int property_sub)
	{
		this.property_sub = property_sub;
	}
	/**
	 * @return Returns the tableName.
	 */
	public String getTableName()
	{
		return tableName;
	}
	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(String tableName)
	{
		if(tableName!=null) tableName=tableName.trim();
		this.tableName = tableName;
	}
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CatType)) return false;
		else {
			CatType mObj = (CatType) obj;
			return (getCatType() == mObj.getCatType());
		}
	}


	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			String hashStr = getClass().getName() + ":" + 
				this.getCatType();
			hashCode = hashStr.hashCode();
		}
		return hashCode;
	}
	
	/**
	 * 是否是系统分类
	 * @return true/false
	 * @author wanghc
	 */
	public boolean isSystemType()
	{
		if(tableName==null || "".equals(tableName))
			return false;
		else
			return true;
	}
}