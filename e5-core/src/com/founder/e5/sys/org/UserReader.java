package com.founder.e5.sys.org;


import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.User;
import com.founder.e5.sys.org.UserFolder;

/**
 * @created 04-七月-2005 14:52:30
 * @version 1.0
 * @updated 11-七月-2005 13:10:42
 */
public interface UserReader {

	/**
	 * 获得所有的用户
	 */
	public User[] getUsers() throws E5Exception;

	/**
	 * 获得用户ID指定的用户
	 * @param userID    用户ID
	 * @throws HibernateException
	 * 
	 */
	public User getUserByID(int userID) throws E5Exception;

	/**
	 * 获得用户代码指定的用户
	 * @param userCode    用户编码
	 * @throws HibernateException
	 * 
	 */
	public User getUserByCode(String userCode) throws E5Exception;

	/**
	 * 获得组织下的所有用户
	 * @param orgID    组织ID
	 * 
	 */
	public User[] getUsersByOrg(int orgID) throws E5Exception;

	/**
	 * 获得角色下的所有用户
	 * @param roleID    角色ID
	 * 
	 */
	public User[] getUsersByRole(int roleID) throws E5Exception;

	/**
	 * 王朝阳增加 2006-4-19
	 * 获得所有用户文件夹
	 * 
	 */
	public UserFolder[] getUserFolders() throws E5Exception;

	
	/**
	 * 获得指定用户的所有用户文件夹
	 * @param userID    用户ID
	 * 
	 */
	public UserFolder[] getUserFolders(int userID) throws E5Exception;

	/**
	 * 获得指定用户ID，指定文档类型下的用户文件夹
	 * @param docTypeID    文档类型ID
	 * @param userID    用户ID
	 * 
	 */
	public UserFolder[] getUserFolders(int docTypeID, int userID) throws E5Exception;

	/**
	 * 获得指定用户ID，指定文档库下的用户文件夹
	 * @param userID    用户ID
	 * @param libID    库ID
	 * 
	 */
	public UserFolder getUserFolder(int userID, int libID) throws E5Exception;
	
	/**
	 * 获得指定用户的父组织
	 * @param userID    用户ID
	 * 
	 */
	public Org getParentOrg(int userID) throws E5Exception;

	/**
	 * 获得指定用户的父组织
	 * @param userCode    用户代码
	 * 
	 */
//	王朝阳增加，为了判断一个机构节点下是否有用户节点 2006-3-10
	public int getUserCountByOrg(int orgID) throws E5Exception;
	
	public Org getParentOrg(String userCode) throws E5Exception;
	
//	王朝阳增加，实现用户查询 2006-3-8
	/*
	 * 根据用户名获取对应的用户
	 * 
	 * 
	 */
	 public User [] getUsersByName(String userName) throws E5Exception;

		//王朝阳增加，实现用户查询 2006-3-8
		/*
		 * 根据用户名中的某些词获取对应的用户
		 * 
		 * 
		 */
	 public User [] getUsersIncludeName(String userName) throws E5Exception;
	

}