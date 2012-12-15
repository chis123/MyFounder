package com.founder.e5.sso;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Role;

/**
 * E5系统用户认证部分
 * @author LuZuowei
 */
public interface SSO 
{
	/**
	 * 验证用户和密码的过程
	 * @param usercode
	 * @param password
	 * @return 0:用户和密码正确，-1用户不存在，-2表示用户密码不正确
	 */
	public int verifyUserPassword(String usercode,String password) throws E5Exception;
	
	/**
	 * 返回当前用户所有有效角色
	 * @param usercode
	 * @return 确认是有效的角色，检查时间范围的
	 */
	public Role[] getValidRole(String usercode) throws E5Exception;
	
	/**
	 * 登记注册的过程
	 * @param usercode
	 * @param roleID
	 * @param hostname
	 * @param servername
	 * @return 返回数组一定是二维，
	 * 	[0]:是loginid的值，-1表示失败，>0表示成功
	 *  [1]:"samemachine"表示在统一机器上登录过，否则就是登录机器的名称
	 */
	public String[] login(String usercode,int roleID,String hostname,String servername,boolean concurrent) throws E5Exception;
	
	/**
	 * 确认最后访问时间
	 * @param loginid
	 * @return 结果信息，0：表示成功，-1:表示角色不再有效时间内，-2:表示系统管理员请下来
	 */
	public int accessLast(int loginid,int userid,int roleid) throws E5Exception; 
	
	/**
	 * 正常退出的过程
	 * @param loginid
	 */
	public void logout(int loginid) throws E5Exception;
}
