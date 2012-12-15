package com.founder.e5.sys.org;
import com.founder.e5.context.E5Exception;
import com.founder.e5.sys.org.Org;
import com.founder.e5.sys.org.OrgReader;

/**
 * @created 04-七月-2005 14:35:37
 * @version 1.0
 * @updated 11-七月-2005 12:40:35
 */
public interface OrgManager extends OrgReader {

	/**
	 * 创建组织
	 * @param org    组织对象
	 * 
	 */
	public void create(Org org) throws E5Exception;

	/**
	 * 修改组织属性
	 * @param org    组织对象
	 * 
	 */
	public void update(Org org) throws E5Exception;

	/**
	 * 删除组织连同它的所有子组织
	 * 
	 * @param orgID    组织对象的ID
	 * @param cascade  是否删除子组织 true 删除， false 不删除
	 */
	public void delete(int orgID, boolean cascade) throws E5Exception;

	/**
	 * 移动当前组织到其它组织下
	 * @param srcOrgID    源组织对象ID
	 * @param destOrgID    目标组织对象ID
	 * @param cascade   是否带上子组织，true  带上所有的子组织，false 不带上
	 */
	public void move(int srcOrgID, int destOrgID, boolean cascade) throws E5Exception;

	/**
	 * 对数组指定的组织进行排序
	 * 
	 * @param orgIDs    需排序的组织ID
	 */
	public void sortOrg(int[] orgIDs) throws E5Exception;

	/**
	 * 创建缺省文件夹
	 * @param defaultFolder    创建缺省文件夹
	 * @throws E5Exception
	 * 
	 */
	public void create(DefaultFolder defaultFolder) throws E5Exception;

	/**
	 * 修改缺省文件夹
	 * @param defaultFolder    修改缺省文件夹
	 * 
	 */
	public void update(DefaultFolder defaultFolder) throws E5Exception;

	/**
	 * 删除缺省文件夹
	 * @param defaultFolder    缺省文件夹对象
	 * 
	 */
	public void delete(DefaultFolder defaultFolder) throws E5Exception;

}