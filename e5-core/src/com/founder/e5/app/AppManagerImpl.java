package com.founder.e5.app;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;


/**
 * @version 1.0
 * @created 11-七月-2005 12:45:11
 */
class AppManagerImpl implements AppManager {

	public AppManagerImpl(){

	}

	/**
	 * 创建子系统对象
	 * 
	 * @param app  子系统对象
	 * @throws E5Exception
	 */
	public void create(App app) throws E5Exception{
	    int appID = 1;
	    try
        {
		    appID = 34;//(int)EUID.getID("AppID");
		    app.setAppID(appID);
		    BaseDAO dao = new BaseDAO();
            dao.save(app);
        }
        catch (Exception e)
        {
            throw new E5Exception("create app error.", e);
        }
	}
	
	/**
	 * 创建子系统对象
	 * 
	 * @param app    子系统对象
	 * @param session hibernate session 对象
	 * @throws E5Exception
	 */
	public void create(App app, Session session) throws E5Exception{
	    int appID = 1;
	    try
        {
		    appID = (int)EUID.getID("AppID");
		    app.setAppID(appID);
		    session.save(app);
        }
        catch (Exception e)
        {
            throw new E5Exception("create app error.", e);
        }
	}

	/**
	 * 判断子系统对象是否存在
	 * 
	 * @param app   子系统对象
	 */
	public boolean exist(App app) throws E5Exception{
		App app1 = get(app.getAbsVersion(), app.getName());
		if(app1 == null)
		    return false;
		else
		    return true;
	}
	
	/**
	 * 判断子系统对象是否存在
	 * 
	 * @param app   子系统对象
	 * @param session hibernate session对象
	 */
	public boolean exist(App app, Session session) throws E5Exception{
		App app1 = get(app.getAbsVersion(), app.getName(), session);
		if(app1 == null)
		    return false;
		else
		    return true;
	}

	
	/**
	 * 得到所有的子系统对象
	 * 
	 */
	public App[] get() throws E5Exception{
	    App[] apps = null;
        List list = DAOHelper.find("from App as app");
        if(list.size() > 0)
        {
            apps = new App[list.size()];
            list.toArray(apps);
        }
        return apps;
	}

	/**
	 * 根据子系统ID得到子系统对象
	 * 
	 * @param appID   子系统ID
	 */
	public App get(int appID) throws E5Exception{
        List list = DAOHelper.find("from App as app where app.AppID = :appID", 
        		new Integer(appID), Hibernate.INTEGER);
        if(list.size() == 1)
            return (App)list.get(0);
        else
            return null;
	}

	/**
	 * 根据子系统版本号和子系统名得到子系统
	 * 
	 * @param appVersion 子系统版本
	 * @param appName  子系统名
	 */
	public App get(String appVersion, String appName) throws E5Exception{
        List list = DAOHelper.find("from App as app where app.Version = :appVersion and app.Name = :appName ",
        		new String[]{"appVersion", "appName"}, 
        		new Object[] {appVersion, appName}, 
        		new Type[] {Hibernate.STRING, Hibernate.STRING});
        if(list.size() == 1)
            return (App)list.get(0);
        else
            return null;
	}
	
	/**
	 * 根据子系统版本号和子系统名得到子系统
	 * 
	 * @param appVersion 子系统版本
	 * @param appName    子系统名
	 * @param session hibernate session 对象
	 */
	private App get(String appVersion, String appName, Session session) throws E5Exception{
        List list = DAOHelper.find("from App as app where app.Version = :appVersion and app.Name = :appName ", 
        		new String[]{"appVersion", "appName"}, 
        		new Object[] {appVersion, appName}, 
        		new Type[] {Hibernate.STRING, Hibernate.STRING}, 
        		session);
        if(list.size() == 1)
            return (App)list.get(0);
        else
            return null;
	}

	/**
	 * 更新子系统信息
	 * 
	 * @param app    子系统对象
	 */
	public void update(App app) throws E5Exception{
	    BaseDAO dao = new BaseDAO();
	    try
        {
            dao.update(app);
        }
        catch (Exception e)
        {
            throw new E5Exception(e);
        }
	}
	
	/**
	 * 更新子系统基本信息
	 * 
	 * @param app   子系统对象
	 * @param session hibernate session 对象
	 */
	public void update(App app, Session session) throws E5Exception{
	    try
        {
            session.update(app);
        }
        catch (Exception e)
        {
            throw new E5Exception(e);
        }
	}

	/**
	 * 删除子系统基本信息
	 * 
	 * @param appID    子系统ID
	 */
	public void delete(int appID) throws E5Exception{
		DAOHelper.delete("delete from App as app where app.AppID = :appID", 
				new Integer(appID), 
				Hibernate.INTEGER);
	}
	
	/**
	 * 删除子系统基本信息
	 * 
	 * @param appID    子系统ID
	 * @param session hibernate session 对象
	 */
	public void delete(int appID, Session session) throws E5Exception{
		DAOHelper.delete("delete from App as app where app.AppID = :appID",
				new Integer(appID), Hibernate.INTEGER, session);
	}
}