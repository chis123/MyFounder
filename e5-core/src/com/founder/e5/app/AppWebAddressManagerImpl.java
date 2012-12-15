package com.founder.e5.app;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-����-2005 12:45:39
 */
class AppWebAddressManagerImpl implements AppWebAddressManager {

	public AppWebAddressManagerImpl(){

	}

	
	/**
	 * ������ϵͳwebҳ����Ϣ
	 * 
	 * @param appWebAddress ��ϵͳwebҳ�����
	 */
	public void create(AppWebAddress appWebAddress) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    AppWebAddress appWeb = get(appWebAddress.getAppID(), appWebAddress.getWebName());
		    if(appWeb != null)
		        throw new E5Exception("AppPermission has exist.");
            dao.save(appWebAddress);
        }
        catch (Exception e)
        {
            throw new E5Exception("create app web address error.", e);
        }
	}
	
	/**
	 * ������ϵͳwebҳ����Ϣ
	 * 
	 * @param appWebAddress    ��ϵͳwebҳ�����
	 * @param session hibernate session ����
	 */
	public void create(AppWebAddress appWebAddress, Session session) throws E5Exception{
	    try
        {
		    AppWebAddress appWeb = get(appWebAddress.getAppID(), appWebAddress.getWebName());
		    if(appWeb != null)
		        throw new E5Exception("AppPermission has exist.");
            session.save(appWebAddress);
        }
        catch (Exception e)
        {
            throw new E5Exception("create app error.", e);
        }
	}

	/**
	 * �õ����е���ϵͳwebҳ����Ϣ
	 */
	public AppWebAddress[] get() throws E5Exception{
	    AppWebAddress[] appWebs = null;
        List list = DAOHelper.find("from AppWebAddress as appWeb");
        if(list.size() > 0)
        {
            appWebs = new AppWebAddress[list.size()];
            list.toArray(appWebs);
        }
        return appWebs;
	}

	/**
	 * ������ϵͳID�õ���ϵͳwebҳ����Ϣ
	 * 
	 * @param appID    ��ϵͳID
	 */
	public AppWebAddress[] get(int appID) throws E5Exception{
	    AppWebAddress[] appWebs = null;
        List list = DAOHelper.find("from AppWebAddress as appWeb where appWeb.AppID = :appID", 
        		new Integer(appID), Hibernate.INTEGER);
        if(list.size() > 0)
        {
            appWebs = new AppWebAddress[list.size()];
            list.toArray(appWebs);
        }
        return appWebs;
	}

	/**
	 * ������ϵͳID��webҳ�����õ���ϵͳwebҳ����Ϣ
	 * 
	 * @param appID  ��ϵͳID
	 * @param webName    webҳ����
	 */
	public AppWebAddress get(int appID, String webName) throws E5Exception{
        List list = DAOHelper.find("from AppWebAddress as appWeb where appWeb.AppID = :appID and appWeb.WebName = :webName ",
        		new String[]{"appID", "webName"}, 
        		new Object[] {new Integer(appID), webName}, 
        		new Type[] {Hibernate.INTEGER, Hibernate.STRING});
        if(list.size() == 1)
        {
            return (AppWebAddress)list.get(0);
        }
        else
            return null;
	}

	/**
	 * ������ϵͳwebҳ���ַ��Ϣ
	 * 
	 * @param appWebAddress    ��ϵͳwebҳ�����
	 */
	public void update(AppWebAddress appWebAddress) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    AppWebAddress appWeb = get(appWebAddress.getAppID(), appWebAddress.getWebName());
		    if(appWeb == null)
		        throw new E5Exception("AppPermission not exist.");
            dao.update(appWebAddress);
        }
        catch (Exception e)
        {
            throw new E5Exception("update app web address error.", e);
        }
	}

	/**
	 * ɾ����ϵͳwebҳ���ַ��Ϣ
	 * 
	 * @param appID    ��ϵͳID
	 */
	public void delete(int appID) throws E5Exception{
	    DAOHelper.delete("delete from AppWebAddress as appWeb where appWeb.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER);
	}
	
	/**
	 * ɾ����ϵͳ����ҳ�����
	 * @param appID    ��ϵͳID
	 * @param session hibernate session ����
	 */
	public void delete(int appID, Session session) throws E5Exception{
	    DAOHelper.delete("delete from AppWebAddress as appWeb where appWeb.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER, session);
	}
}