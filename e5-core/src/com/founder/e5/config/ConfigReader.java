package com.founder.e5.config;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.digester.Digester;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;

import com.founder.e5.context.Context;
import com.founder.e5.context.EUID;
import com.founder.e5.db.DBSessionFactory;
/**
 * <p>系统启动的配置文件读取类</p>
 * <p>Description: 
 * 系统启动时需首先执行这个文件的getConfigure(String)方法来获取
 * e5-config.xml中的所有配置信息。
 * <BR>所有配置信息都保存在这个类的单一实例中。
 * </p>
 * <p>
 * 这个类应该由web.xml中配置的load-on-startup的
 * 某servlet来进行加载
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Founder</p>
 * @created 2005-7-13
 * @author Gong Lijie
 * @version 1.0
 */
public class ConfigReader
{
    //配置的缓存项
    private CacheConfig cacheConfig;
    //中心数据库信息
    private InfoCentralDB centralDB;
	//系统启动时的自动加载项
    private List listRestart = new ArrayList(10);
    //平台字段信息 key=id(in config file) value=InfoField
    private Map mapField = new HashMap(17);
    //工厂类单例信息
    private List listFactory = new ArrayList(10);
    //机构类型信息
    private List listOrg = new ArrayList(10);
    //多应用服务器信息
    private List listServer = new ArrayList(2);
    //DBSession
    private List listDBSession = new ArrayList(2);
    //ID-Config
    private List listID = new ArrayList(3);
    //系统权限TAB页
    private List listSysPermissionPage = new ArrayList(4);
    
    private InfoSystemLoad systemLoad;
    //配置的服务器端的Locale信息
    private InfoLocale infoLocale;
    //个性化定制信息，直接创建出一个，根据配置文件再添加item
    private InfoCustomize infoCustomize = new InfoCustomize();
    
    /**
     * Singleton
     */
    private static ConfigReader reader;
    private ConfigReader(){}
    public synchronized static ConfigReader getInstance(){
    	if (reader == null) reader = new ConfigReader();
    	return reader;
    }
	
    public String toString()
    {
        StringBuffer sbInfo = new StringBuffer(500);
        
        sbInfo.append(cacheConfig.toString());
        sbInfo.append("\n=========centralDB\n");
        sbInfo.append(centralDB.toString());
        sbInfo.append("\n=========listFactory\n");
        sbInfo.append(listFactory.toString());
        sbInfo.append("\n=========listOrg\n");
        sbInfo.append(listOrg.toString());
        sbInfo.append("\n=========listRestart\n");
        sbInfo.append(listRestart.toString());
        sbInfo.append("\n=========listServer\n");
        sbInfo.append(listServer.toString());
        sbInfo.append("\n=========mapField\n");
        sbInfo.append(mapField.toString());

        sbInfo.append("\n=========listDBSession\n");
        sbInfo.append(listDBSession.toString());
        sbInfo.append("\n=========listID\n");
        sbInfo.append(listID.toString());

        return sbInfo.toString();
    }
    
    /**
	 * @return Returns the cacheConfig.
	 */
	public CacheConfig getCacheConfig()
	{
		return cacheConfig;
	}
	/**
	 * @return Returns the centralDB.
	 */
	public InfoCentralDB getCentralDB()
	{
		return centralDB;
	}
	/**
	 * @return Returns the Field Map.
	 * key:id(in config file) 
	 * value:InfoField
	 */
	public Map getFields()
	{
		return mapField;
	}
	/**
	 * @return Returns the listOrg.
	 */
	public List getOrgs()
	{
		return listOrg;
	}
	/**
	 * @return 工厂单例配置的List
	 */
	public List getFactories()
	{
		return listFactory;
	}
	/**
	 * @return Returns the listRestart.
	 */
	public List getServers()
	{
		return listServer;
	}
	/**
	 * @return Returns the System Permission Page List
	 */
	public List getPermissionPages()
	{
		return listSysPermissionPage;
	}

	/**
	 * @return 返回DBSession的配置信息
	 */
	public List getDBSessions()
	{
		return listDBSession;
	}

	/**
	 * @return 返回ID的配置信息
	 */
	public List getIDs()
	{
		return listID;
	}
	/**
	 * @return 返回系统模版加载的配置项目
	 */
	public InfoSystemLoad getSystemLoad()
	{
		return systemLoad;
	}
	
	/**
	 * @return Returns the listRestart.
	 */
	public List getRestarts()
	{
		return listRestart;
	}
	
	public InfoLocale getLocale()
	{
		return infoLocale;
	}
	
	public InfoCustomize getCustomize()
	{
		return infoCustomize;
	}
	//===========AddInfo Method================
	/**
	 * @param info
	 */
    public void addInfo(InfoRestart info)
    {
    	listRestart.add(info);
    }
    public void addInfo(CacheConfig info)
    {
    	cacheConfig = info;
    }
    public void addInfo(InfoCentralDB info)
    {
    	centralDB = info;
    }
    public void addInfo(InfoField info)
    {
    	mapField.put(info.getId(), info);
    }
    public void addInfo(InfoOrg info)
    {
    	listOrg.add(info);
    }

    public void addInfo(InfoDBSession info)
    {
    	listDBSession.add(info);
    }

    public void addInfo(InfoPermissionPage info)
    {
    	listSysPermissionPage.add(info);
    }

    public void addInfo(InfoID info)
    {
    	listID.add(info);
    }

    public void addInfo(InfoFactory info)
    {
    	listFactory.add(info);
    }

    public void addInfo(InfoSystemLoad info)
    {
    	systemLoad = info;
    }
    public void addInfo(InfoServer server)
    {
    	listServer.add(server);
    }
    public void addInfo(InfoLocale info)
    {
    	infoLocale = info;    	
    }
    //个性化定制的TAB
    public void addInfo(InfoCustomizeItem info)
    {
    	infoCustomize.addInfo(info);
    }
    //个性化定制的“是否允许工具条定制”
    public void addCustomizeToolkit(String info)
    {
    	infoCustomize.setCustomizeToolkit(info);
    }
    //个性化定制的“缺省工具栏操作按钮显示方式”
    public void addToolkitButtonStyle(String info)
    {
    	infoCustomize.setDefaultButtonStyle(info);
    }
	//===========end.(AddInfo Method)================
    
	private static boolean checkLicense()		
	{		
		return E5SecurityManager.getInstance().checkLicense();
	}
	
	public static String getLicenseValue(String key){
		return E5SecurityManager.getInstance().getLicenseValue(key);
	}
	
     /**
     * 解析e5-config.xml，取得所有配置信息
     * @param configFile 配置文件路径，用于WEB应用
     * @throws Exception
     * @return ConfigRestart
     */
    public synchronized boolean getConfigure(String configFile)
    {
		if(!checkLicense())
		{
			return false;
		}
		
        InputStream in = Thread.currentThread().getContextClassLoader()
			.getResourceAsStream(configFile);
        if (in == null)
        	throw new RuntimeException("No configuration file!!" + configFile);
        try
		{
            getConfigure(in);
		}
		finally{
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("[E5 getConfigure]Configuration file close exception");
				e.printStackTrace();
			}
		}
		return true;
    }
   /**
     * 解析配置文件
     * @param in 配置文件的输入流。由外部传入
     */
    public synchronized void getConfigure(InputStream in)
    {
    	//一次性检查。检查是否已经读过配置文件
    	if (centralDB != null) return;
    	
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);
        digester.push(this);
        digester.addRuleSet(new RuleSetConfig());

        try
		{
			digester.parse(in);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Configuration file io exception!!! ", e);
		}
		catch (SAXException e)
		{
			throw new RuntimeException("Configuration file parse exception!!! ", e);
		}
    }

    private String configFilePath = "e5-config.xml";
    
    /**
     * 只在本地测试环境中使用的，加载Spring的context的方法
     * 注意此方法造成config包和context包的相互依赖，由于是本地测试方法，不做处理
     */
    private void setApplicationContext()
    {
    	ApplicationContext ctx = new ClassPathXmlApplicationContext("e5-context.xml");
    	Context.setApplicationContext(ctx);
    }
    /**
     * 解析e5-config.xml，取得所有配置信息
     * <BR>该方法只用于本地测试环境，其中包含了自动加载项的执行
     * <BR>实际使用中应使用带参数的重载方法
     * @throws Exception
     * @return ConfigRestart
     */
    public synchronized void getConfigure()
    {
    	setApplicationContext();
    	getConfigure(configFilePath);

    	//在本地测试时，自动调用启动项
    	try
		{
			initRestart();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Service initialization exception!!! ", e);
		}
    }
    
    /**
     * 调用启动类，用于在本地调试时自动启动
     * 注意此方法造成config包和context包的相互依赖，由于是本地测试方法，不做处理
     */
    private void initRestart() throws Exception
    {
        InfoRestart[] infos = (InfoRestart[]) listRestart.toArray(new InfoRestart[0]);
        if (infos == null) return;
        //Restart init
        for (int i = 0;  i < infos.length; i++)
        {
            Class myClass = Class.forName(infos[i].getInvokeClass());
            Method method = myClass.getMethod(infos[i].getInvokeMethod(), null);
            if (Modifier.isStatic(method.getModifiers()))
                method.invoke(null, null);
            else
                method.invoke(myClass.newInstance(), null);
        }
        //DBSession Init
        for (int i = 0; i < listDBSession.size(); i++)
		{
			InfoDBSession db = (InfoDBSession)getDBSessions().get(i);
	        DBSessionFactory.registerDB(db.getName(), db.getImplementation());
		}
        //ID Init
        for (int i = 0; i < listID.size(); i++)
		{
			InfoID id = (InfoID)getIDs().get(i);
	        EUID.registerID(id.getName(), id.getType(), id.getParam());
		}
    }
    public static void main(String[] args)
    {
    	ConfigReader.getInstance().getConfigure();
    }
}
