package com.founder.e5.listpage;

/**
 * 显示字段的基础信息
 * @author LuZuowei
 *
 */
public class PreFieldDisplayData 
{
	private String code;	//字段代码，数据库中
	private String name;	//字段名称，显示用
	private String type;	//字段类型，限制型使用
	private int	   valid;	//是否有效，即在DocType中是否还存在	
	private int	   status;	//状态，已经使用，XSL或者Order或者Layer	
	
	public PreFieldDisplayData() 
	{
		super();
		code = "";
		name = "";
		type = "";
		status = 0;
		valid = 0;
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

	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
