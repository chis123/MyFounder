package com.founder.e5.permission;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.E5Exception;

/**
 * �ļ��в���Ȩ�޹�����
 * @author Gong Lijie
 * @version 1.0
 * @created 2008-8-21
 */
public class FolderProcPermissionManager extends FolderProcPermissionReader {

	private PermissionManager pManager = null;
	public FolderProcPermissionManager(){
		pManager = PermissionHelper.getManager();
		pReader = pManager; //��Manager�У�ʹ�÷ǻ���Ķ�ȡ��
	}
	
	public void save(int roleID, int folderID, int[] procs, int[] unflowProcs) 
	throws E5Exception {
		//��ɾ
		pManager.delete(roleID, "FVFLOW" + folderID);
		//���
		String resourceID = StringUtils.toString(procs);
		if (!StringUtils.isBlank(resourceID))
		{
			Permission p = new Permission(roleID, "FVFLOW" + folderID, resourceID, 1);
			pManager.save(p);
		}
		
		//��ɾ
		pManager.delete(roleID, "FVUNFLOW" + folderID);
		//���
		resourceID = StringUtils.toString(unflowProcs);
		if (!StringUtils.isBlank(resourceID))
		{
			Permission p = new Permission(roleID, "FVUNFLOW" + folderID, resourceID, 1);
			pManager.save(p);
		}
	}
	/**
	 * Ȩ�޸���
	 * @param roleID
	 * @param srcFolderID
	 * @param destFolderID
	 */
	public void copy(int roleID, int srcFolderID, int destFolderID) throws E5Exception
	{
		int[] procs = getProcs(roleID, srcFolderID);
		int[] unflowProcs = getProcUnflows(roleID, srcFolderID);
		
		save(roleID, destFolderID, procs, unflowProcs);
	}
}