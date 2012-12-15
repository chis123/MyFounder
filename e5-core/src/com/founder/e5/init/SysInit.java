package com.founder.e5.init;


import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.hibernate.Session;

import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoSystemLoad;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.E5Exception;
import com.founder.e5.init.Inited;
import com.founder.e5.commons.ResourceMgr;

/**
 * @version 1.0
 * @created 08-七月-2005 15:00:04
 */
public class SysInit implements Inited {

	public SysInit(){

	}

	
	/**
	 * 初始化方法
	 */
	public boolean init() throws E5Exception{
	    ConfigReader reader = ConfigReader.getInstance();
	    InfoSystemLoad load = reader.getSystemLoad();
	    String sysInit = load.getSysInit();
        String domInit = load.getDomInit();
        String flowInit = load.getFlowInit();
        String sqlScript = load.getSqlScript();
        BaseDAO dao = new BaseDAO();
        Session session = null;
        Connection conn = null;
        Statement stmt = null;
	    try
        {
	        session = dao.getSession();
            conn = session.connection();
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            SAXReader saxReader = new SAXReader();
	        Document doc = saxReader.read(new File(sqlScript));
            Element root = doc.getRootElement();
            if("true".equals(sysInit))
            {
                Element sys = root.element("sys");
                Iterator it = sys.elementIterator("sql");
                while(it.hasNext())
                {
                    Element sql = (Element)it.next();
                    stmt.executeUpdate(sql.getText());
                }
                Element sysadmin = root.element("sysadmin");
                it = sysadmin.elementIterator("sql");
                while(it.hasNext())
                {
                    Element sql = (Element)it.next();
                    if(sql.getText() != null && !sql.getTextTrim().equals(""))
                        stmt.executeUpdate(sql.getText());
                }
                Element sysparam = root.element("sysparam");
                it = sysparam.elementIterator("sql");
                while(it.hasNext())
                {
                    Element sql = (Element)it.next();
                    if(sql.getText() != null && !sql.getTextTrim().equals(""))
                        stmt.executeUpdate(sql.getText());
                }
            }
            if("true".equals(domInit))
            {
                Element flow = root.element("flow");
                Iterator it = flow.elementIterator("sql");
                while(it.hasNext())
                {
                    Element sql = (Element)it.next();
                    if(sql.getText() != null && !sql.getTextTrim().equals(""))
                        stmt.executeUpdate(sql.getText());
                }
            }
            if("true".equals(flowInit))
            {
                Element dom = root.element("dom");
                Iterator it = dom.elementIterator("sql");
                while(it.hasNext())
                {
                    Element sql = (Element)it.next();
                    if(sql.getText() != null && !sql.getTextTrim().equals(""))
                        stmt.executeUpdate(sql.getText());
                }
            }
            conn.commit();
            conn.setAutoCommit(autoCommit);
        }
        catch (Exception e)
        {
            ResourceMgr.rollbackQuietly(conn);
            throw new E5Exception("", e);
        }
	    finally
	    {
	        ResourceMgr.closeQuietly(stmt);
	        ResourceMgr.closeQuietly(conn);
	        ResourceMgr.closeQuietly(session);
	    }
		return false;
	}

}