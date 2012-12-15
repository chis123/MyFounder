package com.founder.e5.permission;

import com.founder.e5.commons.StringUtils;
import com.founder.e5.context.E5Exception;

/**
 * 规则和过滤器的权限读取器实现类
 * @created 2008-4-21
 * @author Gong Lijie
 * @version 1.0
 */
class DomPermissionReaderImpl implements DomPermissionReader {
	/* (non-Javadoc)
	 * @see com.founder.e5.permission.DomPermissionReader#getRightlessFilters(int)
	 */
	public int[] getRightlessFilters(int roleID) throws E5Exception {
		int[] pIDs = null;
		
		Permission[] pArr = PermissionHelper.getReader().getPermissions(roleID, TAG_PERMISSION_FILTER);
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
		//过滤器权限的权限记录比较特殊，缺省是有权限的，所以0表示有权限，1表示没有权限
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
		
		Permission[] pArr = PermissionHelper.getReader().getPermissions(roleID, TAG_PERMISSION_RULE);
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
}
