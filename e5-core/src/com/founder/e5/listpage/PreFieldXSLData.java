package com.founder.e5.listpage;

import java.util.Hashtable;

/**
 * XSL��ʾ�����⴦��
 * @author LuZuowei
 *
 */
public class PreFieldXSLData 
{
	private String 		code;	//�ֶδ���
	private String 		name;	//�ֶ�����
	private int     	valid;	//�Ƿ���Ч
	private int			type;	//�ֶδ�������,1:��,2��A,3ö��,4����
	private Hashtable 	hash;	//ö��ʱ���������
	private String		data;	//�������͵Ĵ����ֶ�
	
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
