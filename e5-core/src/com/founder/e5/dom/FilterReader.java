package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-七月-2005 15:29:37
 */
public interface FilterReader {

    /**
     * 返回某文档类型下的所有的Filter对象
     * 
     * @param docTypeID 文档类型ID
     * @return 如果文档类型不存在，或者该文档类型下不存在Filter，则返回空数组
     * @throws E5Exception
     */
    public Filter[] getByTypeID(int docTypeID) throws E5Exception;

    /**
     * 获取指定id的过滤器
     * 
     * @param filterID 过滤器ID
     * @return 如果指定的过滤器不存在，则返回null
     * @throws E5Exception
     */
    public Filter get(int filterID) throws E5Exception;

}