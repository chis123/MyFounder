package com.founder.e5.config;

/**
 * 与配置文件对应的平台权限设置页面
 * Created on 2006-2-10
 * @author Gong Lijie
 */
public class InfoPermissionPage {

	private String id;
	private String name;
	private String visible;
	private String url;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	public String toString(){
		return (new StringBuffer()
				.append("[id:").append(id)
				.append(",name:").append(name)
				.append(",visible:").append(visible)
				.append(",url:").append(url)
				.append("]")
				).toString();
	}
}
