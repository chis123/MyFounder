package com.founder.e5.listpage;

/**
 * �����ֶεĴ���
 * @author LuZuowei
 *
 */
public class PreSortFieldData 
{
	private String code;	//�ֶδ���
	private String name;	//�ֶ�����
	private int	   valid;	//�Ƿ���Ч
	private int	   type;	//����ͽ���	
	
	public PreSortFieldData() 
	{
		super();
		valid = 1;
		type = 1;
	}

	public String getCode() 
	{
		return code;
	}

	public void setCode(String code) 
	{
		this.code = code;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getType() 
	{
		return type;
	}

	public void setType(int type) 
	{
		this.type = type;
	}

	public int getValid() 
	{
		return valid;
	}

	public void setValid(int valid) 
	{
		this.valid = valid;
	}
}
