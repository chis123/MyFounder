package com.founder.e5.permission;

import java.util.ArrayList;
import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 14-7-2005 16:23:56
 * @author Gong Lijie
 * @version 1.0
 */
class CatPermissionManagerImpl implements CatPermissionManager {

	CatPermissionManagerImpl()
	{
		super();
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionManager#save(com.founder.e5.permission.CatPermission, boolean)
	 */
	public void save(CatPermission permission, boolean incremental) throws E5Exception
	{
		Permission p = new Permission(permission.getRoleID(), 
					"CATTYPE" + permission.getCatType(),
					"CAT" + permission.getCatID(),
					permission.getPermission());
		PermissionHelper.getManager().save(p, incremental);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionManager#save(com.founder.e5.permission.CatPermission[], boolean)
	 */
	public void save(CatPermission[] permissionArray, boolean incremental) throws E5Exception
	{
		Permission[] pArr = new Permission[permissionArray.length];
		for (int i = 0; i < permissionArray.length; i++){
			pArr[i] = new Permission(permissionArray[i].getRoleID(), 
				"CATTYPE" + permissionArray[i].getCatType(),
				"CAT" + permissionArray[i].getCatID(),
				permissionArray[i].getPermission());
		}
		PermissionHelper.getManager().save(pArr, incremental);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionManager#deleteCat(int, int)
	 */
	public void deleteCat(int catType, int catID) throws E5Exception
	{
		PermissionHelper.getManager().deleteResource("CATTYPE" + catType, "CAT" + catID);
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionReader#get(int, int, int)
	 */
	public int get(int roleID, int catType, int catID) throws E5Exception
	{
		Permission p = PermissionHelper.getManager().get(roleID, "CATTYPE" + catType, "CAT" + catID);
		if (p != null)
			return p.getPermission();
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionReader#getPermissionsByRole(int)
	 */
	public CatPermission[] getPermissionsByRole(int roleID) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().find(roleID, "CATTYPE%");
		return getCPArrayByRole(pArr, roleID);
	}

	private CatPermission[] getCPArrayByRole(Permission[] pArr, int roleID){
		if (pArr == null) return null;
		
		CatPermission[] fpArr = new CatPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new CatPermission(roleID, 
					Integer.parseInt(pArr[i].getResourceType().substring(7)),
					Integer.parseInt(pArr[i].getResource().substring(3)),
					pArr[i].getPermission());
		}
		return fpArr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionReader#getPermissionsByRole(int, int)
	 */
	public CatPermission[] getPermissionsByRole(int roleID, int catType) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions(roleID, "CATTYPE" + catType);

		if (pArr == null) return null;
		
		CatPermission[] fpArr = new CatPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new CatPermission(roleID, 
					catType,
					Integer.parseInt(pArr[i].getResource().substring(3)),
					pArr[i].getPermission());
		}
		return fpArr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionReader#getCatsOfRole(int, int, int[])
	 */
	public int[] getCatsOfRole(int roleID, int catType, int[] maskArray) throws E5Exception
	{
		CatPermission[] pArr = getPermissionsByRole(roleID, catType);
		if (pArr == null) return null;
		if (maskArray == null) {
			int[] arr = new int[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				arr[i] = pArr[i].getCatID();
			}
			return arr;
		}
		//比较权限，得到合适的分类
		List list = new ArrayList(pArr.length);
		for (int i = 0; i < pArr.length; i++) {
			if (PermissionHelper.hasPermission(pArr[i].getPermission(), maskArray))
				list.add(new Integer(pArr[i].getCatID()));
		}
		
		int size = list.size();
		if (size == 0) return null;
		int[] arr = new int[size];
		for (int i = 0; i < size; i++)
			arr[i] = ((Integer)list.get(i)).intValue();
		return arr;
	}

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionReader#getRolesOfCat(int, int, int[])
	 */
	public int[] getRolesOfCat(int catType, int catID, int[] maskArray) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions("CATTYPE" + catType, "CAT" + catID);
		if (pArr == null) return null;

		if (maskArray == null) {
			int[] arr = new int[pArr.length];
			for (int i = 0; i < pArr.length; i++) {
				arr[i] = pArr[i].getRoleID();
			}
			return arr;
		}
		return PermissionHelper.getRoleByMask(pArr, maskArray);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionManager#deleteCatType(int)
	 */
	public void deleteCatType(int catType) throws E5Exception
	{
		PermissionHelper.getManager().deleteResourceType("CATTYPE" + catType);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionReader#getPermissionsByCatType(int)
	 */
	public CatPermission[] getPermissionsByCatType(int catType) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions("CATTYPE" + catType);

		if (pArr == null) return null;
		
		CatPermission[] fpArr = new CatPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new CatPermission(pArr[i].getRoleID(), 
					catType,
					Integer.parseInt(pArr[i].getResource().substring(3)),
					pArr[i].getPermission());
		}
		return fpArr;
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.CatPermissionReader#getPermissionsByCat(int, int)
	 */
	public CatPermission[] getPermissionsByCat(int catType, int catID) throws E5Exception
	{
		Permission[] pArr = PermissionHelper.getManager().getPermissions("CATTYPE" + catType, "CAT" + catID);

		if (pArr == null) return null;
		
		CatPermission[] fpArr = new CatPermission[pArr.length];
		 
		for (int i = 0; i < fpArr.length; i++)
		{
			fpArr[i] = new CatPermission(pArr[i].getRoleID(), 
					catType,
					catID,
					pArr[i].getPermission());
		}
		return fpArr;
	}
}