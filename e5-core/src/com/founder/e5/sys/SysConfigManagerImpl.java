package com.founder.e5.sys;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Hibernate;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;

/**
 * @version 1.0
 * @created 11-七月-2005 12:44:24
 */
class SysConfigManagerImpl implements SysConfigManager {

	public SysConfigManagerImpl(){

	}

	/**
	 * 创建系统配置
	 * 
	 * @param sysConfig    系统配置对象
	 */
//王朝阳修改，需要获取最新的sysConfigID以后，才能创建成功
//2006-03-03	
	public void create(SysConfig sysConfig) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    String value = get(sysConfig.getAppID(), sysConfig.getProject(), sysConfig.getItem());
		    if(value != null)
		        throw new E5Exception("SysConfig has exist.");
//以下王朝阳增加		    
            int sysConfigID = (int)EUID.getID("SysConfigID");
            sysConfig.setSysConfigID(sysConfigID);
//以上
            dao.save(sysConfig);
        }
        catch (Exception e)
        {
            throw new E5Exception("create sys config error.", e);
        }
	}
	
	/**
	 * 创建系统配置
	 * 
	 * @param sysConfig    系统配置对象
	 * @param session hibernate session 对象
	 */
	public void create(SysConfig sysConfig, Session session) throws E5Exception{
	    try
        {
		    String value = get(sysConfig.getAppID(), sysConfig.getProject(), sysConfig.getItem());
		    if(value != null)
		        throw new E5Exception("SysConfig has exist.");
//		  以下王朝阳增加		    
            int sysConfigID = (int)EUID.getID("SysConfigID");
            sysConfig.setSysConfigID(sysConfigID);
//以上
		    session.save(sysConfig);
        }
        catch (Exception e)
        {
            throw new E5Exception("create sys config error.", e);
        }
	}

	/**
	 * 根据子系统ID，项目，条目得到相应的系统配置值
	 * 
	 * @param appID 子系统ID
	 * @param project 项目
	 * @param item  条目
	 */
	public String get(int appID, String project, String item) throws E5Exception{
        String sql = "select config.Value from com.founder.e5.sys.SysConfig as config where config.AppID=? and config.Project=? and config.Item=?";
	    try
        {
            BaseDAO dao = new BaseDAO();
            List list = dao.find(sql, new Object[]{new Integer(appID), project, item});
            if(list.size() == 1)
                return (String)list.get(0);
            else
                return null;
        }
        catch (Exception e)
        {
            throw new E5Exception("get sys config error.", e);
        }
	}

	/**
	 * 得到所有的系统配置项
	 */
	public SysConfig[] get() throws E5Exception{
	    SysConfig[] configs = null;
        List list = DAOHelper.find("from com.founder.e5.sys.SysConfig as config order by config.SysConfigID");
        if(list.size() > 0)
        {
            configs = new SysConfig[list.size()];
            list.toArray(configs);
        }
        return configs;
	}

	/**
	 * 更新系统配置
	 * 
	 * @param sysConfig  系统配置对象
	 */
	public void update(SysConfig sysConfig) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    String value = get(sysConfig.getAppID(), sysConfig.getProject(), sysConfig.getItem());
		    if(value == null)
		        throw new E5Exception("AppPermission not exist.");
            dao.update(sysConfig);
        }
        catch (Exception e)
        {
            throw new E5Exception("update sys config error.", e);
        }
	}

	/**
	 * 删除系统配置
	 * 
	 * @param sysConfigID 系统配置ID
	 */
	public void delete(int sysConfigID) throws E5Exception{
	    DAOHelper.delete("delete from com.founder.e5.sys.SysConfig as config where config.SysConfigID = :sysConfig", 
	    		new Integer(sysConfigID), Hibernate.INTEGER);
	}

	/**
	 * 根据系统配置ID得到系统配置
	 * 
	 * @param sysConfigID    系统配置ID
	 */
	public SysConfig get(int sysConfigID) throws E5Exception{
        List list = DAOHelper.find("select config from com.founder.e5.sys.SysConfig as config where config.SysConfigID = :sysConfig", 
        		new Integer(sysConfigID), Hibernate.INTEGER);
        if(list.size() == 1)
            return (SysConfig)list.get(0);
        else
            return null;
	}

	/**
	 * 删除子系统的有关配置
	 * 
	 * @param appID    子系统ID
	 */
	public void deleteAppSysConfig(int appID) throws E5Exception{
	    DAOHelper.delete("delete from com.founder.e5.sys.SysConfig as config where config.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER);
	}

	/**
	 * 得到子系统的所有系统配置
	 * 
	 * @param appID 子系统ID
	 */
	public SysConfig[] getAppSysConfigs(int appID) throws E5Exception{
	    SysConfig[] configs = null;
        List list = DAOHelper.find("select config from com.founder.e5.sys.SysConfig as config where config.AppID=:appID order by config.SysConfigID", 
        		new Integer(appID), Hibernate.INTEGER);
        if(list.size() > 0)
        {
            configs = new SysConfig[list.size()];
            list.toArray(configs);
        }
        return configs;
	}
	
	/**
	 * 获得子系统的配置
	 * @param appID    子系统ID
	 * @param session hibernate session 对象
	 */
	public void deleteAppSysConfig(int appID, Session session) throws E5Exception
	{
    	DAOHelper.delete("delete from com.founder.e5.sys.SysConfig as config where config.AppID = :appID",
		    	new Integer(appID), Hibernate.INTEGER, session);
	}
}