package com.founder.e5.context;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.LogFactory;
import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoCentralDB;
import com.founder.e5.db.DBSession;
import com.founder.e5.db.DBSessionFactory;

/**
 * ϵͳ����������
 * 
 * @created 11-7-2005 15:46:23
 * @author Gong Lijie
 * @version 1.0
 */
public class Context {

	private static ConfigReader configReader = ConfigReader.getInstance();
	private static ApplicationContext appContext;
	private static Locale locale;

	private Context(){
	}
	/**
	 * ��Context�����Spring��ApplicationContext
	 * <BR>������com.founder.e5.load.ContextAware�౻Spring����ʱ��
	 * �Զ���Ϊinit-method�����ã��Ӷ�ʹ��Context���Է���Spring��Bean Factory
	 * <BR>���������ϵͳ�Զ����ã������ֹ�����.
	 * 
	 * <BR>setApplicationContext,getBean(Class),getBean(String),getBeanByID(String)
	 * �ĸ�������ʹ��ʱ����Ҫ����Spring��
	 * @param context
	 */
	public static void setApplicationContext(ApplicationContext context)
	{
		appContext = context;
	}
	/**
	 * ��һ��������������ȡ��ȥ����ǰ׺�Ĵ�����
	 * �磺
	 * ���������com.founder.e5.cat.CatManager
	 * �򷵻�CatManager
	 * @param className
	 * @return
	 */
	private static String getBareClassName(String className)
	{
		int pos = className.lastIndexOf('.');
		if (pos < 0)
			return className;
		return className.substring(pos + 1);
	}
	/**
	 * ������ȡSpring�����õ�Bean
	 * <BR>�Դ��������������������ȡ����ʵ��������������������
	 * <BR>Ȼ������������ΪSpring�е�bean id�����ж������
	 * <BR>����ע�⣺�����ñ��������õ�Bean��Spring����ʱ��id������������ȫ��ͬ
	 * <BR>�磺
	 * <BR><bean id="CatManager" 
	 * <BR>		class="com.founder.e5.cat.Factory" 
	 * <BR>		factory-method="buildCatManager"/>
	 * <BR>���÷�ʽΪ��
	 * <BR>Context.getBean("CatManager");
	 * <BR>����
	 * <BR>Context.getBean("com.founder.e5.cat.CatManager");
	 * @param className ����
	 * @return
	 */
	public static Object getBean(String className)
	{
		Object obj = appContext.getBean(getBareClassName(className));
		return obj;
		//return FactoryManager.find(className);
	}
	/**
	 * ����ȡSpring�����õ�Bean
	 * <BR>�Դ����Class������������ȡ����ʵ������������������
	 * <BR>Ȼ������������ΪSpring�е�bean id�����ж������
	 * <BR>����ע�⣺�����ñ��������õ�Bean��Spring����ʱ��id������������ȫ��ͬ
	 * <BR>�磺
	 * <BR><bean id="CatManager" 
	 * <BR>		class="com.founder.e5.cat.Factory" 
	 * <BR>		factory-method="buildCatManager"/>
	 * <BR>���÷�ʽΪ��
	 * <BR>Context.getBean(CatManager.class);
	 * @param clazz ��
	 * @return
	 */
	public static Object getBean(Class clazz)
	{
		return getBean(clazz.getName());
	}
	
	/**
	 * ��IDȡSpring�����õ�Bean
	 * <BR>�磺
	 * <BR><bean id="abcd" 
	 * <BR>		class="com.founder.e5.cat.Factory" 
	 * <BR>		factory-method="buildCatManager"/>
	 * <BR>���÷�ʽΪ��
	 * <BR>Context.getBeanByID("abcd");
	 * @param id
	 * @return
	 */
	public static Object getBeanByID(String id)
	{
		return appContext.getBean(id);
		//return FactoryManager.findByID(id);
	}	

	/**
	 * ��������Դ���õ����ӣ����Ҳ���������Դ�򷵻ؿ�
	 * @param strDataSource
	 * @return Connection ���Ҳ���������Դ�򷵻ؿ�
	 * @throws E5Exception
	 */
	private static Connection getConnection(String strDataSource) 
	throws E5Exception 
	{
		try
		{
			DataSource ds = getDataSource(strDataSource);
			if (ds != null)
				return ds.getConnection();
			else 
				return null;
		}
		catch (SQLException e)
		{
			throw new E5Exception("[Context.getConnection]", e);
		}
	}
	
	/**
	 * ����JDBC���Ӵ��õ�����
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 * @throws E5Exception
	 * @throws ClassNotFoundException
	 */
	private static Connection getConnection(String url, String user, String password) 
	throws E5Exception 
	{
		try
		{
			return java.sql.DriverManager.getConnection(url, user, password);
		}
		catch (SQLException e)
		{
			throw new E5Exception("[Context.getConnection]", e);
		}
	}

	/**
	 * ��IDȡE5����Դ
	 * @param dsID
	 * @return
	 * @throws E5Exception
	 */
	private static E5DataSource getE5DataSource(int dsID, boolean needManager) 
	throws E5Exception 
	{
		DSReader dsReader = null;
		if (needManager) dsReader = getDSManager();
		else dsReader = getDSReader();
		
		return dsReader.get(dsID);
	}

	/**
	 * �ڲ����� ������Դ��ȡ����Դ
	 * @param strDataSource ����Դ��
	 * @return
	 * @throws E5Exception
	 */
	private static DataSource getDataSource(String strDataSource) 
	throws E5Exception 
	{
		try
		{
			javax.naming.Context ctx = new  javax.naming.InitialContext();
			return (javax.sql.DataSource)ctx.lookup(strDataSource);
		}
		catch (NamingException e)
		{
			throw new E5Exception("[Context.getConnection]", e);
		}
	}

	/**
	 * ȡ���������ݿ��DBSession
	 */
	public static DBSession getDBSession() 
	throws E5Exception 
	{
		InfoCentralDB centralDB = configReader.getCentralDB();
		if (centralDB.useJDBC())
			return getDBSession(centralDB.getType(), centralDB.getUrl(), centralDB.getUser(), centralDB.getPassword());
		else
			return getDBSession(centralDB.getType(), centralDB.getUrl());
	}

	/**
	 * ȡĳE5����Դ��Ӧ��DBSession
	 * <br>�ӻ����л�ȡE5����Դ����Ϣ.
	 * �μ�<code>getDBSession(int, boolean)</code>
	 * @param dsID E5����ԴID
	 */
	public static DBSession getDBSession(int dsID) 
	throws E5Exception
	{
		return getDBSession(dsID, false);
	}
	/**
	 * ȡĳE5����Դ��Ӧ��DBSession
	 * <br>
	 * �÷����ȸ���dsIDȡ�õǼǵ�����Դ��Ϣ,
	 * Ȼ����������ļ����Ƿ�JDBC�����ã�
	 * ����ͬ��ʽ����DBSession.
	 * <br>
	 * ������Ϊʹ��jdbcʱ,
	 * E5����Դ�Ǽ��еġ�����Դ������Ϊjdbc url,
	 * Ȼ������û����Ϳ���,��DriverManager��ȡ������
	 * <br>
	 * ������Ϊʹ������Դʱ,
	 * ��E5����Դ�Ǽ��еġ�����Դ��������lookup,
	 * ȡ������
	 * @param dsID
	 * @param needManager ��falseʱ����E5����Դ������ȡE5����Դ��Ϣ
	 * trueʱ�������ݿ��ѯ��ȡ��E5����Դ��Ϣ
	 * @return
	 * @throws E5Exception
	 */
	public static DBSession getDBSession(int dsID, boolean needManager) 
	throws E5Exception
	{
		E5DataSource e5ds = getE5DataSource(dsID, needManager);
		return getDBSession(e5ds);
	}
	
	/**
	 * ��E5����Դ��ȡ������Ӧ���ݿ����͵�DBSession
	 * @param e5ds
	 * @return
	 * @throws E5Exception
	 */
	public static DBSession getDBSession(E5DataSource e5ds) 
	throws E5Exception
	{
		InfoCentralDB centralDB = configReader.getCentralDB();
		if (centralDB.useJDBC())
			return getDBSession(e5ds.getDbType(), e5ds.getDataSource(), e5ds.getUser(), e5ds.getPassword());
		else
			return getDBSession(e5ds.getDbType(), e5ds.getDataSource());
	}
	

	private static DBSession getDBSession(String strDBType, String strDataSource) 
	throws E5Exception 
	{
		try
		{
			DBSession dbSession = DBSessionFactory.getDBSession(strDBType);
			dbSession.setConnection(getConnection(strDataSource));
			return dbSession;
		}
		catch (Exception e)
		{
			throw new E5Exception(e);
		}
	}
	
	private static DBSession getDBSession(String strDBType, String url, String user, String password) 
	throws E5Exception 
	{
		try
		{
			DBSession dbSession = DBSessionFactory.getDBSession(strDBType);
			dbSession.setConnection(getConnection(url, user, password));
			return dbSession;
		}
		catch (Exception e)
		{
			throw new E5Exception(e);
		}
	}
	/**
	 * �����������ݿ�����ݿ�����
	 * ����Ϣ�������ļ��ж���
	 */
	public static String getDBType(){
		return configReader.getCentralDB().getType();
	}

	/**
	 * ȡ����Դ������
	 * ��ͬ��ֱ����Context.getBean(DSManager.class);
	 */
	public static DSManager getDSManager(){
		return (DSManager)getBean(DSManager.class);
	}

	/**
	 * ȡ����Դ��ȡ��
	 * ��ͬ��ֱ����Context.getBean(DSReader.class);
	 */
	public static DSReader getDSReader(){
		return (DSReader)getBean(DSReader.class);
	}
	
	// -----------------------------------------------------------------------
	
	/**
	 * ���������ȡ��Logʵ��
	 * 
	 * @param category ��־�����
	 * @return Logʵ��
	 */
	public static Log getLog( String category ) {
		return LogFactory.getLog( category);
	}
	
	/**
	 * ��ȡ���õ�ϵͳ�������˵�Locale��Ϣ
	 * ����Ϣ��e5-config.xml�н�������
	 * @return
	 */
	public static Locale getLocale()
	{
		return locale;
	}
	/**
	 * ����ϵͳ�������˵�Locale��Ϣ
	 * <BR>���������Load�౻���غ��Զ����ã������ֹ�����
	 * @param context
	 */
	public static void setLocale(Locale _locale)
	{
		locale = _locale;
	}
}