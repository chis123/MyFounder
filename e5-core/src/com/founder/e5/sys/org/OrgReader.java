package com.founder.e5.sys.org;

import com.founder.e5.context.E5Exception;

/**
 * @created 04-七月-2005 14:24:50
 * @version 1.0
 * @updated 11-七月-2005 13:08:36
 */
public interface OrgReader {

	/**
	 * @created 04-七月-2005 14:35:37
	 * @version 1.0
	 */

	/**
	 * 得到所有的组织
	 */
	public Org[] get() throws E5Exception;

	/**
	 * 得到参数指定的组织
	 * @param orgID    orgID
	 * 
	 */
	public Org get(int orgID) throws E5Exception;

	/**
	 * 获得指定组织的所有子组织
	 * @param orgID    组织对象ID
	 * 
	 */
	public Org[] getChildOrgs(int orgID) throws E5Exception;

	/**
	 * 获得指定组织的直接子组织
	 * @param orgID    组织对象ID
	 * 
	 */
	public Org[] getNextChildOrgs(int orgID) throws E5Exception;

	/**
	 * 获得指定组织的父组织
	 * @param orgID    组织对象
	 * 
	 */
	public Org getParentOrg(int orgID) throws E5Exception;

	/**
	 * 获得指定用户的指定类型的组织
	 * @param userID    用户ID
	 * @param typeName    组织类型的名称
	 * 
	 */
	public Org getOrgByType(int userID, String typeName) throws E5Exception;

	/**
	 * 获得指定用户的指定类型的组织
	 * @param userCode    用户代码
	 * @param typeName    组织类型的名称
	 * 
	 */
	public Org getOrgByType(String userCode, String typeName)
			throws E5Exception;

	/**
	 * 获得指定组织下的所有角色
	 * @param orgID    组织ID
	 * 
	 */
	public Role[] getRolesByOrg(int orgID) throws E5Exception;

	/**
	 * 得到给定组织下所有的角色
	 * 
	 * @param orgID 组织ID
	 * @return 角色数组
	 * @throws E5Exception
	 */
	public Role[] getRoles(int orgID) throws E5Exception;

	//王朝阳增加，实现机构查询 2006-3-8
	/**
	 * 根据机构名获取对应的机构
	 * @param orgName
	 * @return
	 * @throws E5Exception
	 */
	public Org[] getOrgsByName(String orgName) throws E5Exception;

	//王朝阳增加，实现机构查询 2006-3-8
	/**
	 * 根据机构名中的某些词获取对应的机构
	 * @param orgName
	 * @return
	 * @throws E5Exception
	 */
	public Org[] getOrgsIncludeName(String orgName) throws E5Exception;

	/**
	 * 获得指定机构的所有缺省文件夹
	 * @param orgID 机构ID
	 */
	public DefaultFolder[] getDefaultFolders(int orgID) throws E5Exception;

	/**
	 * 获得指定机构ID，指定文档类型下的缺省文件夹
	 * @param orgID 机构ID
	 * @param docTypeID 文档类型ID
	 */
	public DefaultFolder getDefaultFolder(int orgID, int docTypeID)
	throws E5Exception;

	/**
	 * 获得所有缺省文件夹
	 */
	public DefaultFolder[] getDefaultFolders() throws E5Exception;
}