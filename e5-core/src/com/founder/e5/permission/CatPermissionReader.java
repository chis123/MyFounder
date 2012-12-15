package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * ����Ȩ�޵Ķ�ȡ��
 * @created 14-7-2005 16:23:40
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatPermissionReader {

	/**
	 * ȡһ����ɫ��һ�������Ȩ��
	 * @param roleID ��ɫID
	 * @param catType ��������
	 * @param catID ����ID
	 */
	public int get(int roleID, int catType, int catID) 
	throws E5Exception;

	/**
	 * ȡһ����ɫ�����з���Ȩ��
	 * 
	 * @param roleID ��ɫID
	 */
	public CatPermission[] getPermissionsByRole(int roleID) 
	throws E5Exception;

	/**
	 * ȡһ����ɫ��һ���������͵����з���Ȩ��
	 * 
	 * @param roleID ��ɫID
	 * @param catType ��������
	 */
	public CatPermission[] getPermissionsByRole(int roleID, int catType) 
	throws E5Exception;
	
	/**
	 * ȡ��һ���������������з��������Ȩ��
	 * @param catType
	 * @return
	 * @throws E5Exception
	 */
	public CatPermission[] getPermissionsByCatType(int catType) 
	throws E5Exception;

	/**
	 * ȡ��һ�����������Ȩ��
	 * @param catType
	 * @param catID
	 * @return
	 * @throws E5Exception
	 */
	public CatPermission[] getPermissionsByCat(int catType, int catID) 
	throws E5Exception;
	
	/**
	 * ȡһ����ɫ��һ������������ĳȨ�޵����з���
	 * 
	 * @param roleID ��ɫID
	 * @param catType ��������
	 * @param permissionArray Ȩ�ޱ��
	 * <br/>Ȩ�ޱ����һ�����飬ֻҪӵ�и��������г�������һ��Ȩ�ޱ�ǣ�����Ϊƥ��ɹ������Է��ظ÷���ID��
	 * <br/>���������Ϊ��ʱ���κ�Ȩ�޶��ɡ�
	 * <p>
	 * <li/>ȡ��ɫ5�Է�������4�ж�Ȩ�޵����з���:<br/>
	 * 		getCatsOfRole(5,4,{PERMISSION_READ})
	 * <li/>ȡ��ɫ5�Է�������4�ж����ߴ���Ȩ�޵����з���:<br/>
	 * 		getCatsOfRole(5,4,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>ȡ��ɫ5�Է�������4�ж����Ҵ���Ȩ�޵����з���:<br/>
	 * 		getCatsOfRole(5,4,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 * ��ϵͳֻ��һ�ַ���Ȩ��ʱ��������������ֱ�Ӵ���null
	 */
	public int[] getCatsOfRole(int roleID, int catType, int[] permissionArray)
	throws E5Exception;

	/**
	 * ȡһ����ɫ��һ��������ĳȨ�޵������ӷ���
	 * �÷�������һ����ṩ����adapter
	 */
//	public int[] getSubCategoriesOfRole(int roleID, int catType, int catID, int[] permissioArray)
//	throws E5Exception;

	/**
	 * ȡ��һ��������ĳȨ�޵����н�ɫ
	 * 
	 * @param catType ��������
	 * @param catID ����ID
	 * @param permissionArray Ȩ�ޱ��
	 * <br/>Ȩ�ޱ����һ�����飬ֻҪӵ�и��������г�������һ��Ȩ�ޱ�ǣ�����Ϊƥ��ɹ������Է��ظý�ɫID��
	 * <br/>���������Ϊ��ʱ���κ�Ȩ�޶��ɡ�
	 * <p>
	 * <li/>ȡ�Է���4����������5���ж�Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfCat(5,4,{PERMISSION_READ})
	 * <li/>ȡ�Է���5����������4���ж����ߴ���Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfCat(5,4,{PERMISSION_READ, PERMISSION_PROCESS})
	 * <li/>ȡ�Է���5����������4���ж����Ҵ���Ȩ�޵����н�ɫ:<br/>
	 * 		getRolesOfCat(5,4,{PERMISSION_READ & PERMISSION_PROCESS})
	 * </p>
	 * ��ϵͳֻ��һ�ַ���Ȩ��ʱ��������������ֱ�Ӵ���null
	 */
	public int[] getRolesOfCat(int catType, int catID, int[] permissionArray)
	throws E5Exception;

}