package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * 分类管理器
 * @created 21-7-2005 16:28:18
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatManager extends CatReader {

	/**
	 * 创建一个新的分类类型
	 * @param catType
	 */
	public void createType(CatType catType)
	throws E5Exception;

	/**
	 * 修改一个分类类型
	 * @param catType
	 */
	public void updateType(CatType catType)
	throws E5Exception;

	/**
	 * 删除一个分类类型
	 * 同时把该类型下的所有分类都删除
	 * 不删除分类表
	 * @param id 分类类型ID
	 */
	public void deleteType(int id)
	throws E5Exception;

	/**
	 * 按名字查找分类
	 * 支持模糊查询
	 * 该功能在分类管理中使用，便于快速定位一个分类
	 * @param catType 分类类型ID
	 * @param catName 分类名
	 */
	public Category[] getCatsByName(int catType, String catName)
	throws E5Exception;

	/**
	 * 创建分类
	 * 只创建主分类信息，扩展属性信息要单独保存
	 * @param cat
	 */
	public void createCat(Category cat)
	throws E5Exception;

	/**
	 * <p>修改一个分类的基本属性</p>
	 * <p>注意这里的修改并不考虑层次变化，也就是说，不考虑parentID、level属性的变化
	 * <BR>层次变化是单独进行的动作，需要调用<code>move</code>方法</p>
	 * <BR>当分类的名称发生变化时，它以及它所有子分类的级联名称都要发生变化
	 * @param cat
	 */
	public void updateCat(Category cat)
	throws E5Exception;

	/**
	 * 把一个分类移动到另一个分类之下
	 * 分类的层次可能发生变化
	 * 当分类层次变化时，它的所有子分类的层次，以及级联名称也同时发生变化
	 * @param catType 分类类型ID
	 * @param cat 需要移动的分类ID
	 * @param catID 目标根分类ID
	 * @throws E5Exception
	 */
	public void move(int catType, int srcCatID, int destCatID)
	throws E5Exception;
	
	/**
	 * 把对父分类的某些属性的修改传递到子分类
	 * @param cat 包含了已修改属性的父分类
	 * @param fields 指定需要进行同步的属性，参见Category类中的常量定义
	 * @throws E5Exception 
	 */
	public void updateTransfer(Category cat, int[] fields) 
	throws E5Exception;

	/**
	 * 删除一个分类
	 * 同时把所有的子分类删除
	 * @param catType 分类类型ID
	 * @param catID 分类ID
	 */
	public void deleteCat(int catType, int catID)
	throws E5Exception;

	/**
	 * 删除一个分类
	 * 同时把所有的子分类删除
	 * @param catTypeName 分类类型名
	 * @param catID 分类ID
	 */
	public void deleteCat(String catTypeName, int catID)
	throws E5Exception;
	
	/**
	 * 恢复一个已删除的分类
	 * @param catType
	 * @param catID
	 * @throws E5Exception
	 */
	public void restoreCat(int catType, int catID)
	throws E5Exception;
	/**
	 * 取所有已经删除的分类
	 * @param catType 分类类型ID
	 * @return 已删分类的数组
	 * @throws E5Exception
	 */
	public Category[] getAllDeleted(int catType)
	throws E5Exception;
	
	
	/**
	 * 取得分类的子孙节点（包含已经删除的分类）
	 * 
	 * @param catType - 分类类型
	 * @param catID - 分类ID
	 * @return 分类数组
	 */
	public Category[] getChildrenCatsIncludeDeleted(int catType,int catID,int extType)
	throws E5Exception;
	
}