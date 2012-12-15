package com.founder.e5.permission;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.E5Exception;

/**
 * 文件夹操作权限读取器
 * @author Gong Lijie
 * @version 1.0
 * @created 2008-8-21
 */
public class FolderProcPermissionReader {
	protected PermissionReader pReader = null;

	public FolderProcPermissionReader(){
		pReader = PermissionHelper.getReader();
	}

	/**
	 * 取某用户对某文件夹的所有带权限的流程操作
	 * @param roleID
	 * @param folderID
	 */
	public int[] getProcs(int roleID, int folderID) throws E5Exception{
		return getProcs(roleID, "FVFLOW" + folderID);
	}

	private int[] getProcs(int roleID, String resourceType) throws E5Exception{
		Permission[] pArr = pReader.getPermissions(roleID, resourceType);
		if (pArr == null) return null;
		
		String folders = pArr[0].getResource();
		return StringUtils.getIntArray(folders);
	}
	/**
	 * 取某用户对某文件夹的所有带权限的非流程操作
	 * @param roleID
	 * @param folderID
	 */
	public int[] getProcUnflows(int roleID, int folderID) throws E5Exception{
		return getProcs(roleID, "FVUNFLOW" + folderID);
	}

}