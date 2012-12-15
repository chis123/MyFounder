package com.founder.e5.permission;

import com.founder.e5.context.E5Exception;

/**
 * 文档管理部分的权限管理器。
 * 如规则权限管理、过滤器权限管理等。
 * @created 2008-4-21
 * @author Gong Lijie
 * @version 1.0
 */
public interface DomPermissionManager extends DomPermissionReader{
	/**
	 * 保存过滤器权限。
	 * 
	 * @param roleID
	 * @param filters 过滤器缺省有权限，这里参数表示的是没有权限的过滤器ID数组
	 * @throws E5Exception
	 */
	void saveFilters(int roleID, int[] filters) throws E5Exception;
	
	/**
	 * 保存规则权限
	 * @param roleID
	 * @param rules 规则ID数组
	 * @throws E5Exception
	 */
	void saveRules(int roleID, int[] rules) throws E5Exception;
}
