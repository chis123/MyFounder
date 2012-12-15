package com.founder.e5.dom;


import com.founder.e5.dom.facade.CacheManagerFacade;

/**
 * @created 2005-7-20 10:04:35
 * @author Zhang Kaifeng
 * @version
 */
class DataSourceConfigReaderImpl implements DataSourceConfigReader {

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DataSourceConfigReader#get(java.lang.String)
     */
    public int get(String dsConfigName) {
        return this.getCache().get(dsConfigName);

    }

    private DataSourceConfigCache getCache() {
        return (DataSourceConfigCache) CacheManagerFacade
                .find(DataSourceConfigCache.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DataSourceConfigReader#getAll()
     */
    public DataSourceConfig[] getAllDSConfigs() {
        return this.getCache().getAllDSConfigs();
    }

}