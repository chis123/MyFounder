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
 * ��Hibernate�����Ĺ�����
 * �̳���Hibernate Sychronizerʵ�ֵĳ�����
 * @created on 2005-7-12
 * @author Gong Lijie
 * @version 1.0
 */
public class BaseDAO extends _BaseDAO
{
	private static String configFile = null;
	
	/**
	 * �������ļ�����Hibernate��SessionFactory��ʼ��
	 * �÷���ֻ�ڳ�ʼ��ʱ��һ��
	 * ϵͳ����ʱ�Զ�����
	 */
	public static synchronized Configuration init()
	throws HibernateException 
	{
		//��e5-config�����ļ��ж�ȡ���������ݿ���Ϣ��Hiberante�����ļ���
        InfoCentralDB centralDB = ConfigReader.getInstance().getCentralDB();
		if (centralDB == null) throw new RuntimeException("No CentralDB info!");
        configFile = centralDB.getConfigFile();
        
		//����String��ʽ����Hibernate������
		//����費��WEB��ʽ�������õ��Ǿ���·��������File���ͼ���һ��
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
	 * ����Hibernate��SessionFactory���ͷŵ���Դ��
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
    //�÷��ص�Hiberante���ö����ٰ��������ݿ��url/user/pwd����Ϣ����
	protected static void fillCentralDB(Configuration cfg, InfoCentralDB centralDB)
	{
        if (cfg == null) return;
        
        String url = cfg.getProperty("connection.datasource");
        if (url == null)//��������Դ��ʽ
        	url = cfg.getProperty("connection.url");
        String user = cfg.getProperty("connection.username");
        String password = cfg.getProperty("connection.password");

        centralDB.setUrl(url);
        centralDB.setUser(user);
        centralDB.setPassword(password);
        
        /**
         * hibernate�����ļ���dialect�ĸ�ʽ��
         * org.hibernate.dialect.Oracle9Dialect
         * ��Ҫ��ȡ�����һ�Σ�Ȼ�������ǵ����ͱȽϣ���ʼ�����������
         * ��oracle/sybase/sqlserver/mysql
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
