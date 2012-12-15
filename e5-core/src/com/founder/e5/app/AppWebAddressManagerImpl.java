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
 * @created 11-七月-2005 12:45:39
 */
class AppWebAddressManagerImpl implements AppWebAddressManager {

	public AppWebAddressManagerImpl(){

	}

	
	/**
	 * 创建子系统web页面信息
	 * 
	 * @param appWebAddress 子系统web页面对象
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
	 * 创建子系统web页面信息
	 * 
	 * @param appWebAddress    子系统web页面对象
	 * @param session hibernate session 对象
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
	 * 得到所有的子系统web页面信息
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
	 * 根据子系统ID得到子系统web页面信息
	 * 
	 * @param appID    子系统ID
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
	 * 根据子系统ID和web页面名得到子系统web页面信息
	 * 
	 * @param appID  子系统ID
	 * @param webName    web页面名
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
	 * 更新子系统web页面地址信息
	 * 
	 * @param appWebAddress    子系统web页面对象
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
	 * 删除子系统web页面地址信息
	 * 
	 * @param appID    子系统ID
	 */
	public void delete(int appID) throws E5Exception{
	    DAOHelper.delete("delete from AppWebAddress as appWeb where appWeb.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER);
	}
	
	/**
	 * 删除子系统管理页面对象
	 * @param appID    子系统ID
	 * @param session hibernate session 对象
	 */
	public void delete(int appID, Session session) throws E5Exception{
	    DAOHelper.delete("delete from AppWebAddress as appWeb where appWeb.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER, session);
	}
}