package com.founder.e5.sso;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.founder.e5.commons.DateUtils;
import com.founder.e5.context.E5Exception;
import com.founder.e5.context.EUID;
import com.founder.e5.sys.LoginUser;
import com.founder.e5.sys.LoginUserManager;
import com.founder.e5.sys.org.Role;
import com.founder.e5.sys.org.RoleReader;
import com.founder.e5.sys.org.User;
import com.founder.e5.sys.org.UserReader;
import com.founder.e5.sys.org.UserRole;
/**
 * E5系统缺省实现的SSO部分
 * @author LuZuowei
 */
public class SSOImpl implements SSO 
{
	private LoginUserManager loginManager;
	private UserReader userReader = null; 
	private RoleReader roleReader = null;
	public	static int	refreshTime = 15; 	//自动刷新的时间
	
	public SSOImpl() 
	{
		super();
	}
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sso.SSO#verifyUserPassword(java.lang.String, java.lang.String)
	 */
	public int verifyUserPassword(String usercode, String password) throws E5Exception 
	{
		int nRet = 0;
		User user = userReader.getUserByCode(usercode);
		if (user != null)
		{
			String pass = user.getUserPassword();
			if (pass == null && password == null)
				nRet = 0;
			else if (pass == null)
			{
				if ("".equals(password.trim()))
					nRet = 0;
				else
					nRet = -2; //密码不正确
			}
			else if (password == null)
			{
				if ("".equals(pass.trim()))
					nRet = 0;
				else
					nRet = -2; //密码不正确
			}
			else
			{
				if (pass.equals(password))
					nRet = 0;
				else
					nRet = -2;	//密码不正确！
			}
		}
		else
			nRet = -1; //用户不存在!
		return nRet;
	}
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sso.SSO#getValidRole(java.lang.String)
	 */
	public Role[] getValidRole(String usercode) throws E5Exception 
	{
		Role[] validroles = null;
		User user = userReader.getUserByCode(usercode);
		Role[] roles = roleReader.getRolesByUser(user.getUserID());
		if (roles != null)
		{
			List list = Collections.synchronizedList(new ArrayList(5)); 
			for (int i = 0; i < roles.length; i++)
			{
				if (isValid(user.getUserID(),roles[i]))
					list.add(roles[i]);
			}
			if (list.size() > 0)
			{
				validroles = new Role[list.size()];
				list.toArray(validroles);
			}
			list.clear();
			list = null;
		}
		return validroles;
	}
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sso.SSO#login(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public String[] login(String usercode, int roleID, String hostname,
			String servername,boolean concurrent) throws E5Exception 
	{
		String[] ret = new String[2]; 
		int id = 0;
		if (concurrent)
		{
			id = loginNew(usercode,roleID,hostname,servername);
			ret[0] = String.valueOf(id);
			ret[1] = "";
		}
		else
		{
			LoginUser loginuser = loginManager.get(usercode,roleID);
			if (loginuser != null) 	//已经登录
			{
				Calendar cur = Calendar.getInstance();
				cur.add(Calendar.MINUTE,0 - SSOImpl.refreshTime);
				String curstr = DateUtils.format(cur);
				if (curstr.compareToIgnoreCase(loginuser.getLastAccessTime()) < 0) //15分钟前登录过
				{
					if (loginuser.getHostName().equals(hostname)) //同一机器登录
					{
						//ret[0] = "-1";
						//ret[1] = "samemachine";
						//直接登录一个全新的ID进入
						id = loginNew(usercode,roleID,hostname,servername);
						ret[0] = String.valueOf(id);
						ret[1] = "";
					}
					else 		//不同机器在登录
					{
						ret[0] = "-1";
						ret[1] = loginuser.getHostName();
					}
				}
				else	//15分钟前没有登录，是更早以前的登录，可能没有正常退出
				{
					//(1) 首先归档记录
					loginManager.remove(loginuser.getLoginID(),false);
					//(2) 登录一个新的进入
					id = loginNew(usercode,roleID,hostname,servername);
					ret[0] = String.valueOf(id);
					ret[1] = "";					
				}				
			}
			else
			{
				id = loginNew(usercode,roleID,hostname,servername);
				ret[0] = String.valueOf(id);
				ret[1] = "";
			}
		}
		return ret;
	}
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sso.SSO#accessLast(int,int)
	 */
	public int accessLast(int loginid,int userid,int roleid) throws E5Exception
	{
		int ret = 0;
		Role role = roleReader.get(roleid);
		if (role != null)
		{
			if (isValid(userid,role))
			{
				if (loginManager.access(loginid) > 0)
					ret = 0;
				else
					ret = -2;
			}
			else
				ret = -1;
		}
		return ret;
	}
	/*
	 *  (non-Javadoc)
	 * @see com.founder.e5.sso.SSO#logout(int)
	 */
	public void logout(int loginid)  throws E5Exception
	{
		loginManager.remove(loginid,true);
	}
	
	/**
	 * 判断是否在当前有效时间范围内
	 * @param role
	 * @return
	 */
	private boolean isValid(int userID,Role role) throws E5Exception
	{
		boolean valid = false;
		if (role != null)
		{
			UserRole ur = roleReader.getUserRole(userID,role.getRoleID());
			if (ur.isValid(new Date()))
				valid = true;
			else System.out.println("[no valid role]UserRole:" + ur);
		}
		return valid;
	}
	
	/**
	 * 真正完成一次登录
	 * @param usercode
	 * @param roleID
	 * @param hostname
	 * @param servername
	 * @return
	 * @throws E5Exception
	 */
	private int loginNew(String usercode, int roleID, String hostname,
			String servername) throws E5Exception
	{
		int id = 0;
		LoginUser login = new LoginUser();
		id = (int)EUID.getID("LoginUserID");
		login.setLoginID(id);
		login.setUserCode(usercode);
		login.setUserID(userReader.getUserByCode(usercode).getUserID());
		login.setRoleID(roleID);
		login.setHostName(hostname);
		login.setAppID(0);
		login.setPortalID(0);
		login.setServerName(servername);
		loginManager.add(login);
		return id;
	}
	public void setLoginManager(LoginUserManager loginManager) 
	{
		this.loginManager = loginManager;
	}
	public void setRoleReader(RoleReader roleReader) 
	{
		this.roleReader = roleReader;
	}
	public void setUserReader(UserReader userReader) 
	{
		this.userReader = userReader;
	}
}
