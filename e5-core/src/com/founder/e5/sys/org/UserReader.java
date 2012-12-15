package com.founder.e5.sys.org;


import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.User;
import com.founder.e5.sys.org.UserFolder;

/**
 * @created 04-����-2005 14:52:30
 * @version 1.0
 * @updated 11-����-2005 13:10:42
 */
public interface UserReader {

	/**
	 * ������е��û�
	 */
	public User[] getUsers() throws E5Exception;

	/**
	 * ����û�IDָ�����û�
	 * @param userID    �û�ID
	 * @throws HibernateException
	 * 
	 */
	public User getUserByID(int userID) throws E5Exception;

	/**
	 * ����û�����ָ�����û�
	 * @param userCode    �û�����
	 * @throws HibernateException
	 * 
	 */
	public User getUserByCode(String userCode) throws E5Exception;

	/**
	 * �����֯�µ������û�
	 * @param orgID    ��֯ID
	 * 
	 */
	public User[] getUsersByOrg(int orgID) throws E5Exception;

	/**
	 * ��ý�ɫ�µ������û�
	 * @param roleID    ��ɫID
	 * 
	 */
	public User[] getUsersByRole(int roleID) throws E5Exception;

	/**
	 * ���������� 2006-4-19
	 * ��������û��ļ���
	 * 
	 */
	public UserFolder[] getUserFolders() throws E5Exception;

	
	/**
	 * ���ָ���û��������û��ļ���
	 * @param userID    �û�ID
	 * 
	 */
	public UserFolder[] getUserFolders(int userID) throws E5Exception;

	/**
	 * ���ָ���û�ID��ָ���ĵ������µ��û��ļ���
	 * @param docTypeID    �ĵ�����ID
	 * @param userID    �û�ID
	 * 
	 */
	public UserFolder[] getUserFolders(int docTypeID, int userID) throws E5Exception;

	/**
	 * ���ָ���û�ID��ָ���ĵ����µ��û��ļ���
	 * @param userID    �û�ID
	 * @param libID    ��ID
	 * 
	 */
	public UserFolder getUserFolder(int userID, int libID) throws E5Exception;
	
	/**
	 * ���ָ���û��ĸ���֯
	 * @param userID    �û�ID
	 * 
	 */
	public Org getParentOrg(int userID) throws E5Exception;

	/**
	 * ���ָ���û��ĸ���֯
	 * @param userCode    �û�����
	 * 
	 */
//	���������ӣ�Ϊ���ж�һ�������ڵ����Ƿ����û��ڵ� 2006-3-10
	public int getUserCountByOrg(int orgID) throws E5Exception;
	
	public Org getParentOrg(String userCode) throws E5Exception;
	
//	���������ӣ�ʵ���û���ѯ 2006-3-8
	/*
	 * �����û�����ȡ��Ӧ���û�
	 * 
	 * 
	 */
	 public User [] getUsersByName(String userName) throws E5Exception;

		//���������ӣ�ʵ���û���ѯ 2006-3-8
		/*
		 * �����û����е�ĳЩ�ʻ�ȡ��Ӧ���û�
		 * 
		 * 
		 */
	 public User [] getUsersIncludeName(String userName) throws E5Exception;
	

}