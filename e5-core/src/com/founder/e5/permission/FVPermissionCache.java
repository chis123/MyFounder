package com.founder.e5.permission;
import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;

/**
 * @deprecated 2006-5-17
 * �Ѿ����ϲ���PermissionCache����
 * 
 * @created 14-7-2005 16:24:43
 * @author Gong Lijie
 * @version 1.0
 */
public class FVPermissionCache implements Cache {

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
	public FVPermissionCache(){
		
	}
	public void refresh() throws E5Exception{
		FVPermissionManager manager = PermissionHelper.getFVPermissionManager();
		fvPermissions = manager.getAll();
	}

	public void reset(){
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