package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * �ļ�����ͼȨ�޵Ķ�ȡ��
 * 
 * @updated 18-7-2005 15:32:17
 * @author Gong Lijie
 * @version 1.0
 */
public interface FVPermissionReader {

	/**
	 * ȡһ����ɫ��һ���ļ���/��ͼ��Ȩ��
	 * 
	 * @param roleID    ��ɫID
	 * @param fvID    �ļ���/��ͼID
	 * @return int	Ȩ��ֵ
	 * @throws E5Exception
	 */
	public int get(int roleID, int fvID) throws E5Exception;

	/**
	 * ȡһ����ɫ�����е��ļ���/��ͼȨ��
	 * @param roleID    ��ɫID
	 * @return FVPermission[]
	 * @throws E5Exception
	 */
	public FVPermission[] getPermissionsByRole(int roleID) throws E5Exception;

	/**
	 * ȡĳ���ļ��е����е�Ȩ�޼�¼���������н�ɫ��Ȩ�޼�¼
	 * @param fvID �ļ���ID
	 * @return FVPermission[]
	 * @throws E5Exception
	 */
	public FVPermission[] getPermissionsByFolder(int fvID) throws E5Exception;

	/**
	 * ȡһ����ɫ��ĳȨ�޵������ļ���/��ͼ
	 * @param roleID    ��ɫID
	 * @param permissionArray    Ȩ�ޱ��
	 * <br/>Ȩ�ޱ����һ�����飬ֻҪӵ�и��������г�������һ��Ȩ�ޱ�ǣ�����Ϊƥ��ɹ������Է��ظ��ļ���/��ͼID��
	 * <br/>���������Ϊ��ʱ���κ�Ȩ�޶��ɡ�
	 * <p>
	 * <li/>ȡ��ɫ5�ж�Ȩ�޵������ļ���:<br/>
	 * 		getFoldersOfRole(5,{FVPermission.PERMISSION_READ})
	 * <li/>ȡ��ɫ5�ж����ߴ���Ȩ�޵������ļ���:<br/>
	 * 		getFoldersOfRole(5,{FVPermission.PERMISSION_READ, FVPermission.PERMISSION_PROCESS})
	 * <li/>ȡ��ɫ5�ж����Ҵ���Ȩ�޵������ļ���:<br/>
	 * 		getFoldersOfRole(5,{FVPermission.PERMISSION_READ & FVPermission.PERMISSION_PROCESS})
	 * </p>
	 */
	public int[] getFoldersOfRole(int roleID, int[] permissionArray) throws E5Exception;

	/**
	 * ȡ��ĳ�ļ�����ĳȨ�޵����н�ɫ
	 * 
	 * @param fvID    �ļ�����ͼID
	 * @param permissionArray    Ȩ�ޱ��
	 * <br/>Ȩ�ޱ����һ�����飬ֻҪӵ�и��������г�������һ��Ȩ�ޱ�ǣ�����Ϊƥ��ɹ������Է��ظý�ɫID��
	 * <br/>���������Ϊ��ʱ���κ�Ȩ�޶��ɡ�
	 * <p>
	 * <li/>ȡ���ļ���5�ж�Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfFolder(5,{FVPermission.PERMISSION_READ})
	 * <li/>ȡ���ļ���5�ж����ߴ���Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfFolder(5,{FVPermission.PERMISSION_READ, FVPermission.PERMISSION_PROCESS})
	 * <li/>ȡ���ļ���5�ж����Ҵ���Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfFolder(5,{FVPermission.PERMISSION_READ & FVPermission.PERMISSION_PROCESS})
	 * </p>
	 */
	public int[] getRolesOfFolder(int fvID, int[] permissionArray) throws E5Exception;

}