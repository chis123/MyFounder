package com.founder.e5.listpage;

/**
 * ��ʾ�ֶεĻ�����Ϣ
 * @author LuZuowei
 *
 */
public class PreFieldDisplayData 
{
	private String code;	//�ֶδ��룬���ݿ���
	private String name;	//�ֶ����ƣ���ʾ��
	private String type;	//�ֶ����ͣ�������ʹ��
	private int	   valid;	//�Ƿ���Ч������DocType���Ƿ񻹴���	
	private int	   status;	//״̬���Ѿ�ʹ�ã�XSL����Order����Layer	
	
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
