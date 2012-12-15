package com.founder.e5.sys;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.LoginUser;
/**
 * @created 04-七月-2005 16:01:14
 * @version 1.0
 * @updated 11-七月-2005 13:04:29
 * Lu Zuowei modified 2006/4/4
 */
public interface LoginUserManager 
{
	/**
	 * 记录LoginUser记录在Fsys_LoginUser
	 * @param loginUser    登录用户的信息loginUser
	 */
	public void add(LoginUser login) throws E5Exception;
	
	/**
	 * 删除LoginUser记录从Fsys_LoginUser	 * 
	 * 并且转入到归档的用户日志表中
	 * @param normal 是否正常退出
	 */
	public void remove(int loginid,boolean normal) throws E5Exception;
	
	/**
	 * 删除LoginUser记录从Fsys_LoginUser转移到Fsys_LoginArchive中
	 * 自动认为非正常退出，删除条件为早于date传入参数的部分
	 * @param date: 日期格式，必须严格为:2000-01-01 15:30:40.000
	 * @throws E5Exception
	 */
	public void remove(String date) throws E5Exception;
	/**
	 * 刷新时间的过程
	 * @param loginid
	 * @throws E5Exception
	 * @return 影响行数
	 */
	public int access(int loginid) throws E5Exception;
	
	/**
	 * 获得当前的登录用户
	 */
	public LoginUser[] get() throws E5Exception;

	/**
	 * 根据登录ID获取当前登录用户信息
	 * @param loginid
	 * @return
	 * @throws E5Exception
	 */
	public LoginUser getById(int loginid) throws E5Exception;
	
	/**
	 * 得到所有的系统登录用户信息,并按一定的字段进行排序
	 * 王朝阳增加，按用户属性进行排序的登录用户集合 2006-03-06
	 */
	public LoginUser[] getBySort(String sortField) throws E5Exception;

	/**
	 * 得到所有的系统登录用户信息,并按一定的字段进行排序
	 * 王朝阳增加，按用户属性进行排序的登录用户集合 2006-03-06
	 */
	public LoginUser[] getBySort(String sortField,String sortBy) throws E5Exception;
	
	/**
	 * 获得指定用户编码的登录用户
	 * @param userCode    用户登录名
	 */
	public LoginUser[] get(String userCode) throws E5Exception;

	/**
	 * 获得指定用户的当前登录用户
	 * @param userID    登录用户的ID
	 * 
	 */
	public LoginUser[] get(int userID) throws E5Exception;
	
	/**
	 * 获取指定用户的当前登录者
	 * @param userCode
	 * @param roleID
	 * @return
	 * @throws E5Exception
	 */
	public LoginUser get(String userCode, int roleID) throws E5Exception;
	/**
	 * 根据指定用户、角色确认当前登录状态
	 * @param userCode
	 * @param roleID
	 * @param hostName
	 * @return
	 * @throws E5Exception
	 */
	public LoginUser get(String userCode, int roleID, String hostName) throws E5Exception;
}