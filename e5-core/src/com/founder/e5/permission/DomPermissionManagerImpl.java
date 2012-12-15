package com.founder.e5.permission;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.E5Exception;

/**
 * �ĵ������ֵ�Ȩ�޹�����ʵ����
 * @created 2008-4-21
 * @author Gong Lijie
 * @version 1.0
 */
class DomPermissionManagerImpl implements DomPermissionManager {

	/* (non-Javadoc)
	 * @see com.founder.e5.permission.DomPermissionReader#getRightlessFilters(int)
	 */
	public int[] getRightlessFilters(int roleID) throws E5Exception {
		int[] pIDs = null;
		
		Permission[] pArr = PermissionHelper.getManager().getPermissions(roleID, TAG_PERMISSION_FILTER);
		if (pArr != null && pArr.length > 0)
		{
			String permission = pArr[0].getResource();
			pIDs = StringUtils.getIntArray(permission, ",");
		}
		return pIDs;
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.DomPermissionReader#hasFilterRight(int, int)
	 */
	public boolean hasFilterRight(int roleID, int filterID) throws E5Exception {
		//������Ȩ�޵�Ȩ�޼�¼�Ƚ����⣬ȱʡ����Ȩ�޵ģ�����0��ʾ��Ȩ�ޣ�1��ʾû��Ȩ��
		int[] pArr = getRightlessFilters(roleID);
		if (pArr == null) return true;
		
		for (int i = 0; i < pArr.length; i++) {
			if (pArr[i] == filterID) return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.DomPermissionReader#getRules(int)
	 */
	public int[] getRules(int roleID) throws E5Exception {
		int[] pIDs = null;
		
		Permission[] pArr = PermissionHelper.getManager().getPermissions(roleID, TAG_PERMISSION_RULE);
		if (pArr != null && pArr.length > 0)
		{
			String permission = pArr[0].getResource();
			pIDs = StringUtils.getIntArray(permission, ",");
		}
		return pIDs;
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.DomPermissionReader#hasRuleRight(int, int)
	 */
	public boolean hasRuleRight(int roleID, int ruleID) throws E5Exception {
		int[] pArr = getRules(roleID);
		if (pArr == null) return false;
		
		for (int i = 0; i < pArr.length; i++) {
			if (pArr[i] == ruleID) return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.DomPermissionManager#saveFilters(int, int[])
	 */
	public void saveFilters(int roleID, int[] filters) throws E5Exception {
		save(roleID, filters, TAG_PERMISSION_FILTER);
	}
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.DomPermissionManager#saveRules(int, int[])
	 */
	public void saveRules(int roleID, int[] rules) throws E5Exception {
		save(roleID, rules, TAG_PERMISSION_RULE);
	}
	
	/**
	 * ����͹������ı��淽ʽһ�������ô˷���
	 * @param roleID
	 * @param idArr
	 * @param tag
	 * @throws E5Exception
	 */
	private void save(int roleID, int[] idArr, String tag) throws E5Exception {
		StringBuffer ids = new StringBuffer();
		if (idArr != null)
		{
			for (int i = 0; i < idArr.length; i++) {
				ids.append(idArr[i]).append(",");
			}
		}
		//�Ȱ�ԭ����Ȩ��ɾ��
		PermissionHelper.getManager().delete(roleID, tag);
		
		//�����µ�Ȩ��
		if (ids.length() > 0)
		{
			Permission p = new Permission(roleID, tag, ids.toString(), 1);
			PermissionHelper.getManager().save(p);
		}
	}
}
