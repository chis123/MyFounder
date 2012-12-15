package com.founder.e5.app.template;
import java.io.File;
import java.util.Iterator;

import org.hibernate.Session;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.founder.e5.app.App;
import com.founder.e5.app.AppFactory;
import com.founder.e5.app.AppManager;
import com.founder.e5.app.AppPermission;
import com.founder.e5.app.AppPermissionManager;
import com.founder.e5.app.AppWebAddress;
import com.founder.e5.app.AppWebAddressManager;
import com.founder.e5.app.template.IAppTemplateManager;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.SysConfig;
import com.founder.e5.sys.SysConfigManager;
import com.founder.e5.sys.SysFactory;

/**
 * @version 1.0
 * @created 11-七月-2005 09:47:59
 */
public class SysTemplateManager implements IAppTemplateManager {

    private Session session = null;
	public SysTemplateManager(){
	}
	void setSession(Session session)
	{
	    this.session = session;
	}
	/**
	 * 导出子系统web页面地址信息
	 * 
	 * @param appID  子系统ID
	 */
	private String exportAppWebAddresses(int appID) throws E5Exception{
	    StringBuffer sb = new StringBuffer();
	    AppWebAddressManager manager = AppFactory.getAppWebAddressManager();
	    AppWebAddress[] appWeb = manager.get(appID);
	    if(appWeb != null)
	    {
	        sb.append("  <AppManagerWeb>\n");
            for(int i = 0; i < appWeb.length; i++)
	        {
                AppWebAddress addr = appWeb[i];
	            sb.append("    <AppWebAddress>\n");
                sb.append("      <WebName value=\"").append(addr.getWebName()).append("\"/>\n");
                sb.append("      <WebURL value=\"").append(addr.getWebURL()).append("\"/>\n");
                sb.append("      <Icon value=\"").append(addr.getIcon()).append("\"/>\n");
                sb.append("    </AppWebAddress>\n");                
	        }
            sb.append("  </AppManagerWeb>\n");
	    }
		return sb.toString();
	}

	/**
	 * 导出子系统权限设定页面信息
	 * 
	 * @param appID  子系统ID
	 */
	private String exportAppPermission(int appID) throws E5Exception{
	    StringBuffer sb = new StringBuffer();
	    AppPermissionManager manager = AppFactory.getAppPermissionManager();
	    AppPermission[] appPerm = manager.get(appID);
	    if(appPerm != null)
	    {
	        sb.append("  <Permission>\n");
	        for(int i = 0; i < appPerm.length; i++)
	        {
	            AppPermission perm = appPerm[i];
	            sb.append("    <AppPermission>\n");
	            sb.append("      <NResourceType value=\"").append(perm.getResourceType()).append("\"/>\n");
	            sb.append("      <CommonURL value=\"").append(perm.getCommonURL()).append("\"/>\n");
	            sb.append("      <Notes value=\"");
	            if(perm.getNotes() != null)
	                sb.append(perm.getNotes());
	            sb.append("\"/>\n");
	            sb.append("    </AppPermission>\n");
            }
	       sb.append("  </Permission>\n");
	    }
		return sb.toString();
	}

	/**
	 * 导出子系统配置信息
	 * 
	 * @param appID 子系统ID
	 */
	private String exportConfig(int appID) throws E5Exception{
	    SysConfigManager manager = SysFactory.getSysConfigManager();
	    StringBuffer sb = new StringBuffer();
	    SysConfig[] sysConfig = manager.getAppSysConfigs(appID);
	    if(sysConfig != null)
	    {
	        sb.append("  <SysConfig>\n");
	        for(int i = 0; i < sysConfig.length; i++)
	        {
	            SysConfig config = sysConfig[i];
	            sb.append("    <AppSysConfig>\n");
	            sb.append("      <Project value=\"").append(config.getProject()).append("\"/>\n");
	            sb.append("      <Item value=\"").append(config.getItem()).append("\"/>\n");
	            sb.append("      <Value value=\"").append(config.getValue()).append("\"/>\n");
	            sb.append("      <Note value=\"");
	            if(config.getNote() != null)
	                sb.append(config.getNote());
	            sb.append("\"/>\n");
	            sb.append("    </AppSysConfig>\n");
	        }
	        sb.append("  </SysConfig>\n");
	    }
		return sb.toString();
	}

	/**
	 * 导出子系统初始化信息
	 * 
	 * @param appID  子系统ID
	 */
	private String exportAppInit(int appID) throws E5Exception{
	    StringBuffer sb = new StringBuffer();
	    AppManager manager = AppFactory.getAppManager();
	    App app = manager.get(appID);
	    if(app != null && app.getInitInfo() != null)
	    {
	        sb.append("  <Init>\n");
	        sb.append("    <ClassName value=\"").append(app.getInitInfo()).append("\" />\n"); 
	        sb.append("  </Init>\n");
	    }
	    return sb.toString();
	}

	
	/**
	 * 卸载appID指定的子系统
	 * @param appID    子系统ID
	 * 
	 */
	public void unload(int appID) throws E5Exception{
	    AppManager appManager = AppFactory.getAppManager();
	    AppWebAddressManager addressManager = AppFactory.getAppWebAddressManager();
	    AppPermissionManager permissionManager = AppFactory.getAppPermissionManager();
	    SysConfigManager configManager = SysFactory.getSysConfigManager();
	    
	    appManager.delete(appID, session);
	    addressManager.delete(appID, session);
	    permissionManager.delete(appID, session);
	    configManager.deleteAppSysConfig(appID, session);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int, java.lang.String)
	 */
	public int load(int appID, String templateFile) throws E5Exception
	{
	    SAXReader reader = new SAXReader();
	    Document doc = null;
        try {
			doc = reader.read(new File(templateFile));
		} catch (DocumentException e) {
			throw new E5Exception("Invalid Template File", e);
		}
		return load(0, doc);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.app.template.IAppTemplateManager#exportTemplate(int)
	 */
	public String exportTemplate(int appID) throws E5Exception{
	    StringBuffer sb = new StringBuffer();
	    sb.append(exportAppWebAddresses(appID));
	    sb.append(exportAppPermission(appID));
	    sb.append(exportConfig(appID));
	    sb.append(exportAppInit(appID));
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int, org.dom4j.Document)
	 */
	public int load(int appID, Document doc) 
	throws E5Exception 
	{
	    AppManager appManager = AppFactory.getAppManager();
	    AppWebAddressManager addressManager = AppFactory.getAppWebAddressManager();
	    AppPermissionManager permissionManager = AppFactory.getAppPermissionManager();
	    SysConfigManager configManager = SysFactory.getSysConfigManager();
	    try
        {
            Element root = doc.getRootElement();
            if (root == null) return 0;
            Element appWeb = root.element("AppManagerWeb");
            if (appWeb != null)
            for(Iterator it = appWeb.elementIterator("AppWebAddress"); it.hasNext();)
            {
                Element webAddress = (Element)it.next();
                String webName = webAddress.element("WebName").attribute("value").getText();
                String webURL = webAddress.element("WebURL").attribute("value").getText();
//                String parentName = webAddress.element("ParentName").attribute("value").getText();
                String icon = webAddress.element("Icon").attribute("value").getText();
                AppWebAddress appWebAddress = new AppWebAddress();
                appWebAddress.setAppID(appID);
                appWebAddress.setWebName(webName);
                appWebAddress.setWebURL(webURL);
                appWebAddress.setIcon(icon);
                addressManager.create(appWebAddress, session);
            }
            Element perm = root.element("Permission");
            if (perm != null)
            for(Iterator it = perm.elementIterator("AppPermission"); it.hasNext();)
            {
                Element appPerm = (Element)it.next();
                String resourceType = appPerm.element("NResourceType").attribute("value").getText();
                String commonURL = appPerm.element("CommonURL").attribute("value").getText();
                String notes = appPerm.element("Notes").attribute("value").getText();
                AppPermission appPermission = new AppPermission();
                appPermission.setAppID(appID);
                appPermission.setResourceType(resourceType);
                appPermission.setCommonURL(commonURL);
                appPermission.setNotes(notes);
                permissionManager.create(appPermission, session);
            }
            Element configs = root.element("SysConfig");
            if (configs != null)
            for(Iterator it = configs.elementIterator("AppSysConfig"); it.hasNext();)
            {
                Element sysConfig = (Element)it.next();
                String project = sysConfig.element("Project").attribute("value").getText();
                String item = sysConfig.element("Item").attribute("value").getText();
                String value = sysConfig.element("Value").attribute("value").getText();
//                String note = sysConfig.element("Note").attribute("value").getText();
                SysConfig config = new SysConfig();
                config.setAppID(appID);
                config.setItem(item);
                config.setProject(project);
                config.setValue(value);
                configManager.create(config, session);
            }
            if(root.element("Init") != null)
            {
                if(root.element("Init").element("ClassName") != null)
                {
                    if(root.element("Init").element("ClassName").attribute("value") != null)
                    {
                        String initClass = root.element("Init").element("ClassName").attribute("value").getText();
                        Object init = Class.forName(initClass).newInstance();
                        java.lang.reflect.Method method = init.getClass().getMethod("doInit",null);
                        method.invoke(init, null);
                        App app = appManager.get(appID);
                        app.setInitInfo(initClass);
                        appManager.update(app);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new E5Exception("[Load application template-sytem]", e);
        }
	    return 0;
	}

}