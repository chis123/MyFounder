package com.founder.e5.permission;
import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:24:48
 * @author Gong Lijie
 * @version 1.0
 */
public class PermissionCache implements Cache {

	private Permission[] permissions;
	private FVPermission[] fvPermissions;
	
	/**
	 * ��������Ҫ����Readerʵ������<br>
	 * ������ʱ��ע�ⲻҪʹ�ù��췽�������������ʵ��<br>
	 * ��ʹ��������ʽ��<br>
	 * <code>
	 * XXXCache cache = (XXXCache)(CacheReader.find(XXXCache.class));
	 * if (cache != null)
	 * 		return cache.get(...);
	 * </code>
	 * ����XXXCache�������Ļ�����
	 */
	public PermissionCache(){
		
	}
	public void refresh() throws E5Exception {
		permissions = PermissionHelper.getManager().getAll();
		FVPermissionManager manager = PermissionHelper.getFVPermissionManager();
		fvPermissions = manager.getAll();
	}

	public void reset(){

	}

	/**
	 * ȡ��ɫȨ��
	 * @param roleID ��ɫID
	 * @param resourceType ��Դ����
	 * @param resource ��Դ��
	 * @return Permission ���ӻ������Ҳ�����Ӧ�ļ�¼���򷵻�null
	 */
	Permission getPermission(int roleID, String resourceType, String resource){
		if (permissions == null) return null;
		for (int i = 0; i < permissions.length; i++){
			if ((permissions[i].getRoleID() == roleID) 
					&& (permissions[i].getResourceType().equals(resourceType))
					&& (permissions[i].getResource().equals(resource)))
				return permissions[i];
		}
		return null;
	}

	/**
	 * ȡ��ɫ������Ȩ��
	 * @param roleID
	 */
	List getPermissions(int roleID){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].getRoleID() == roleID) 
				list.add(permissions[i]);
		}
		return list;
	}

	/**
	 * ȡ��ɫ��ĳ��Դ���͵�����Ȩ��
	 * @param roleID
	 * @param resourceType
	 */
	List getPermissions(int roleID, String resourceType){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++){
			if ((permissions[i].getRoleID() == roleID) 
				&& (permissions[i].getResourceType().equals(resourceType)))
				list.add(permissions[i]);
		}
		return list;
	}

	/**
	 * ȡĳ��Դ������Ȩ��
	 * ��������Դ���͡���Դ��
	 * 
	 * @param resourceType
	 * @param resource
	 */
	List getPermissions(String resourceType, String resource){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].getResourceType().equals(resourceType)
					&& permissions[i].getResource().equals(resource)) 
				list.add(permissions[i]);
		}
		return list;
	}

	/**
	 * ȡĳ��Դ���͵�����Ȩ��
	 * ��������Դ����
	 * 
	 * @param resourceType
	 */
	List getPermissions(String resourceType){
		if (permissions == null) return null;

		List list = new ArrayList(10);
		for (int i = 0; i < permissions.length; i++) {
			if (permissions[i].getResourceType().equals(resourceType)) 
				list.add(permissions[i]);
		}
		return list;
	}
	/**
	 * ȡ��ɫ���ļ���Ȩ��
	 * �ڲ��������ϳ��á�
	 * 
	 * @param roleID ��ɫID
	 * @param fvID �ļ���/��ͼID
	 */
	int getFVPermission(int roleID, int fvID){
		if (fvPermissions == null) return 0;
		
		for (int i = 0; i < fvPermissions.length; i++)
			if ((fvPermissions[i].getRoleID() == roleID)
					&& (fvPermissions[i].getFvID() == fvID))
				return fvPermissions[i].getPermission();
		return 0;
	}

	/**
	 * ȡ��ɫ�������ļ���Ȩ��
	 * �ճ������������Դ��ʹ�á�
	 * 
	 * @param roleID ��ɫID
	 */
	List getFVPermissionsByRole(int roleID){
		List list = new ArrayList();
		if (fvPermissions == null) return list;
		
		for (int i = 0; i < fvPermissions.length; i++)
			if (fvPermissions[i].getRoleID() == roleID)
				list.add(fvPermissions[i]);
		return list;
	}

	/**
	 * ȡĳ�ļ���/��ͼ������Ȩ��
	 * @param fvID �ļ�����ͼID
	 */
	List getFVPermissionsByFolder(int fvID){
		List list = new ArrayList();
		if (fvPermissions == null) return list;
		
		for (int i = 0; i < fvPermissions.length; i++)
			if (fvPermissions[i].getFvID() == fvID)
				list.add(fvPermissions[i]);
		return list;
	}
}