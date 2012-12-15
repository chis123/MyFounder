package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Role;


/**
 * @created 04-����-2005 15:01:26
 * @version 1.0
 * @updated 11-����-2005 12:42:36
 */
public interface RoleReader {

	/**
	 * ���ָ����ɫID�Ľ�ɫ����
	 * @param roleID    roleID
	 * 
	 */
	public Role get(int roleID) throws E5Exception;

	/**
	 * ������ 2006-4-19
	 * ��ȡ���н�ɫ����
	 * @return
	 * @throws E5Exception
	 */

	public Role [] getRoles() throws E5Exception;
	/**
	 * ���ָ���û������н�ɫ
	 * @param userID    �û�ID
	 * 
	 */
	public Role[] getRolesByUser(int userID) throws E5Exception;

	/**
	 * �����ָ����֯�µ�ָ�������µĽ�ɫ
	 * @param orgID    ��֯ID
	 * @param roleName    ��ɫ����
	 * 
	 */
	public Role getRoleByName(int orgID, String roleName) throws E5Exception;
	
	
	/**
	 * ���ָ����ɫ�ĸ���֯
	 * @param roleID    ��ɫID
	 * 
	 */
	public Org getParentOrgByRole(int roleID) throws E5Exception;

//�����������ӿ�2006-4-14
	/**
	 * ���ָ���û��������û���ɫ����
	 * @param userID    �û�ID
	 * 
	 */
	public UserRole[] getUserRoles(int userID) throws E5Exception;

	/**
	 * ���ָ���û���ָ����ɫ�µ��û���ɫ����
	 * @param userID    �û�ID
	 * @param roleID    ��ɫID
	 * 
	 */
	public UserRole getUserRole(int userID, int roleID) throws E5Exception;
	
	/**
	 * ���������� 2006-4-19
	 * ��������û���ɫ��ϵ
	 * @return
	 * @throws E5Exception
	 */
	public UserRole [] getUserRoles() throws E5Exception;
	
	
	//���������ӣ�ʵ�ֽ�ɫ��ѯ 2006-3-8
	/*
	 * ���ݽ�ɫ����ȡ��Ӧ�Ľ�ɫ
	 * 
	 * 
	 */
	 public Role [] getRolesByName(String roleName) throws E5Exception;

		//���������ӣ�ʵ�ֽ�ɫ��ѯ 2006-3-8
		/*
		 * ���ݽ�ɫ���е�ĳЩ�ʻ�ȡ��Ӧ�Ľ�ɫ
		 * 
		 * 
		 */
	 public Role [] getRolesIncludeName(String roleName) throws E5Exception;

}