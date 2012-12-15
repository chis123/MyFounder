package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:25:11
 * @author Gong Lijie
 * @version 1.0
 */
public interface PermissionManager extends PermissionReader {

	/**
	 * Ȩ�ޱ���
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * 
	 * @param permission
	 */
	public void save(Permission permission)throws E5Exception;

	/**
	 * Ȩ�޵���������
	 * �����Ϊ��������ʱ�������е�Ȩ���������
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * 
	 * @param permission
	 * @param incremental
	 */
	public void save(Permission permission, boolean incremental)throws E5Exception;

	/**
	 * ���Ȩ�ޱ���
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * 
	 * @param permissionArray
	 */
	public void save(Permission[] permissionArray)throws E5Exception;

	/**
	 * ���Ȩ�޵���������
	 * �����Ϊ��������ʱ�������е�Ȩ���������
	 * Ȩ��Ϊ0ʱ��ɾ�����е�Ȩ�޼�¼
	 * 
	 * @param permissionArray
	 * @param incremental
	 */
	public void save(Permission[] permissionArray, boolean incremental)throws E5Exception;

	/**
	 * ����ɫɾ������Ȩ��
	 * ��ĳ����ɫ��ɾ��ʱ�����ô˷������н�ɫ��ӦȨ�޵�����
	 * 
	 * @param id ��ɫID
	 */
	public void deleteRole(int id)throws E5Exception;

	/**
	 * ����Դ����ɾ������Ȩ��
	 * ��ĳ����Դ��ɾ��ʱ�����ô˷���������ӦȨ�޵�����
	 * @param resourceType ��Դ����
	 */
	public void deleteResourceType(String resourceType)throws E5Exception;

	/**
	 * ����Դ��ɾ������Ȩ��
	 * ��ĳ����Դ��ɾ��ʱ�����ô˷���������ӦȨ�޵�����
	 * 
	 * @param resourceType ��Դ����
	 * @param resource ��Դ��
	 */
	public void deleteResource(String resourceType, String resource)throws E5Exception;
	
	/**
	 * ɾ��ĳ��ɫ��ĳ��Դ���͵�����Ȩ��
	 * ����Ҫ��������ĳ����ɫ��Ȩ��ʱ����ʱ��Ҫ����Դ�����Ȱ����ж�Ӧ��Ȩ�޶���������������
	 * ���ô˷���
	 * @param roleID ��ɫID
	 * @param resourceType ��Դ����
	 * @throws E5Exception
	 */
	public void delete(int roleID, String resourceType) throws E5Exception;
	/**
	 * ȡ�����е�Ȩ�޼�¼
	 * @return Ȩ�޼�¼����
	 * @throws E5Exception
	 */
	public Permission[] getAll() throws E5Exception;
	
	/**
	 * ��һ������Դ������������Ȩ��
	 * @param roleID ��ɫID
	 * @param resourceTypePattern ��Դ���͵�ģʽ
	 * <p>
	 * ��Դ���Ϳ�����like�е�ģʽ
	 * ��find(1,"FLOW%")��ʾ���ҽ�ɫ1����������Ȩ��
	 * find(1,"CATTYPE%")��ʾ���ҽ�ɫ1�����з���Ȩ��
	 * </p>
	 * @return Permission[]
	 * @throws E5Exception
	 */
	public Permission[] find(int roleID, String resourceTypePattern) throws E5Exception;

	/**
	 * ����һ����ɫ��Ȩ������������һ����ɫ��Ȩ��
	 * ����ɫ��Ȩ�޸���
	 * ע�⣬�ý�ɫԭ����Ȩ�޻��ȱ�ȫ�����
	 * @param srcRoleID
	 * @param destRoleID
	 * @throws E5Exception
	 */
	public void copy(int srcRoleID, int destRoleID) throws E5Exception;
}