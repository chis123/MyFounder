package com.founder.e5.listpage;

/**
 * 排序字段的处理
 * @author LuZuowei
 *
 */
public class PreSortFieldData 
{
	private String code;	//字段代码
	private String name;	//字段名称
	private int	   valid;	//是否有效
	private int	   type;	//升序和降序	
	
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
