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
 * @created 11-七月-2005 12:45:21
 */
class AppPermissionManagerImpl implements AppPermissionManager {

	public AppPermissionManagerImpl(){

	}

	
	/**
	 * 创建子系统权限页面信息
	 * @param appPermission    子系统权限页面信息
	 * 
	 */
	public void create(AppPermission appPermission) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    AppPermission appPerm = get(appPermission.getAppID(), appPermission.getResourceType());
		    if(appPerm != null)
		        throw new E5Exception("AppPermission has exist.");
            dao.save(appPermission);
        }
        catch (Exception e)
        {
            throw new E5Exception("create app permission error.", e);
        }
	}
	
	/**
	 * 创建子系统权限页面信息
	 * @param appPermission    子系统权限页面信息
	 * 
	 */
	public void create(AppPermission appPermission, Session session) throws E5Exception{
	    try
        {
		    AppPermission appPerm = get(appPermission.getAppID(), appPermission.getResourceType(), session);
		    if(appPerm != null)
		        throw new E5Exception("AppPermission has exist.");
            session.save(appPermission);
        }
        catch (Exception e)
        {
            throw new E5Exception("create app error.", e);
        }
	}

	
	/**
	 * 获得所有的子系统权限页面信息
	 */
	public AppPermission[] get() throws E5Exception{
	    AppPermission[] appPerms = null;

        List list = DAOHelper.find("from AppPermission as appPerm");
        if(list.size() > 0)
        {
            appPerms = new AppPermission[list.size()];
            list.toArray(appPerms);
        }
        return appPerms;
	}

	/**
	 * 获得指定子系统的所有权限页面信息
	 * @param appID    子系统ID
	 * 
	 */
	public AppPermission[] get(int appID) throws E5Exception{
	    AppPermission[] appPerms = null;
        List list = DAOHelper.find("from AppPermission as appPerm where appPerm.AppID = :appID", 
        		new Integer(appID), Hibernate.INTEGER);
        if(list.size() > 0)
        {
            appPerms = new AppPermission[list.size()];
            list.toArray(appPerms);
        }
        return appPerms;
	}

	/**
	 * 根据资源类型得到子系统权限页面信息
	 * 
	 * @param nResourceType    资源类型
	 */
	public AppPermission[] get(String resourceType) throws E5Exception{
	    AppPermission[] appPerms = null;
        List list = DAOHelper.find("from AppPermission as appPerm where appPerm.ResourceType = :rt", 
        		resourceType, Hibernate.STRING);
        if(list.size() > 0)
        {
            appPerms = new AppPermission[list.size()];
            list.toArray(appPerms);
        }
        return appPerms;
	}

	/**
	 * 获得指定子系统，指定资源类型的权限页面信息
	 * @param appID    子系统ID
	 * @param nResourceType    资源类型
	 * 
	 */
	public AppPermission get(int appID, String resourceType) throws E5Exception{
        List list = DAOHelper.find("from AppPermission as appPerm where appPerm.AppID = :id and appPerm.ResourceType = :rt",
        		new String[]{"id", "rt"}, 
        		new Object[] {new Integer(appID), resourceType}, 
        		new Type[] {Hibernate.INTEGER, Hibernate.STRING});
        if(list.size() == 1)
            return (AppPermission)list.get(0);
        else
            return null;
	}
	
	
	/**
	 * 获得指定子系统，指定资源类型的权限页面信息
	 * @param appID    子系统ID
	 * @param nResourceType    资源类型
	 * 
	 */
	private AppPermission get(int appID, String resourceType, Session session) throws E5Exception{
        List list = DAOHelper.find("from AppPermission as appPerm where appPerm.AppID = :id and appPerm.ResourceType = :rt", 
        		new String[]{"id", "rt"}, 
        		new Object[] {new Integer(appID), resourceType}, 
        		new Type[] {Hibernate.INTEGER, Hibernate.STRING});
        if(list.size() == 1)
            return (AppPermission)list.get(0);
        else
            return null;
	}

	/**
	 * 更新子系统权限对象
	 * 
	 * @param appPermission    子系统权限对象
	 */
	public void update(AppPermission appPermission) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    AppPermission appPerm = get(appPermission.getAppID(), appPermission.getResourceType());
		    if(appPerm == null)
		        throw new E5Exception("AppPermission not exist.");
            dao.update(appPermission);
        }
        catch (Exception e)
        {
            throw new E5Exception("create app error.", e);
        }
	}

	/**
	 * 删除子系统权限页面信息
	 * 
	 * @param appID    子系统ID
	 */
	public void delete(int appID) throws E5Exception{
	    DAOHelper.delete("delete from AppPermission as appPerm where appPerm.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER);
	}
	
	/**
	 * 删除权限页面信息
	 * @param appID    子系统ID
	 * @param session hibernate session 对象
	 */
	public void delete(int appID, Session session) throws E5Exception{
	    DAOHelper.delete("delete from AppPermission as appPerm where appPerm.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER, session);
	}
}