package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * 分类扩展属性读取器
 * 包括扩展属性类型的读取<br>
 * 扩展属性在系统中用于分类的别名。
 * 
 * @created 21-7-2005 16:20:07
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatExtReader {

	/**
	 * 根据分类类型ID、分类ID、扩展属性类型名称,得到该分类的扩展属性
	 * 
	 * @param catType 分类类型ID
	 * @param catID   分类ID
	 * @param extType 给出扩展属性类型名，需要先查找扩展属性类型ID
	 * @return CatExt 
	 * @throws E5Exception
	 */
	public CatExt getExt(int catType, int catID, String extType) 
	throws E5Exception;

	/**
	 * 根据分类类型ID、分类ID、分类扩展属性类型ID,得到该分类的扩展属性
	 * 
	 * @param catType 分类类型ID
	 * @param catID   分类ID
	 * @param extType 给出扩展属性类型ID
	 * @return CatExt
	 * @throws E5Exception
	 */
	public CatExt getExt(int catType, int catID, int extType) 
	throws E5Exception;

	/**
	 * 根据分类类型ID、分类ID取得分类的所有扩展属性
	 * 
	 * @param catType 分类类型ID
	 * @param catID 分类ID
	 * @return 扩展属性数组
	 * @throws E5Exception
	 */
	public CatExt[] getExts(int catType, int catID) 
	throws E5Exception;

	/**
	 * 取一个分类类型的所有分类的某种扩展属性
	 * @param catType 分类类型ID
	 * @param extType 扩展属性名，需要先查找一次扩展属性
	 * @return 扩展属性数组 
	 * @throws E5Exception
	 */
	public CatExt[] getAllExts(int catType, String extType)
	throws E5Exception;

	/**
	 * 取一个分类类型的所有分类的某种扩展属性
	 * @param catType 分类类型ID
	 * @param extType 扩展属性ID
	 * @return 扩展属性取值数组
	 * @throws E5Exception
	 */
	public CatExt[] getAllExts(int catType, int extType)
	throws E5Exception;

	/**
	 * 取一个分类的所有直接子分类的某一种扩展属性
	 * @param catType 分类类型ID
	 * @param catID   分类ID
	 * @param extType 给出扩展属性类型名，需要先查找一次扩展属性ID
	 * @return 扩展属性数组
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int catID, String extType) 
	throws E5Exception;

	/**
	 * 取一个分类的所有直接子分类的某一种扩展属性
	 * @param catType 分类类型ID
	 * @param catID   分类ID
	 * @param extType 给出扩展属性类型ID
	 * @return 扩展属性数组
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int catID, int extType) 
	throws E5Exception;

	/**
	 * 取某些分类的所有直接子分类的某一种扩展属性
	 * 返回的数组按分类ID升序进行排序
	 * @param catType 分类类型ID
	 * @param catIDs  分类ID数组
	 * @param extType 给出扩展属性类型名，需要先查找一次扩展属性ID
	 * @return 扩展属性数组
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, String extType) 
	throws E5Exception;

	/**
	 * 取某些分类的所有直接子分类的某一种扩展属性
	 * 返回的数组按分类ID升序进行排序
	 * @param catType 分类类型ID
	 * @param catIDs  分类ID数组
	 * @param extType 给出扩展属性类型ID
	 * @return 扩展属性数组
	 * @throws E5Exception
	 */
	public CatExt[] getSubExt(int catType, int[] catIDs, int extType) 
	throws E5Exception;

	/**
	 * 按名称取一个扩展属性类型
	 * @param extType 扩展属性类型名称
	 * @return 分类扩展属性类型
	 * @throws E5Exception
	 */
	public CatExtType getExtType(String extType)
	throws E5Exception;

	/**
	 * 按ID获取扩展属性类型
	 * @param extType 扩展属性类型ID
	 * @return 分类扩展属性类型 
	 * @throws E5Exception
	 */
	public CatExtType getExtType(int extType)
	throws E5Exception;
	
	/**
	 * 取得所有分类扩展属性类型
	 * @return 分类扩展属性类型数组
	 * @throws E5Exception
	 */
	public CatExtType[] getExtTypes()
	throws E5Exception;

	/**
	 * 根据分类类型，获取当前分类所有支持的属性类型
	 *
	 * @param catType
	 * @return 分类扩展属性类型数组
	 * @throws E5Exception
	 */
	public CatExtType[] getExtTypes(int catType)
	throws E5Exception;
}