package com.founder.e5.listpage;

/**
 * 与ListXML定义的Fields中的内容相对应
 * @author LuZuowei
 *
 */
public class ShowField 
{
	private String name;	//名称，当前是字段名称
	private int type;		//处理类型
	private String url;		//处理URL方式
	private String clazz;	//class名称
	private String method;	//方法名称
	
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
