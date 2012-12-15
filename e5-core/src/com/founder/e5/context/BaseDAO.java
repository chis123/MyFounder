package com.founder.e5.context;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoCentralDB;
import com.founder.e5.db.DBType;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 对Hibernate操作的管理类
 * 继承自Hibernate Sychronizer实现的抽象类
 * @created on 2005-7-12
 * @author Gong Lijie
 * @version 1.0
 */
public class BaseDAO extends _BaseDAO
{
	private static String configFile = null;
	
	/**
	 * 用配置文件进行Hibernate的SessionFactory初始化
	 * 该方法只在初始化时做一次
	 * 系统加载时自动调用
	 */
	public static synchronized Configuration init()
	throws HibernateException 
	{
		//从e5-config配置文件中读取的中心数据库信息：Hiberante配置文件名
        InfoCentralDB centralDB = ConfigReader.getInstance().getCentralDB();
		if (centralDB == null) throw new RuntimeException("No CentralDB info!");
        configFile = centralDB.getConfigFile();
        
		//若用String方式加载Hibernate有问题
		//则假设不是WEB方式，或配置的是绝对路径，改用File类型加载一次
        Configuration cfg = null;
		try {
			cfg = init(configFile);
		} catch (HibernateException e) {
			System.out.println("Cannot get hibernate config by string:" + configFile);
			System.out.println("Try to use file!");
			File fileConfig = new File(configFile);
			cfg = init(fileConfig);
		}
        
        fillCentralDB(cfg, centralDB);
        return cfg;
	}
	
	/**
	 * 清理Hibernate的SessionFactory，释放掉资源。
	 * @throws HibernateException
	 */
	public static synchronized void reset()
	throws HibernateException 
	{
		if (sessionFactoryMap == null) return;
		
		Collection sfList = sessionFactoryMap.values();
		if (sfList == null || (sfList.size() == 0)) return;
		
		Iterator it = sfList.iterator();
		while (it.hasNext())
		{
			SessionFactory sf = (SessionFactory) it.next();
			sf.close();
		}
		sessionFactoryMap.clear();
	}
    //用返回的Hiberante配置对象，再把中心数据库的url/user/pwd等信息补足
	protected static void fillCentralDB(Configuration cfg, InfoCentralDB centralDB)
	{
        if (cfg == null) return;
        
        String url = cfg.getProperty("connection.datasource");
        if (url == null)//不是数据源方式
        	url = cfg.getProperty("connection.url");
        String user = cfg.getProperty("connection.username");
        String password = cfg.getProperty("connection.password");

        centralDB.setUrl(url);
        centralDB.setUser(user);
        centralDB.setPassword(password);
        
        /**
         * hibernate配置文件中dialect的格式：
         * org.hibernate.dialect.Oracle9Dialect
         * 需要先取出最后一段，然后用我们的类型比较，起始部分相符即可
         * 如oracle/sybase/sqlserver/mysql
         * 
         */
        String dialect = cfg.getProperty("dialect");
        if (dialect == null) return;

        dialect = dialect.substring(dialect.lastIndexOf('.') + 1).toLowerCase();
        
        String[] dbTypes = DBType.getAllTypes();
        for (int i = 0; i < dbTypes.length; i++) 
        {
        	if (dialect.startsWith(dbTypes[i]))
        	{
        		centralDB.setType(dbTypes[i]);
        		break;
        	}
		}
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.context._BaseDAO#getConfigurationFileName()
	 */
	protected String getConfigureFile()
	{
		return configFile;
	}
}
