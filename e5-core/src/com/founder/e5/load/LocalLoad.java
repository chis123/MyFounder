package com.founder.e5.load;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;

import com.founder.e5.commons.Log;
import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoDBSession;
import com.founder.e5.config.InfoID;
import com.founder.e5.config.InfoLocale;
import com.founder.e5.config.InfoRestart;
import com.founder.e5.context.Context;
import com.founder.e5.context.EUID;
import com.founder.e5.db.DBSessionFactory;

public class LocalLoad
{
	private Log log = Context.getLog("e5.context");
	/**
	 * 按文件进行初始化
	 * @param configFile
	 * @throws Exception
	 */
	public void init(String configFile)
    {
        //调用ConfigReader读取所有配置信息
        ConfigReader reader = ConfigReader.getInstance();
		try
		{
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);
			if (in == null) in = new FileInputStream(configFile);

			reader.getConfigure(in);
//			reader.getConfigure(new FileInputStream(configFile));
			ResourceMgr.closeQuietly(in);
			
			log.info("[E5Load]Configuration file loaded.");
			//配置信息读取进来后，做初始的赋值以及自动加载工作

			load(reader);
			log.info("[E5Load]Initialization finished.");
		}
		catch (Exception e)
		{
			log.error("[E5Load]", e);
		}
    }
    /**
     * 该方法用于执行配置文件中配置的自动启动项
     * @param reader
     * @throws Exception
     */
    void load(ConfigReader reader) throws Exception
    {
        InfoRestart[] infos = (InfoRestart[]) reader.getRestarts().toArray(new InfoRestart[0]);
        if (infos == null) return;
        //先设置Locale信息
        InfoLocale infoLocale = reader.getLocale();
        Locale locale;
        if (infoLocale != null)
        {
        	if (infoLocale.getVariant() != null)
        		locale = new Locale(infoLocale.getLanguage(), infoLocale.getCountry(),
        				infoLocale.getVariant());
        	else if (infoLocale.getCountry() != null)
        		locale = new Locale(infoLocale.getLanguage(), infoLocale.getCountry());
        	else
        		locale = new Locale(infoLocale.getLanguage());
        }
        else
        	locale = Locale.CHINA;
        Context.setLocale(locale);

        /**
         * =====Restart init====
         * BaseDAO.init
         * FactoryManager.init
         * CacheManager.init
         */
        for (int i = 0;  i < infos.length; i++)
        {
        	if (log.isInfoEnabled())
        	{
        		log.info("[E5Load]restart:" + infos[i].getInvokeClass());
        		log.info("[E5Load]." + infos[i].getInvokeMethod());
        	}
            Class myClass = Class.forName(infos[i].getInvokeClass());
            Method method = myClass.getMethod(infos[i].getInvokeMethod(), null);
            if (Modifier.isStatic(method.getModifiers()))
                method.invoke(null, null);
            else
                method.invoke(myClass.newInstance(), null);
    		log.info("[E5Load]restart ok.");
        }
        //DBSession Init
		log.info("[E5Load]DBSession registering...");
        for (int i = 0; i < reader.getDBSessions().size(); i++)
		{
			InfoDBSession db = (InfoDBSession)reader.getDBSessions().get(i);
	        DBSessionFactory.registerDB(db.getName(), db.getImplementation());
		}
		log.info("[E5Load]DBSession registered.");
        //ID Init
        for (int i = 0; i < reader.getIDs().size(); i++)
		{
			InfoID id = (InfoID)reader.getIDs().get(i);
	        EUID.registerID(id.getName(), id.getType(), id.getParam());
		}
		log.info("[E5Load]EUID registered.");
    }
}
