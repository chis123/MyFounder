package com.founder.e5.sys;

import java.util.List;

import org.hibernate.type.Type;
import org.hibernate.Hibernate;

import com.founder.e5.commons.DateUtils;
import com.founder.e5.commons.ResourceMgr;
import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.Context;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.db.DBSession;

/**
 * @version 1.0
 * @created 11-七月-2005 12:44:34
 */
class LoginUserManagerImpl implements LoginUserManager 
{
	public LoginUserManagerImpl()
	{
	}
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#add(com.founder.e5.sys.LoginUser)
	 */
	public void add(LoginUser login) throws E5Exception
	{
	    String loginTime = DateUtils.format();

	    login.setAppID(0);
	    login.setPortalID(0);
	    login.setLoginTime(loginTime);
	    login.setLastAccessTime(loginTime);	    
	    try
        {
		    BaseDAO dao = new BaseDAO();
            dao.save(login);
        }
        catch (Exception e)
        {
            throw new E5Exception("add LoginUser failed!", e);
        }
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#access(int)
	 */
	public int access(int loginid) throws E5Exception
	{
		int nRows = 0;
		String sql = "update FSYS_LOGINUSER set LASTACCESSTIME = ? where LOGINID = ?";
	    String loginTime = DateUtils.format();

		DBSession session = Context.getDBSession();
		try
		{
			nRows = session.executeUpdate(sql,new Object[]{loginTime, new Integer(loginid)});
		}
		catch(Exception e)
		{
			throw new E5Exception("refresh lastaccesstime has error!",e);
		}
		finally
		{
			ResourceMgr.closeQuietly(session);
		}
		return nRows;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#remove(com.founder.e5.sys.LoginUser, boolean)
	 */
	public void remove(int loginid,boolean normal) throws E5Exception
	{
		//(1)首先归档
		StringBuffer sql = new StringBuffer(10);
		sql.append("insert into FSYS_LOGINARCHIVE(LOGINID,NUSERID,STRUSERCODE,NROLEID,STRHOSTNAME,SERVERNAME,LOGINTIME,LASTACCESSTIME,PORTALID,APPID,NORMALEXIT) ")
		.append("select LOGINID,NUSERID,STRUSERCODE,NROLEID,STRHOSTNAME,SERVERNAME,LOGINTIME,'");
	    String loginTime = DateUtils.format();
		sql.append(loginTime).append("',PORTALID,APPID,");
		if (normal)
			sql.append("0");
		else
			sql.append("1");
		sql.append(" from FSYS_LOGINUSER where LOGINID = ?");

		DBSession session = Context.getDBSession();
		try
		{
			session.executeUpdate(sql.toString(),new Object[]{new Integer(loginid)});
		}
		catch(Exception e)
		{
			//2007-10-17 Gong Lijie 容忍归档过程中失败，以便继续登录
			System.out.println("Login sql failed[" + sql.toString() + "]!" 
					+ e.getLocalizedMessage());
		}
		finally
		{
			ResourceMgr.closeQuietly(session);
		}
		
		//(2)然后删除
        DAOHelper.delete("delete from LoginUser where loginID = :id", 
        		new Integer(loginid), 
        		Hibernate.INTEGER);		
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#get()
	 */
	public LoginUser[] get() throws E5Exception
	{
	    LoginUser[] loginUsers = null;
        List list = DAOHelper.find("from com.founder.e5.sys.LoginUser as loginUser order by loginUser.loginID");
        if(list.size() > 0)
        {
            loginUsers = new LoginUser[list.size()];
            list.toArray(loginUsers);
        }
        return loginUsers;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#getBySort(java.lang.String)
	 */
	public LoginUser[] getBySort(String sortField) throws E5Exception
	{
	    LoginUser[] loginUsers = null;
	    String hsql="from com.founder.e5.sys.LoginUser as loginUser Order By loginUser."+sortField;
        List list = DAOHelper.find(hsql);
        if(list.size() > 0)
        {
            loginUsers = new LoginUser[list.size()];
            list.toArray(loginUsers);
        }
        return loginUsers;
	}

	public LoginUser[] getBySort(String sortField,String sortBy) throws E5Exception
	{
	    LoginUser[] loginUsers = null;
	    String hsql="from com.founder.e5.sys.LoginUser as loginUser Order By loginUser."+sortField+" "+sortBy;
        List list = DAOHelper.find(hsql);
        if(list.size() > 0)
        {
            loginUsers = new LoginUser[list.size()];
            list.toArray(loginUsers);
        }
        return loginUsers;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#get(java.lang.String, int, java.lang.String)
	 */
	public LoginUser get(String userCode, int roleID, String hostName) throws E5Exception
	{
        List list = DAOHelper.find("from com.founder.e5.sys.LoginUser as loginUser where loginUser.userCode=:userCode and loginUser.roleID=:roleID and loginUser.hostName = :hostName",
        		new String[]{"userCode", "roleID", "hostName"},
        		new Object[] {userCode, new Integer(roleID), hostName}, 
        		new Type[] {Hibernate.STRING, Hibernate.INTEGER, Hibernate.STRING});
        if(list.size() > 0)
            return (LoginUser)list.get(0);
        else
            return null;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#get(java.lang.String, int)
	 */
	public LoginUser get(String userCode, int roleID) throws E5Exception
	{
        List list = DAOHelper.find("from com.founder.e5.sys.LoginUser as loginUser where loginUser.userCode=:userCode and loginUser.roleID=:roleID",
        		new String[]{"userCode", "roleID"},
        		new Object[] {userCode, new Integer(roleID)}, 
        		new Type[] {Hibernate.STRING, Hibernate.INTEGER});
        if(list.size() > 0)
            return (LoginUser)list.get(0);
        else
            return null;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#get(java.lang.String)
	 */
	public LoginUser[] get(String userCode) throws E5Exception
	{
	    LoginUser[] loginUsers = null;
        List list = DAOHelper.find("from com.founder.e5.sys.LoginUser as loginUser where loginUser.userCode = :userCode order by loginUser.loginID", 
        		userCode, Hibernate.STRING);
        if(list.size() > 0)
        {
            loginUsers = new LoginUser[list.size()];
            list.toArray(loginUsers);
        }
        return loginUsers;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#get(int)
	 */
	public LoginUser[] get(int userID) throws E5Exception
	{
        List list = DAOHelper.find("from com.founder.e5.sys.LoginUser as loginUser where loginUser.userID=:userID order by loginUser.loginID", 
        		new Integer(userID), Hibernate.INTEGER);
	    LoginUser[] loginUsers = null;
        if(list.size() > 0)
        {
            loginUsers = new LoginUser[list.size()];
            list.toArray(loginUsers);
        }
        return loginUsers;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#getById(int)
	 */
	public LoginUser getById(int loginid) throws E5Exception
	{
        List list = DAOHelper.find("from com.founder.e5.sys.LoginUser as loginUser where loginUser.loginid = :ID", 
        		new Integer(loginid), Hibernate.INTEGER);
        
	    LoginUser loginUser = null;
        if(list.size() > 0)
        	loginUser = (LoginUser)list.get(0);
        list.clear();
        list = null;
        return loginUser;
	}
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sys.LoginUserManager#remove(java.lang.String)
	 */
	public void remove(String date) throws E5Exception 
	{
		//(1)首先归档
		StringBuffer sql = new StringBuffer(10);
		sql.append("insert into FSYS_LOGINARCHIVE(LOGINID,NUSERID,STRUSERCODE,NROLEID,STRHOSTNAME,SERVERNAME,LOGINTIME,LASTACCESSTIME,PORTALID,APPID,NORMALEXIT) ")
		.append("select LOGINID,NUSERID,STRUSERCODE,NROLEID,STRHOSTNAME,SERVERNAME,LOGINTIME,'");
	    String loginTime = DateUtils.format();
		sql.append(loginTime).append("',PORTALID,APPID,").append("1");
		sql.append(" from FSYS_LOGINUSER where LASTACCESSTIME < ?");

		DBSession session = Context.getDBSession();
		try
		{
			session.executeUpdate(sql.toString(),new Object[]{date});
		}
		catch(Exception e)
		{
			//2007-10-17 Gong Lijie 容忍归档过程中的异常
			System.out.println("Login sql failed[" + sql.toString() + "]!" 
					+ e.getLocalizedMessage());
		}
		finally
		{
			ResourceMgr.closeQuietly(session);
		}
		
		//(2)然后删除
        DAOHelper.delete("delete from LoginUser where lastAccessTime < :lastaccesstime", 
        		date, 
        		Hibernate.STRING);		
	}
}