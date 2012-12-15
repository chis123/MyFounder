package com.founder.e5.sys.org;


import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.User;
import com.founder.e5.sys.org.UserFolder;
import com.founder.e5.sys.org.UserReader;

/**
 * @created 04-七月-2005 14:52:30
 * @version 1.0
 * @updated 11-七月-2005 12:42:13
 */
public interface UserManager extends UserReader {

	/**
	 * 添加用户
	 * @param user    用户对象
	 * 
	 */
	public void create(User user) throws E5Exception;

	/**
	 * 更新用户信息
	 * @param user    用户对象
	 * @throws HibernateException
	 * 
	 */
	public void update(User user) throws E5Exception;

	/**
	 * 删除用户
	 * @param userID    用户ID
	 * @throws HibernateException
	 * 
	 */
	public void delete(int userID) throws E5Exception;

	/**
	 * 创建用户文件夹
	 * @param userFolder    创建用户文件夹
	 * @throws HibernateException
	 * 
	 */
	public void create(UserFolder userFolder) throws E5Exception;

	/**
	 * 修改用户文件夹
	 * @param userFolder    修改用户文件夹
	 * 
	 */
	public void update(UserFolder userFolder) throws E5Exception;

	/**
	 * 删除用户文件夹
	 * @param userFolder    用户文件夹对象
	 * 
	 */
	public void delete(UserFolder userFolder) throws E5Exception;



	/**
	 * 对参数的用户ID数组指定用户进行排序
	 * 
	 * @param userID 用户ID数组
	 */
	public void sortUsers(int[] userID) throws E5Exception;

	/**
	 * 设置用户管理监听器
	 * @param listener
	 */
	public void setListener(ManagerEventListener listener);
}