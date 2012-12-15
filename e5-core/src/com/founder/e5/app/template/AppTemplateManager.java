package com.founder.e5.app.template;
import java.io.File;

import com.founder.e5.app.App;
import com.founder.e5.app.AppFactory;
import com.founder.e5.app.AppManager;
import com.founder.e5.app.template.DomTemplateManager;
import com.founder.e5.app.template.FlowTemplateManager;
import com.founder.e5.app.template.IAppTemplateManager;
import com.founder.e5.app.template.SysTemplateManager;
import com.founder.e5.commons.Log;
import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @version 1.0
 * @created 11-七月-2005 09:47:34
 */
public class AppTemplateManager implements IAppTemplateManager {

	public DomTemplateManager m_DomTemplateManager;
	public FlowTemplateManager m_FlowTemplateManager;
	public SysTemplateManager m_SysTemplateManager;

	public AppTemplateManager(){
	    m_DomTemplateManager = new DomTemplateManager();
	    m_FlowTemplateManager = new FlowTemplateManager();
	    m_SysTemplateManager = new SysTemplateManager();
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int, java.lang.String)
	 */
	public int load(int appID, String templateFile) 
	throws E5Exception
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
	 * @see com.founder.e5.app.template.IAppTemplateManager#load(int, org.dom4j.Document)
	 */
	public int load(int appID, Document doc) 
	throws E5Exception 
	{
	    /**
	     * bSuccedType表示子系统上载到那个阶段
	     * 1为系统模板加载成功
	     * 2表示文档加载成功
	     * 3表示流程加载成功
	     */
	    int bSuccedType=0;

	    int aID = 0;
	    BaseDAO dao = new BaseDAO();
	    Session session = null;
	    Transaction tx = null;
	    //先加载系统部分
	    try
	    {
	        session = dao.getSession();
	        tx = session.beginTransaction();
	        aID = loadApp(doc, session);
            
            m_SysTemplateManager.setSession(session);
		    m_SysTemplateManager.load(aID, doc);
		    bSuccedType++;
		    tx.commit();
	    }
	    catch(Exception ex)
	    {
	        ResourceMgr.rollbackQuietly(tx);
	        throw new E5Exception("[AppTemplate.load]Error when load system:", ex);
	    }
	    finally
	    {
	        ResourceMgr.closeQuietly(session);
	    }
	    //再加载文档和流程
	    try
	    {
		    m_DomTemplateManager.setSession(session);
		    m_DomTemplateManager.load(aID, doc);
		    bSuccedType++;
		    
		    m_FlowTemplateManager.setSession(session);
		    m_FlowTemplateManager.load(aID, doc);
		    bSuccedType++;
	    }
	    catch(Exception ex)
	    {
    		if(bSuccedType == 2) //2表示文档部分加载成功了，流程部分异常。3表示全成功！
    		{
    			m_FlowTemplateManager.unload(aID);
    			m_DomTemplateManager.unload(aID);
    		}
    		if(bSuccedType==1) //1表示系统部分成功了，文档部分加载时异常。
    		{
    			m_DomTemplateManager.unload(aID);
    		}
    		//卸载系统部分
    	    try{
    		    session = dao.getSession();
    	        tx = session.beginTransaction();
                m_SysTemplateManager.setSession(session);
    		    m_SysTemplateManager.unload(appID);
    		    tx.commit();
    	    }finally{
    	        ResourceMgr.closeQuietly(session);
    	    }   		
	        throw new E5Exception("[AppTemplate.load]Error when load dom and flows:", ex);
	    }
		return aID;
	}

	/**
	 * 根据给定的DOM对象装载子系统
	 * @param Document    需要装载的模板文件
	 * @param session hibernate session 对象
	 */
	private int loadApp(Document doc, Session session) 
	throws E5Exception
	{
	    try
        {
            Element root = doc.getRootElement();
            String appName = root.element("AppName").attribute("value").getText();
            String absVersion = root.element("AppAbsVersion").attribute("value").getText();
            String appVersion = root.element("AppVersion").attribute("value").getText();
            String appProvider = root.element("AppProvider").attribute("value").getText();
            
            App app = new App();
            app.setName(appName);
            app.setAbsVersion(absVersion);
            app.setVersion(appVersion);
            app.setProvider(appProvider);
            
            AppManager appManager = AppFactory.getAppManager();
            if(appManager.exist(app))
                throw new E5Exception("App has exist:" + appName);
            
            appManager.create(app, session);
            return app.getAppID();
        }
        catch (Exception e)
        {
            throw new E5Exception("[Load application template - system]", e);
        }
	}

	/**
	 * 导出子系统基本信息
	 * 
	 * @param 子系统ID
	 */
	private String exportApp(int appID) throws E5Exception{
	    AppManager manager = AppFactory.getAppManager();
	    App app = manager.get(appID);
	    StringBuffer sb = new StringBuffer();
	    if(app != null)
	    {
	        sb.append("  <AppName value=\"").append(app.getName()).append("\"/>\n");
            sb.append("  <AppAbsVersion value=\"").append(app.getAbsVersion()).append("\"/>\n");
            sb.append("  <AppVersion value=\"").append(app.getVersion()).append("\"/>\n");
            sb.append("  <AppProvider value=\"").append(app.getProvider()).append("\"/>\n");
	    }
		return sb.toString();
	}

	/**
	 * 卸载appID指定的子系统
	 * @param appID    子系统ID
	 * 
	 */
	public void unload(int appID) throws E5Exception{
    	Log log = Context.getLog("e5.sys");
    	//先删系统部分
        BaseDAO dao = new BaseDAO();
	    Session session = null;
	    Transaction tx = null;
	    try
	    {
		    session = dao.getSession();
	        tx = session.beginTransaction();
            m_SysTemplateManager.setSession(session);
		    m_SysTemplateManager.unload(appID);
		    tx.commit();
	    }
	    catch(Exception ex)
	    {
	        ResourceMgr.rollbackQuietly(tx);
        	log.error("[AppTemplate.unload]Error when unload system:", ex);
	        throw new E5Exception("[AppTemplate.unload]Error when unload system!", ex);
	    }
	    finally
	    {
	        ResourceMgr.closeQuietly(session);
	    }
	    //再删流程部分
	    try
        {
		    m_FlowTemplateManager.unload(appID);
        }
        catch(Exception ex){
        	log.error("[AppTemplate.unload]Error when unload flows:", ex);
        }
        //再删文档部分
	    try {
			m_DomTemplateManager.unload(appID);
		} catch (Exception ex) {
        	log.error("[AppTemplate.unload]Error when unload dom:", ex);
		}

	}

	/**
	 * 导出appID指定的子系统
	 * @param appID    子系统ID
	 * 
	 */
	public String exportTemplate(int appID) throws E5Exception{
	    StringBuffer sb = new StringBuffer();
	    sb.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>\n");
        sb.append("<Application>\n");
        sb.append(exportApp(appID));
        sb.append(m_SysTemplateManager.exportTemplate(appID));
        sb.append(m_DomTemplateManager.exportTemplate(appID));
        try{
        	String flowXml =m_FlowTemplateManager.exportTemplate(appID);
        	sb.append(flowXml);
        }
        catch(Exception ex){}
        sb.append("</Application>");
		return sb.toString();
	}

}