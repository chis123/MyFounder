package com.founder.e5.cat;

import java.io.Serializable;

/**
 * ������չ��������
 * 
 * @created 21-7-2005 16:27:02
 * @author Gong Lijie
 * @version 1.0
 */
public class CatExtType  implements Serializable{
	private static final long serialVersionUID = -1702753079797168770L;
	private int type;
	private String typeName;//��չ������������ 
	private int order;  //����
	private int catType;//�޶���ĳ��������ʱ��WT_TYPE��Ӧ

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
	 * ȡ�÷�������
	 * @return ��������
	 */
	public int getCatType()
	{
		return catType;
	}
	/**
	 * ���÷�������
	 * @param catType The catType to set.
	 */
	public void setCatType(int catType)
	{
		this.catType = catType;
	}
	/**
	 * ȡ�÷�����չ�������͵�˳���
	 * @return Returns the order.
	 */
	public int getOrder()
	{
		return order;
	}
	/**
	 * ���÷�����չ�������͵�˳���
	 * @param order The order to set.
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}
	/**
	 * ȡ����չ�������͵�����
	 * @return ��չ�������͵�����
	 */
	public String getTypeName()
	{
		return typeName;
	}
	/**
	 * ������չ�������͵�����
	 * @param type ��չ�������͵�����
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
	 * ȡ����չ��������ID
	 * @return ��չ��������ID
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