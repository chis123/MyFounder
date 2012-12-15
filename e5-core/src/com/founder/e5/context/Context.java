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
 * 系统环境管理类
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
	 * 向Context中添加Spring的ApplicationContext
	 * <BR>方法在com.founder.e5.load.ContextAware类被Spring加载时，
	 * 自动作为init-method被调用，从而使得Context可以访问Spring的Bean Factory
	 * <BR>这个方法由系统自动调用，请勿手工调用.
	 * 
	 * <BR>setApplicationContext,getBean(Class),getBean(String),getBeanByID(String)
	 * 四个方法在使用时，需要依赖Spring包
	 * @param context
	 */
	public static void setApplicationContext(ApplicationContext context)
	{
		appContext = context;
	}
	/**
	 * 从一个完整的类名中取出去掉包前缀的纯类名
	 * 如：
	 * 若传入的是com.founder.e5.cat.CatManager
	 * 则返回CatManager
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
	 * 按类名取Spring中配置的Bean
	 * <BR>对传入的类名参数，本方法取出其实际类名（不含包名），
	 * <BR>然后把这个类名作为Spring中的bean id，进行对象查找
	 * <BR>所以注意：可以用本方法调用的Bean在Spring配置时，id必须与类名完全相同
	 * <BR>如：
	 * <BR><bean id="CatManager" 
	 * <BR>		class="com.founder.e5.cat.Factory" 
	 * <BR>		factory-method="buildCatManager"/>
	 * <BR>调用方式为：
	 * <BR>Context.getBean("CatManager");
	 * <BR>或者
	 * <BR>Context.getBean("com.founder.e5.cat.CatManager");
	 * @param className 类名
	 * @return
	 */
	public static Object getBean(String className)
	{
		Object obj = appContext.getBean(getBareClassName(className));
		return obj;
		//return FactoryManager.find(className);
	}
	/**
	 * 按类取Spring中配置的Bean
	 * <BR>对传入的Class参数，本方法取出其实际类名（不含包名）
	 * <BR>然后把这个类名作为Spring中的bean id，进行对象查找
	 * <BR>所以注意：可以用本方法调用的Bean在Spring配置时，id必须与类名完全相同
	 * <BR>如：
	 * <BR><bean id="CatManager" 
	 * <BR>		class="com.founder.e5.cat.Factory" 
	 * <BR>		factory-method="buildCatManager"/>
	 * <BR>调用方式为：
	 * <BR>Context.getBean(CatManager.class);
	 * @param clazz 类
	 * @return
	 */
	public static Object getBean(Class clazz)
	{
		return getBean(clazz.getName());
	}
	
	/**
	 * 按ID取Spring中配置的Bean
	 * <BR>如：
	 * <BR><bean id="abcd" 
	 * <BR>		class="com.founder.e5.cat.Factory" 
	 * <BR>		factory-method="buildCatManager"/>
	 * <BR>调用方式为：
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
	 * 根据数据源名得到连接，若找不到该数据源则返回空
	 * @param strDataSource
	 * @return Connection 若找不到该数据源则返回空
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
	 * 根据JDBC连接串得到连接
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
	 * 按ID取E5数据源
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
	 * 内部方法 按数据源名取数据源
	 * @param strDataSource 数据源名
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
	 * 取对中心数据库的DBSession
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
	 * 取某E5数据源对应的DBSession
	 * <br>从缓存中获取E5数据源的信息.
	 * 参见<code>getDBSession(int, boolean)</code>
	 * @param dsID E5数据源ID
	 */
	public static DBSession getDBSession(int dsID) 
	throws E5Exception
	{
		return getDBSession(dsID, false);
	}
	/**
	 * 取某E5数据源对应的DBSession
	 * <br>
	 * 该方法先根据dsID取得登记的数据源信息,
	 * 然后根据配置文件中是否JDBC的设置，
	 * 按不同方式返回DBSession.
	 * <br>
	 * 当配置为使用jdbc时,
	 * E5数据源登记中的“数据源”被作为jdbc url,
	 * 然后根据用户名和口令,从DriverManager中取得连接
	 * <br>
	 * 当配置为使用数据源时,
	 * 按E5数据源登记中的“数据源”来进行lookup,
	 * 取得连接
	 * @param dsID
	 * @param needManager 当false时，从E5数据源缓存中取E5数据源信息
	 * true时，做数据库查询，取得E5数据源信息
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
	 * 按E5数据源获取符合相应数据库类型的DBSession
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
	 * 返回中心数据库的数据库类型
	 * 该信息在配置文件中定义
	 */
	public static String getDBType(){
		return configReader.getCentralDB().getType();
	}

	/**
	 * 取数据源管理器
	 * 等同于直接用Context.getBean(DSManager.class);
	 */
	public static DSManager getDSManager(){
		return (DSManager)getBean(DSManager.class);
	}

	/**
	 * 取数据源读取器
	 * 等同于直接用Context.getBean(DSReader.class);
	 */
	public static DSReader getDSReader(){
		return (DSReader)getBean(DSReader.class);
	}
	
	// -----------------------------------------------------------------------
	
	/**
	 * 根据类别名取得Log实例
	 * 
	 * @param category 日志类别名
	 * @return Log实例
	 */
	public static Log getLog( String category ) {
		return LogFactory.getLog( category);
	}
	
	/**
	 * 获取设置的系统服务器端的Locale信息
	 * 该信息在e5-config.xml中进行配置
	 * @return
	 */
	public static Locale getLocale()
	{
		return locale;
	}
	/**
	 * 设置系统服务器端的Locale信息
	 * <BR>这个方法在Load类被加载后自动调用，请勿手工调用
	 * @param context
	 */
	public static void setLocale(Locale _locale)
	{
		locale = _locale;
	}
}