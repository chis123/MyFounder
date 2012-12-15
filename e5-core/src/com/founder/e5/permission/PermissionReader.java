package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * Ȩ�޵Ķ�ȡ��
 * @created 14-7-2005 16:25:16
 * @author Gong Lijie
 * @version 1.0
 */
public interface PermissionReader {

	/**
	 * ȡ��ɫȨ��
	 * @param roleID ��ɫID
	 * @param resourceType ��Դ����
	 * @param resource ��Դ��
	 */
	public Permission get(int roleID, String resourceType, String resource)
		throws E5Exception;

	/**
	 * ȡ��ɫ������Ȩ��
	 * ��������ɫID
	 * 
	 * @param roleID
	 */
	public Permission[] getPermissions(int roleID)throws E5Exception;

	/**
	 * ȡ��ɫ��ĳ��Դ���͵�Ȩ��
	 * ��������ɫID����Դ����
	 * 
	 * @param roleID
	 * @param resourceType
	 */
	public Permission[] getPermissions(int roleID, String resourceType)throws E5Exception;

	/**
	 * ȡĳ��Դ���͵�����Ȩ��
	 * ��������Դ����
	 * 
	 * @param resourceType
	 */
	public Permission[] getPermissions(String resourceType)throws E5Exception;

	/**
	 * ȡĳ��Դ������Ȩ��
	 * ��������Դ���ͣ���Դ��
	 * 
	 * @param resourceType
	 * @param resource
	 */
	public Permission[] getPermissions(String resourceType, String resource)throws E5Exception;

	/**
	 * ȡĳ��ɫ��ĳЩȨ�޵�������Դ������
	 * @param roleID ��ɫID
	 * @param resourceType ��Դ����
	 * @param maskArray Ȩ�ޱ��
	 * <br/>Ȩ�ޱ����һ�����飬ֻҪӵ�и��������г�������һ��Ȩ�ޱ�ǣ�����Ϊƥ��ɹ������Է��ظ���Դ��
	 * <br/>���������Ϊ��ʱ���κ�Ȩ�޶��ɡ�
	 * <p>
	 * <li/>ȡ��ɫ5��ĳ��Դ�����ж�Ȩ�޵�������Դ:<br/>
	 * 		getResourcesOfRole(5, resourceTypeName,{PERMISSION_READ})
	 * <li/>ȡ��ɫ5��ĳ��Դ�����ж����ߴ���Ȩ�޵�������Դ:<br/>
	 * 		getResourcesOfRole(5, resourceTypeName,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>ȡ��ɫ5��ĳ��Դ�����ж����Ҵ���Ȩ�޵�������Դ:<br/>
	 * 		getResourcesOfRole(5, resourceTypeName,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 */
	public String[] getResourcesOfRole(int roleID, String resourceType, int[] maskArray)throws E5Exception;

	/**
	 * ȡ��ĳ��Դ��ĳЩȨ�޵����н�ɫ��ID
	 * @param resourceType ��Դ����
	 * @param resource ��Դ��
	 * @param permissionArray Ȩ�ޱ��
	 * <br/>Ȩ�ޱ����һ�����飬ֻҪӵ�и��������г�������һ��Ȩ�ޱ�ǣ�����Ϊƥ��ɹ������Է��ظý�ɫID��
	 * <br/>���������Ϊ��ʱ���κ�Ȩ�޶��ɡ�
	 * <p>
	 * <li/>ȡ��ĳ��Դ�ж�Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfResources(resourceTypeName, resourceName,{PERMISSION_READ})
	 * <li/>ȡ��ĳ��Դ�ж����ߴ���Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfResources(resourceTypeName, resourceName,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>ȡ��ĳ��Դ�ж����Ҵ���Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfResources(resourceTypeName, resourceName,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 */
	public int[] getRolesOfResources(String resourceType, String resource, int[] permissionArray)throws E5Exception;
	
}