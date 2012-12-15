package com.founder.e5.config;

/**
 * 2006-4-11
 * 关于中心服务器的配置有变动，不需要单独配置这里的信息了，系统自动取Hibernate中的配置。
 * e5-config.xml中，只要保留configFile（必填）和isJDBC（可选）即可。
 * 
 * 与配置文件对应的中心服务器信息
 * 上层应用可以指定系统是否使用JNDI——这个配置对中心数据源和非中心数据源都有效
 * 当使用JNDI时，只需要配置前3个属性即可，其中url配置为jndi数据源的名字，
 * 如jdbc/e5new<BR/>
 * 若使用JDBC，需要另外配置以下3项
 * isJDBC="1" or isJDBC="true" 
 * user="[数据库连接用户]"
 * password="[数据库连接用户的密码]"
 * 并且把url改为类似"jdbc:oracle:thin:@10.11.12.13:1521:ORCL"的形式
 * Created on 2005-7-13
 * @author Gong Lijie
 */
public class InfoCentralDB
{
	private String type;
	private String url;
	private String configFile;
	private String isJDBC;
	private String user;
	private String password;
	
	public String toString(){
		return (new StringBuffer()
				.append("[type:").append(type)
				.append(",url:").append(url)
				.append(",configFile:").append(configFile)
				.append(",isJDBC:").append(isJDBC)
				.append(",user:").append(user)
				.append(",password:").append(password)
				.append("]")
				).toString();
	}
	public boolean useJDBC()
	{
		if (isJDBC == null)
			return false;
		if (isJDBC.equals("1"))
			return true;
		if (isJDBC.equals("true"))
			return true;
		return false;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return Returns the configFile.
	 */
	public String getConfigFile()
	{
		return configFile;
	}
	/**
	 * @param configFile The configFile to set.
	 */
	public void setConfigFile(String configFile)
	{
		this.configFile = configFile;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	/**
	 * @return Returns the isJDBC.
	 */
	public String getIsJDBC() {
		return isJDBC;
	}
	/**
	 * @param isJDBC The isJDBC to set.
	 */
	public void setIsJDBC(String isJDBC) {
		this.isJDBC = isJDBC;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
