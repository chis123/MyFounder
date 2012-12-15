package com.founder.e5.cat;

import com.founder.e5.context.E5Exception;

/**
 * 分类读取器,用于读取分类和分类类型<br>
 * 获取实例的方法：CatReader reader = (CatReader)com.founder.e5.context.Context.getBean("CatReader");
 * 
 * @created 21-7-2005 16:28:14
 * @author Gong Lijie
 * @version 1.0
 */
public interface CatReader {

	/**
	 * 根据分类类型名称、分类ID、扩展属性类型取得一个分类。
	 * 根据参数中指定的扩展属性类型，决定是否使用别名，以及使用哪一个别名，
	 * 使用别名后将用别名的取值和级联名称替换当前分类的名称和分类级联名称返回，
	 * 若指定的扩展属性类型对该分类而言并不存在，则返回的是主类型属性
	 * 
	 * @param catTypeName    分类类型名
	 * @param catID    分类ID
	 * @param extType    扩展属性类型，null表示主类型
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(String catTypeName, int catID, String extType) 
	throws E5Exception;

	/**
	 * 根据分类类型名称、分类ID取得一个分类
	 * @param catTypeName 分类类型名
	 * @param catID 分类ID
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(String catTypeName, int catID) 
	throws E5Exception;

	/**
	 * 根据分类类型ID、分类ID、扩展属性类型取得一个分类
	 * 根据参数中指定的扩展属性类型，决定是否使用别名，以及使用哪一个别名，
	 * 使用别名后将用别名的取值和级联名称替换当前分类的名称和分类级联名称返回，
	 * 若指定的扩展属性类型对该分类而言并不存在，则返回的是主类型属性
	 * @param catType    分类类型ID
	 * @param catID    分类ID
	 * @param extType    扩展属性类型，null表示主类型
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(int catType, int catID, String extType)
	throws E5Exception;

	/**
	 * 根据分类类型ID、分类ID取得一个分类
	 * @param catType 分类类型ID
	 * @param catID 分类ID
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCat(int catType, int catID)
	throws E5Exception;

	/**
	 * 根据分类类型ID、分类名称取得一个分类
	 * @param catType 分类类型ID
	 * @param catName 分类名称
	 * @return Category
	 * @throws E5Exception
	 */
	public Category getCatByName(int catType, String catName)
	throws E5Exception;
	/**
	 * 取一个分类类型的所有分类（按某个扩展属性类型）
	 * 根据参数中指定的扩展属性类型，决定是否使用别名，以及使用哪一个别名，
	 * 使用别名后将用别名的取值和级联名称替换当前分类的名称和分类级联名称返回，
	 * 若指定的扩展属性类型对该分类而言并不存在，则返回的是主类型属性
	 * @param catTypeName    分类类型名
	 * @param extType    扩展属性类型，null表示主类型
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getCats(String catTypeName, String extType)
	throws E5Exception;

	/**
	 * 取一个分类类型的所有分类
	 * @param catTypeName 分类类型名称
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getCats(String catTypeName)
	throws E5Exception;
	/**
	 * 取一个分类类型的所有分类（按某个扩展属性类型）
	 * 根据参数中指定的扩展属性类型，决定是否使用别名，以及使用哪一个别名，
	 * 使用别名后将用别名的取值和级联名称替换当前分类的名称和分类级联名称返回，
	 * 若指定的扩展属性类型对该分类而言并不存在，则返回的是主类型属性
	 * @param catType 分类类型ID
	 * @param extType    扩展属性类型，null表示主类型
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getCats(int catType, String extType)
	throws E5Exception;

	/**
	 * 取一个分类类型的所有分类
	 * @param catType
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getCats(int catType)
	throws E5Exception;
	/**
	 * 取一个分类的所有子分类（按某个扩展属性类型）
	 * @param catType 分类类型ID
	 * @param catID 当分类ID为0时，取该分类类型下的所有分类
	 * @param extType 扩展属性类型，0表示主类型
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getChildrenCats(int catType, int catID, int extType)
	throws E5Exception;

	/**
	 * 取一个分类的所有子分类。当分类ID为0时，取该分类类型下的所有分类
	 * @param catType 分类类型ID
	 * @param catID 分类ID
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getChildrenCats(int catType, int catID)
	throws E5Exception;
	/**
	 * 取一个分类的所有子分类（按某个扩展属性类型）
	 * @param catType 分类类型ID
	 * @param catID 当分类ID为0时，取该分类类型下的所有分类
	 * @param extType 扩展属性类型，null表示主类型
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getChildrenCats(int catType, int catID, String extType)
	throws E5Exception;
	/**
	 * 取一个分类的所有"直接"子分类
	 * 根据参数中指定的扩展属性类型，决定是否使用别名，以及使用哪一个别名，
	 * 使用别名后将用别名的取值和级联名称替换当前分类的名称和分类级联名称返回，
	 * 若指定的扩展属性类型对该分类而言并不存在，则返回的是主类型属性
	 * @param catTypeName 分类类型名
	 * @param catID 分类ID 当分类ID为0时，取该分类类型下的所有根分类
	 * @param extType    扩展属性类型，null表示主类型
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getSubCats(String catTypeName, int catID, String extType)
	throws E5Exception;

	/**
	 * 取一个分类的所有"直接"子分类
	 * @param catTypeName 分类类型名称
	 * @param catID 当分类ID为0时，取该分类类型下的所有根分类
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getSubCats(String catTypeName, int catID)
	throws E5Exception;
	/**
	 * 取一个分类的所有"直接"子分类
	 * 根据参数中指定的扩展属性类型，决定是否使用别名，以及使用哪一个别名，
	 * 使用别名后将用别名的取值和级联名称替换当前分类的名称和分类级联名称返回，
	 * 若指定的扩展属性类型对该分类而言并不存在，则返回的是主类型属性
	 * @param catType 分类类型ID
	 * @param catID 分类ID 当分类ID为0时，取该分类类型下的所有根分类
	 * @param extType    扩展属性类型，null表示主类型
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getSubCats(int catType, int catID, String extType)
	throws E5Exception;

	/**
	 * 取一个分类的所有"直接"子分类
	 * @param catType 分类类型ID
	 * @param catID 当分类ID为0时，取该分类类型下的所有根分类
	 * @return 分类数组
	 * @throws E5Exception
	 */
	public Category[] getSubCats(int catType, int catID)
	throws E5Exception;
	
	/**
	 * 根据分类类新ID取一个分类类型
	 * 
	 * @param id 分类类型ID
	 * @return 分类类型CatType
	 * @throws E5Exception
	 */
	public CatType getType(int catType)
	throws E5Exception;

	/**
	 * 根据名称取一个分类类型
	 * @param name 分类类型名称
	 * @return 分类类型CatType
	 * @throws E5Exception
	 */
	public CatType getType(String name)
	throws E5Exception;

	/**
	 * 取所有的分类类型
	 * @return 分类类型数组
	 * @throws E5Exception
	 */
	public CatType[] getTypes()
	throws E5Exception;
	
	/**
	 * 根据分类ID数组取得分类,数组长度应小于1000
	 * @param catType 分类类型ID
	 * @param catIDs 分类ID数组,格式为:1,2,3 ...,n
	 * @return 符合catIDs的分类数组,该数组长度可能与catIDs中的id个数不同,有些id可能没查到
	 * @throws E5Exception
	 */
	public Category[] getCat(int catType,String catIDs)
	throws E5Exception;
	

}