package com.founder.e5.sys.org;
import com.founder.e5.sys.org.OrgType;

/**
 * @created 04-七月-2005 14:40:11
 * @version 1.0
 * @updated 11-七月-2005 12:41:44
 */
public interface OrgTypeReader {

	/**
	 * 通过指定的组织类型ID获得组织的类型名称
	 * @param typeID    组织对象的类型ID
	 * 
	 */
	public String getTypeName(String typeID);

	/**
	 * 通过组织的类型名称获得组织的类型ID
	 * @param typeName    组织类型名称
	 * 
	 */
	public String getTypeID(String typeName);

	/**
	 * 获得所有的组织类型ID和类型名称的映射对象
	 */
	public OrgType[] get();

}