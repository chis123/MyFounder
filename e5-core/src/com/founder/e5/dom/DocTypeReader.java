package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * 文档类型管理器，提供给应用层使用 depends on context.jar
 * 
 * @version 1.0
 * @updated 11-七月-2005 13:59:28
 */
public interface DocTypeReader {

    /**
     * 根据文档类型ID获取文档类型对象
     * 
     * @param docTypeID 文档类型ID
     * @return 如果不存在，则返回null
     * @throws E5Exception
     */
    public DocType get(int docTypeID) throws E5Exception;

    /**
     * 根据文档类型（唯一）名称获取文档类型对象
     * 
     * @param docTypeName 文档类型名称
     * @return 如果不存在，则返回null
     * @throws E5Exception
     */
    public DocType get(String docTypeName) throws E5Exception;

    /**
     * 根据应用子系统的ID获取所有文档类型，默认按照文档类型ID正序排列
     * 
     * @param appID 子系统ID。如果为0，则返回所有的文档类型
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DocType[] getTypes(int appID) throws E5Exception;

    /**
     * 获取指定文档类型所属子应用的id
     * 
     * @param docTypeID 文档类型ID
     * @return 如果不存在，则返回0
     * @throws E5Exception
     */
    public int getAppID(int docTypeID) throws E5Exception;

    /**
     * 根据子系统ID，获取所有的文档类型ID，默认按照文档类型ID正序排列
     * 
     * @param appID 子系统ID
     * @return 如果不存在，则返回空数组；如果参数为0，则返回所有的文档类型ID
     * @throws E5Exception
     */
    public int[] getIDs(int appID) throws E5Exception;

    /**
     * 获取指定id的字段
     * 
     * @param fieldID 文档类型字段ID
     * @return 如果没有则返回null
     * @throws E5Exception
     */
    public DocTypeField getField(int fieldID) throws E5Exception;

    /**
     * 获取指定文档类型下的所有字段
     * 返回的所有字段默认按照id排序，正序
     * 
     * @param docTypeID 文档类型ID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DocTypeField[] getFields(int docTypeID) throws E5Exception;
    
    /**
     * 获取指定文档类型下的所有的字段，按照指定字段的顺序
     * 
     * @param docTypeID 文档类型ID
     * @param fieldName 排序的字段名称
     * @param order 正序为空字符串，逆序为desc
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsOrderBy(int docTypeID ,String fieldName, String order) throws E5Exception;

    /**
     * 获取指定字段的名字
     * 
     * @param fieldID
     * @return 如果没有则返回null
     * @throws E5Exception
     */
    public String getFieldName(int fieldID) throws E5Exception;

    /**
     * 获取指定类型的所有扩展字段，包括应用级字段和用户级字段
     * 默认按照id排序，排正序
     * 
     * @param docTypeID 文档类型ID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsExt(int docTypeID) throws E5Exception;
    
    /**
     * 获取指定类型的所有应用级字段，
     * 默认按照id排序，排正序
     * 
     * @param docTypeID 文档类型ID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsApp(int docTypeID) throws E5Exception;
    
    /**
     * 获取指定类型的所有用户级字段，
     * 默认按照id排序，排正序
     * 
     * @param docTypeID 文档类型ID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DocTypeField[] getFieldsUser(int docTypeID) throws E5Exception;
    
    /**
     * 获取指定类型的所有系统字段，默认按照id排正序
     * @param docTypeID 文档类型ID
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DocTypeField[] getSysFields(int docTypeID) throws E5Exception;
    
    /**
     * 根据文档类型ID，类型字段的Code，获得字段对象
     * 这里没有判断字段是否已经被删除，因为创建文档类型字段时，只要存在同名的，则不能创建
     * @param docTypeID 文档类型ID
     * @param columnCode 字段英文名
     * @return 如果没有，则返回null
     * @throws E5Exception
     */
    public DocTypeField getField(int docTypeID,String columnCode)throws E5Exception;

}