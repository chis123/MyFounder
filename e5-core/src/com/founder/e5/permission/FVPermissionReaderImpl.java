package com.founder.e5.permission;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:23:25
 * @author Gong Lijie
 * @version 1.0
 */
class FVPermissionReaderImpl implements FVPermissionReader {

	FVPermissionReaderImpl()
	{
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#get(int, int)
	 */
	public int get(int roleID, int fvID) throws E5Exception
	{
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null)
			return cache.getFVPermission(roleID, fvID);
		return PermissionHelper.getFVPermissionManager().get(roleID, fvID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getPermissionsByRole(int)
	 */
	public FVPermission[] getPermissionsByRole(int roleID) throws E5Exception
	{
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null){
			List list = cache.getFVPermissionsByRole(roleID);
			if (list.size() == 0) return null;
			return (FVPermission[])list.toArray(new FVPermission[0]);
		}
		return PermissionHelper.getFVPermissionManager().getPermissionsByRole(roleID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getFoldersOfRole(int, int[])
	 */
	public int[] getFoldersOfRole(int roleID, int[] maskArray) throws E5Exception
	{
		FVPermission[] pArr = getPermissionsByRole(roleID);
		if (pArr == null) return null;
		
		if (maskArray == null) {
			int[] fvArr = new int[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				fvArr[i] = pArr[i].getFvID();
			}
			return fvArr;
		}
	
		//比较权限，得到合适的角色
		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i].getPermission(), maskArray))
				list.add(new Integer(pArr[i].getFvID()));
		}
		
		int size = list.size();
		if (size == 0) return null;
		int[] fvArr = new int[size];
		for (int i = 0; i < size; i++)
			fvArr[i] = ((Integer)list.get(i)).intValue();
		return fvArr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getRolesOfFolder(int, int[])
	 */
	public int[] getRolesOfFolder(int fvID, int[] maskArray) throws E5Exception
	{
		
		List list0 = PermissionHelper.getCache().getFVPermissionsByFolder(fvID);
		if (list0.size() == 0) return null;

		if (maskArray == null) {
			int[] roleArr = new int[list0.size()];
			for (int i = 0; i < roleArr.length; i++) {
				roleArr[i] = ((FVPermission)list0.get(i)).getRoleID();
			}
			return roleArr;
		}
	
		//比较权限，得到合适的角色
		List list = new ArrayList(list0.size());
		for (int i = 0; i < list0.size(); i++) {
			FVPermission p = (FVPermission)list0.get(i);
			if (PermissionHelper.hasPermission(p.getPermission(), maskArray))
				list.add(new Integer(p.getRoleID()));
		}
		
		int size = list.size();
		if (size == 0) return null;
		int[] roleArr = new int[size];
		for (int i = 0; i < size; i++)
			roleArr[i] = ((Integer)list.get(i)).intValue();
		return roleArr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.FVPermissionReader#getPermissionsByFolder(int)
	 */
	public FVPermission[] getPermissionsByFolder(int fvID) throws E5Exception
	{
		PermissionCache cache = PermissionHelper.getCache();
		if (cache != null){
			List list = cache.getFVPermissionsByFolder(fvID);
			if (list.size() == 0) return null;
			return (FVPermission[])list.toArray(new FVPermission[0]);
		}
		return PermissionHelper.getFVPermissionManager().getPermissionsByFolder(fvID);
	}
}