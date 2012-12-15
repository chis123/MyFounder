package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * 文档库管理器，供应用层使用
 * @created 11-七月-2005 13:07:29
 * @version 1.0
 */
public interface DocLibReader {

    /**
     * 获取指定文档类型下的文档库
     * 
     * @param docTypeID 文档类型ID
     * @return 如果文档类型不存在或其下无文档库，则返回空数组；如果传入参数为0，则返回所有的文档库
     * @throws E5Exception
     * 
     */
    public DocLib[] getByTypeID(int docTypeID) throws E5Exception;

    /**
     * 获取指定文档类型下的文档库
     * 
     * @param docTypeName 文档类型名称
     * @return 如果文档类型不存在或其下没有文档库，则返回空数组
     * @throws E5Exception
     */
    public DocLib[] getByTypeName(String docTypeName) throws E5Exception;

    /**
     * 获取文档库对应附件表的信息
     * 
     * @param docLibID 文档库ID
     * @return 如果没有，则返回null
     * @throws E5Exception
     */
    public DocLibAdditionals getFlowTableInfo(int docLibID) throws E5Exception;

    /**
     * 获取指定文档类型下的所有文档库id
     * 
     * @param docTypeID 文档类型ID
     * @return 如果不存在，则返回空数组；如果传入参数为0，则返回所有的文档库
     * @throws E5Exception
     */
    public int[] getIDsByTypeID(int docTypeID) throws E5Exception;

    /**
     * 返回指定id的文档库
     * 
     * @param docLibID 文档库ID
     * @return 如果不存在，则返回null
     * @throws E5Exception
     */
    public DocLib get(int docLibID) throws E5Exception;

    /**
     * 获取指定id的所有文档库
     * 
     * @param docLibIDs 文档库ID数组
     * @return 如果某文档库不存在，则对应的索引位置为null
     * @throws E5Exception
     */
    public DocLib[] get(int[] docLibIDs) throws E5Exception;
    
    /**
     * 根据文档库ID，获取文档类型ID
     * @param docLibID 文档库ID
     * @return 如果没有对应的文档库，则返回0
     * @throws E5Exception
     */
    public int getDocTypeID(int docLibID) throws E5Exception;

}