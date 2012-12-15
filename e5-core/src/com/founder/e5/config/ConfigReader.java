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
 * <p>ϵͳ�����������ļ���ȡ��</p>
 * <p>Description: 
 * ϵͳ����ʱ������ִ������ļ���getConfigure(String)��������ȡ
 * e5-config.xml�е�����������Ϣ��
 * <BR>����������Ϣ�������������ĵ�һʵ���С�
 * </p>
 * <p>
 * �����Ӧ����web.xml�����õ�load-on-startup��
 * ĳservlet�����м���
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Founder</p>
 * @created 2005-7-13
 * @author Gong Lijie
 * @version 1.0
 */
public class ConfigReader
{
    //���õĻ�����
    private CacheConfig cacheConfig;
    //�������ݿ���Ϣ
    private InfoCentralDB centralDB;
	//ϵͳ����ʱ���Զ�������
    private List listRestart = new ArrayList(10);
    //ƽ̨�ֶ���Ϣ key=id(in config file) value=InfoField
    private Map mapField = new HashMap(17);
    //�����൥����Ϣ
    private List listFactory = new ArrayList(10);
    //����������Ϣ
    private List listOrg = new ArrayList(10);
    //��Ӧ�÷�������Ϣ
    private List listServer = new ArrayList(2);
    //DBSession
    private List listDBSession = new ArrayList(2);
    //ID-Config
    private List listID = new ArrayList(3);
    //ϵͳȨ��TABҳ
    private List listSysPermissionPage = new ArrayList(4);
    
    private InfoSystemLoad systemLoad;
    //���õķ������˵�Locale��Ϣ
    private InfoLocale infoLocale;
    //���Ի�������Ϣ��ֱ�Ӵ�����һ�������������ļ������item
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
	 * @return �����������õ�List
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
	 * @return ����DBSession��������Ϣ
	 */
	public List getDBSessions()
	{
		return listDBSession;
	}

	/**
	 * @return ����ID��������Ϣ
	 */
	public List getIDs()
	{
		return listID;
	}
	/**
	 * @return ����ϵͳģ����ص�������Ŀ
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
    //���Ի����Ƶ�TAB
    public void addInfo(InfoCustomizeItem info)
    {
    	infoCustomize.addInfo(info);
    }
    //���Ի����Ƶġ��Ƿ������������ơ�
    public void addCustomizeToolkit(String info)
    {
    	infoCustomize.setCustomizeToolkit(info);
    }
    //���Ի����Ƶġ�ȱʡ������������ť��ʾ��ʽ��
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
     * ����e5-config.xml��ȡ������������Ϣ
     * @param configFile �����ļ�·��������WEBӦ��
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
     * ���������ļ�
     * @param in �����ļ��������������ⲿ����
     */
    public synchronized void getConfigure(InputStream in)
    {
    	//һ���Լ�顣����Ƿ��Ѿ����������ļ�
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
     * ֻ�ڱ��ز��Ի�����ʹ�õģ�����Spring��context�ķ���
     * ע��˷������config����context�����໥�����������Ǳ��ز��Է�������������
     */
    private void setApplicationContext()
    {
    	ApplicationContext ctx = new ClassPathXmlApplicationContext("e5-context.xml");
    	Context.setApplicationContext(ctx);
    }
    /**
     * ����e5-config.xml��ȡ������������Ϣ
     * <BR>�÷���ֻ���ڱ��ز��Ի��������а������Զ��������ִ��
     * <BR>ʵ��ʹ����Ӧʹ�ô����������ط���
     * @throws Exception
     * @return ConfigRestart
     */
    public synchronized void getConfigure()
    {
    	setApplicationContext();
    	getConfigure(configFilePath);

    	//�ڱ��ز���ʱ���Զ�����������
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
     * ���������࣬�����ڱ��ص���ʱ�Զ�����
     * ע��˷������config����context�����໥�����������Ǳ��ز��Է�������������
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
