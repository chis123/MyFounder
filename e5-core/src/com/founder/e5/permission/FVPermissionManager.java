package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * ϵͳ�������ļ���Ȩ�����úͶ�ȡ�Ĺ���ӿ�
 * ȡ��ɫȨ��ʱ�����̳С�
 * @created 14-7-2005 16:22:55
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVPermissionManager extends FVPermissionReader {

	/**
	 * ����Ȩ��
	 * 
	 * @param permission
	 */
	public void save(FVPermission permission) throws E5Exception;

	/**
	 * ������Ȩ��
	 * 
	 * @param permissionArray
	 */
	public void save(FVPermission[] permissionArray) throws E5Exception;

	/**
	 * ɾ��һ���ļ�����ͼʱɾ�����ж���Ȩ��
	 * 
	 * @param fvID �ļ�����ͼID
	 */
	public void deleteFolder(int fvID) throws E5Exception;
	
	/**
	 * ɾ��һ����ɫʱɾ�������е��ļ���Ȩ��
	 * @param roleID
	 * @throws E5Exception
	 */
	public void deleteRole(int roleID) throws E5Exception;

	/**
	 * һ����ȡ�������ļ���Ȩ��
	 * @throws E5Exception
	 */
	public FVPermission[] getAll() throws E5Exception;

	/**
	 * ����һ����ɫ��Ȩ������������һ����ɫ���ļ���Ȩ��
	 * ����ɫ���ļ���Ȩ�޸���
	 * ע�⣬�ý�ɫԭ�����ļ���Ȩ�޻��ȱ�ȫ�����
	 * @param srcRoleID
	 * @param destRoleID
	 * @throws E5Exception
	 */
	public void copy(int srcRoleID, int destRoleID) throws E5Exception;
}