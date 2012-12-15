package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-七月-2005 15:41:10
 */
public interface DataSourceConfigManager extends DataSourceConfigReader {

    /**
     * 创建DSCONFIG记录
     * 
     * @param dsConfig
     *            name和dsid都必须事先设置好
     * @throws E5Exception
     *             如果已经存在相同的名字，则抛出异常
     */
    public void create(DataSourceConfig dsConfig) throws E5Exception;

    /**
     * 删除一个DSCONFIG
     * 
     * @param dsConfigName
     *            如果config不存在，则什么都不做
     * @throws E5Exception
     */
    public void delete(String dsConfigName) throws E5Exception;

    /**
     * 更新一个DSCONFIG。只可更新dsid
     * 
     * @param dsConfig
     * @throws E5Exception
     *             如果config不存在，则抛出异常
     */
    public void update(DataSourceConfig dsConfig) throws E5Exception;

}