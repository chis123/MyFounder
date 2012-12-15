package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * 文档管理部分的权限读取器。
 * 如过滤器权限读取、规则权限读取。
 * @created 2008-4-21
 * @author Gong Lijie
 * @version 1.0
 */
public interface DomPermissionReader {

	String TAG_PERMISSION_RULE = "RulePermission";
	String TAG_PERMISSION_FILTER = "FilterPermission";

	/**
	 * 取某角色没有权限的过滤器ID数组。
	 * 过滤器缺省是有权限的，因此权限表中记录的是没有权限的过滤器ID。
	 * @param roleID
	 * @return 没有权限的过滤器ID数组
	 * @throws E5Exception
	 */
	int[] getRightlessFilters(int roleID) throws E5Exception;
	/**
	 * 判断一个角色对某过滤器是否有权限
	 * @param roleID
	 * @param filterID
	 * @return
	 * @throws E5Exception
	 */
	boolean hasFilterRight(int roleID, int filterID) throws E5Exception;
	
	/**
	 * 取某角色有权限的规则ID数组
	 * @param roleID
	 * @return 有权限的规则ID数组
	 * @throws E5Exception
	 */
	int[] getRules(int roleID) throws E5Exception;
	
	/**
	 * 判断一个角色对某规则是否有权限
	 * @param roleID
	 * @param ruleID
	 * @return
	 * @throws E5Exception
	 */
	boolean hasRuleRight(int roleID, int ruleID) throws E5Exception;
}
