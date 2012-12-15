package com.founder.e5.dom;


import com.founder.e5.context.E5Exception;

/**
 * 依赖context包
 * 
 * @version 1.0
 * @created 11-七月-2005 15:40:59
 */
public interface DataSourceConfigReader {

    /**
     * 根据数据源配置名获得数据源id
     * 
     * @param dsConfigName 数据源配置的名称
     * @return 如果数据源配置不存在，则返回-1
     * @throws E5Exception
     */
    public int get(String dsConfigName) throws E5Exception;

    /**
     * 获取所有的数据源配置的数组
     * 
     * @return 如果没有，则返回空数组
     * @throws E5Exception
     */
    public DataSourceConfig[] getAllDSConfigs() throws E5Exception;

}