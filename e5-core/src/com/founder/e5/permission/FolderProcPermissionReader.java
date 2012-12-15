package com.founder.e5.permission;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.E5Exception;

/**
 * �ļ��в���Ȩ�޶�ȡ��
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
	 * ȡĳ�û���ĳ�ļ��е����д�Ȩ�޵����̲���
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
	 * ȡĳ�û���ĳ�ļ��е����д�Ȩ�޵ķ����̲���
	 * @param roleID
	 * @param folderID
	 */
	public int[] getProcUnflows(int roleID, int folderID) throws E5Exception{
		return getProcs(roleID, "FVUNFLOW" + folderID);
	}

}