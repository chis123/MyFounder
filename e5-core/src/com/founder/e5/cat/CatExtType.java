package com.founder.e5.cat;

import java.io.Serializable;

/**
 * 分类扩展属性类型
 * 
 * @created 21-7-2005 16:27:02
 * @author Gong Lijie
 * @version 1.0
 */
public class CatExtType  implements Serializable{
	private static final long serialVersionUID = -1702753079797168770L;
	private int type;
	private String typeName;//扩展属性类型名称 
	private int order;  //排序
	private int catType;//限定在某个分类上时与WT_TYPE对应

	private int hashCode = Integer.MIN_VALUE;

	public String toString () {
		return (new StringBuffer()
				.append("[type:").append(type)
				.append(",typeName:").append(typeName)
				.append(",order:").append(order)
				.append(",catType:").append(catType)
				.append("]")
				).toString();
	}
	
	/**
	 * 取得分类类型
	 * @return 分类类型
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * 设置分类类型
	 * @param catType The catType to set.
	 */
	public void setCatType(int catType)
	{
		this.catType = catType;
	}
	/**
	 * 取得分类扩展属性类型的顺序号
	 * @return Returns the order.
	 */
	public int getOrder()
	{
		return order;
	}
	/**
	 * 设置分类扩展属性类型的顺序号
	 * @param order The order to set.
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}
	/**
	 * 取得扩展属性类型的名称
	 * @return 扩展属性类型的名称
	 */
	public String getTypeName()
	{
		return typeName;
	}
	/**
	 * 设置扩展属性类型的名称
	 * @param type 扩展属性类型的名称
	 */
	public void setTypeName(String type)
	{
		this.typeName = type;
	}
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CatExtType)) return false;
		else {
			CatExtType mObj = (CatExtType) obj;
			return (getType() == mObj.getType());
		}
	}


	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) 
		{
			if (null == getTypeName()) 
				return super.hashCode();
			else 
				this.hashCode = getTypeName().hashCode();
		}
		return this.hashCode;
	}

	/**
	 * 取得扩展属性类型ID
	 * @return 扩展属性类型ID
	 */
	public int getType()
	{
		return type;
	}
	/**
	 * 
	 * @param type The type to set.
	 */
	void setType(int type)
	{
		this.type = type;
	}
}