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
 * @created 11-����-2005 12:44:24
 */
class SysConfigManagerImpl implements SysConfigManager {

	public SysConfigManagerImpl(){

	}

	/**
	 * ����ϵͳ����
	 * 
	 * @param sysConfig    ϵͳ���ö���
	 */
//�������޸ģ���Ҫ��ȡ���µ�sysConfigID�Ժ󣬲��ܴ����ɹ�
//2006-03-03	
	public void create(SysConfig sysConfig) throws E5Exception{
	    try
        {
		    BaseDAO dao = new BaseDAO();
		    String value = get(sysConfig.getAppID(), sysConfig.getProject(), sysConfig.getItem());
		    if(value != null)
		        throw new E5Exception("SysConfig has exist.");
//��������������		    
            int sysConfigID = (int)EUID.getID("SysConfigID");
            sysConfig.setSysConfigID(sysConfigID);
//����
            dao.save(sysConfig);
        }
        catch (Exception e)
        {
            throw new E5Exception("create sys config error.", e);
        }
	}
	
	/**
	 * ����ϵͳ����
	 * 
	 * @param sysConfig    ϵͳ���ö���
	 * @param session hibernate session ����
	 */
	public void create(SysConfig sysConfig, Session session) throws E5Exception{
	    try
        {
		    String value = get(sysConfig.getAppID(), sysConfig.getProject(), sysConfig.getItem());
		    if(value != null)
		        throw new E5Exception("SysConfig has exist.");
//		  ��������������		    
            int sysConfigID = (int)EUID.getID("SysConfigID");
            sysConfig.setSysConfigID(sysConfigID);
//����
		    session.save(sysConfig);
        }
        catch (Exception e)
        {
            throw new E5Exception("create sys config error.", e);
        }
	}

	/**
	 * ������ϵͳID����Ŀ����Ŀ�õ���Ӧ��ϵͳ����ֵ
	 * 
	 * @param appID ��ϵͳID
	 * @param project ��Ŀ
	 * @param item  ��Ŀ
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
	 * �õ����е�ϵͳ������
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
	 * ����ϵͳ����
	 * 
	 * @param sysConfig  ϵͳ���ö���
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
	 * ɾ��ϵͳ����
	 * 
	 * @param sysConfigID ϵͳ����ID
	 */
	public void delete(int sysConfigID) throws E5Exception{
	    DAOHelper.delete("delete from com.founder.e5.sys.SysConfig as config where config.SysConfigID = :sysConfig", 
	    		new Integer(sysConfigID), Hibernate.INTEGER);
	}

	/**
	 * ����ϵͳ����ID�õ�ϵͳ����
	 * 
	 * @param sysConfigID    ϵͳ����ID
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
	 * ɾ����ϵͳ���й�����
	 * 
	 * @param appID    ��ϵͳID
	 */
	public void deleteAppSysConfig(int appID) throws E5Exception{
	    DAOHelper.delete("delete from com.founder.e5.sys.SysConfig as config where config.AppID = :appID", 
	    		new Integer(appID), Hibernate.INTEGER);
	}

	/**
	 * �õ���ϵͳ������ϵͳ����
	 * 
	 * @param appID ��ϵͳID
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
	 * �����ϵͳ������
	 * @param appID    ��ϵͳID
	 * @param session hibernate session ����
	 */
	public void deleteAppSysConfig(int appID, Session session) throws E5Exception
	{
    	DAOHelper.delete("delete from com.founder.e5.sys.SysConfig as config where config.AppID = :appID",
		    	new Integer(appID), Hibernate.INTEGER, session);
	}
}