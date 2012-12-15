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
 * @created 11-����-2005 12:45:11
 */
class AppManagerImpl implements AppManager {

	public AppManagerImpl(){

	}

	/**
	 * ������ϵͳ����
	 * 
	 * @param app  ��ϵͳ����
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
	 * ������ϵͳ����
	 * 
	 * @param app    ��ϵͳ����
	 * @param session hibernate session ����
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
	 * �ж���ϵͳ�����Ƿ����
	 * 
	 * @param app   ��ϵͳ����
	 */
	public boolean exist(App app) throws E5Exception{
		App app1 = get(app.getAbsVersion(), app.getName());
		if(app1 == null)
		    return false;
		else
		    return true;
	}
	
	/**
	 * �ж���ϵͳ�����Ƿ����
	 * 
	 * @param app   ��ϵͳ����
	 * @param session hibernate session����
	 */
	public boolean exist(App app, Session session) throws E5Exception{
		App app1 = get(app.getAbsVersion(), app.getName(), session);
		if(app1 == null)
		    return false;
		else
		    return true;
	}

	
	/**
	 * �õ����е���ϵͳ����
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
	 * ������ϵͳID�õ���ϵͳ����
	 * 
	 * @param appID   ��ϵͳID
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
	 * ������ϵͳ�汾�ź���ϵͳ���õ���ϵͳ
	 * 
	 * @param appVersion ��ϵͳ�汾
	 * @param appName  ��ϵͳ��
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
	 * ������ϵͳ�汾�ź���ϵͳ���õ���ϵͳ
	 * 
	 * @param appVersion ��ϵͳ�汾
	 * @param appName    ��ϵͳ��
	 * @param session hibernate session ����
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
	 * ������ϵͳ��Ϣ
	 * 
	 * @param app    ��ϵͳ����
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
	 * ������ϵͳ������Ϣ
	 * 
	 * @param app   ��ϵͳ����
	 * @param session hibernate session ����
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
	 * ɾ����ϵͳ������Ϣ
	 * 
	 * @param appID    ��ϵͳID
	 */
	public void delete(int appID) throws E5Exception{
		DAOHelper.delete("delete from App as app where app.AppID = :appID", 
				new Integer(appID), 
				Hibernate.INTEGER);
	}
	
	/**
	 * ɾ����ϵͳ������Ϣ
	 * 
	 * @param appID    ��ϵͳID
	 * @param session hibernate session ����
	 */
	public void delete(int appID, Session session) throws E5Exception{
		DAOHelper.delete("delete from App as app where app.AppID = :appID",
				new Integer(appID), Hibernate.INTEGER, session);
	}
}