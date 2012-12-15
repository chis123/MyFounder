package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * 分类扩展属性管理器
 * 包括扩展属性类型的管理
 * @created 21-7-2005 16:20:02
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatExtManager extends CatExtReader
{
	/**
	 * 分类的多个扩展属性只能一次性保存
	 * 保存时，先清除该分类的所有旧扩展属性，然后保存新的扩展属性
	 * 若传入的扩展属性数组是null，则只清除旧属性
	 * @param catType 分类类型
	 * @param catID 分类ID
	 * @param catExtArray 扩展属性数组；注意这些扩展属性必须都属于同一个分类
	 */
	public void saveExt(int catType, int catID, CatExt[] catExtArray)
	throws E5Exception;

	/**
	 * 删除一个分类时把分类的所有扩展属性同时删除
	 * 
	 * 分类的扩展属性是整体保存的，
	 * 没有针对某一个扩展属性进行删除的方法
	 * 
	 * @param catType
	 * @param catID
	 */
	public void deleteCat(int catType, int catID)
	throws E5Exception;

	/**
	 * 创建扩展属性类型
	 * 
	 * @param extType
	 */
	public void createExtType(CatExtType extType)
	throws E5Exception;

	/**
	 * 删除一个扩展属性类型
	 * 同时删除该类型下的所有扩展属性
	 * @param extType
	 */
	public void deleteExtType(int extType)
	throws E5Exception;

	/**
	 * 修改扩展属性类型
	 * 
	 * @param extType
	 */
	public void updateExtType(CatExtType extType)
	throws E5Exception;

}