package com.founder.e5.config;

/**
 * 2006-4-11
 * �������ķ������������б䶯������Ҫ���������������Ϣ�ˣ�ϵͳ�Զ�ȡHibernate�е����á�
 * e5-config.xml�У�ֻҪ����configFile�������isJDBC����ѡ�����ɡ�
 * 
 * �������ļ���Ӧ�����ķ�������Ϣ
 * �ϲ�Ӧ�ÿ���ָ��ϵͳ�Ƿ�ʹ��JNDI����������ö���������Դ�ͷ���������Դ����Ч
 * ��ʹ��JNDIʱ��ֻ��Ҫ����ǰ3�����Լ��ɣ�����url����Ϊjndi����Դ�����֣�
 * ��jdbc/e5new<BR/>
 * ��ʹ��JDBC����Ҫ������������3��
 * isJDBC="1" or isJDBC="true" 
 * user="[���ݿ������û�]"
 * password="[���ݿ������û�������]"
 * ���Ұ�url��Ϊ����"jdbc:oracle:thin:@10.11.12.13:1521:ORCL"����ʽ
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
