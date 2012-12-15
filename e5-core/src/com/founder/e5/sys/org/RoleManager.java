package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Role;
import com.founder.e5.sys.org.UserRole;
import com.founder.e5.sys.org.RoleReader;

/**
 * @created 04-����-2005 15:01:26
 * @version 1.0
 * @updated 11-����-2005 12:42:55
 */
public interface RoleManager extends RoleReader {

	/**
	 * ��ӽ�ɫ
	 * @param orgID    ����֯ID
	 * @param roleName    ��ɫ��
	 * 
	 */
	public int create(int orgID, String roleName) throws E5Exception;

	/**
	 * ���½�ɫ
	 * @param role    ��ɫ����
	 * 
	 */
	public void update(Role role) throws E5Exception;

	/**
	 * ɾ����ɫ
	 * @param roleID    ��ɫID
	 * 
	 */
	public void delete(int roleID) throws E5Exception;

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
	 * ����ɫ����ĳ���û�
	 * @param userID    �û�ID
	 * @param userRole    �û���ɫ����
	 * 
	 */
	public void grantRole(int userID, UserRole userRole) throws E5Exception;

	/**
	 * ��ĳ����ɫ��ָ���û���ȡ��
	 * @param userID    �û�ID
	 * @param roleID    ��ɫID
	 * 
	 */
	public void revokeRole(int userID, int roleID) throws E5Exception;

	/**
	 * ������ָ����˳��Խ�ɫ��������
	 * @param roleIDs    ��ɫID����
	 */
	public void sortRoles(int[] roleIDs) throws E5Exception;
}