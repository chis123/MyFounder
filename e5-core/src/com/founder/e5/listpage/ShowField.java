package com.founder.e5.listpage;

/**
 * ��ListXML�����Fields�е��������Ӧ
 * @author LuZuowei
 *
 */
public class ShowField 
{
	private String name;	//���ƣ���ǰ���ֶ�����
	private int type;		//��������
	private String url;		//����URL��ʽ
	private String clazz;	//class����
	private String method;	//��������
	
	public ShowField() 
	{
		super();
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
