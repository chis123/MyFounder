package com.founder.e5.listpage;

import java.util.Hashtable;

/**
 * XSL显示的特殊处理
 * @author LuZuowei
 *
 */
public class PreFieldXSLData 
{
	private String 		code;	//字段代码
	private String 		name;	//字段名称
	private int     	valid;	//是否有效
	private int			type;	//字段处理类型,1:简单,2加A,3枚举,4复杂
	private Hashtable 	hash;	//枚举时加入的内容
	private String		data;	//复杂类型的处理手段
	
	public PreFieldXSLData() 
	{
		super();
		hash = new Hashtable(10);
	}

	public String getCode() 
	{
		return code;
	}

	public void setCode(String code) 
	{
		this.code = code;
	}

	public String getData() 
	{
		return data;
	}

	public void setData(String data) 
	{
		this.data = data;
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

	protected void finalize() throws Throwable 
	{
		if (hash != null)
		{
			hash.clear();
			hash = null;
		}
		super.finalize();
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) 
	{
		this.valid = valid;
	}

	public Hashtable getHash() 
	{
		return hash;
	}

	public void setHash(Hashtable hash) 
	{
		this.hash = hash;
	}
}
