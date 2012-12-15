package com.founder.e5.dom;

import com.founder.e5.context.Cache;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.facade.ContextFacade;
import com.founder.e5.dom.util.DomUtils;

/**
 * depends on context.jar
 * 
 * @version 1.0
 * @created 11-ÆßÔÂ-2005 15:57:28
 */
public class DataSourceConfigCache implements Cache {

    /**
     * key = configName
     * 
     * value = dsID
     */
    private DataSourceConfig[] dsConfigs;

    public int get(String dsConfigName) {
        
        for( int i = 0; i < dsConfigs.length; i++){
            
            if(dsConfigs[i].getDsName().equalsIgnoreCase(dsConfigName))
                return dsConfigs[i].getDsID();
            
        }
        
        return -1;
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#refresh()
     */
    public void refresh() throws E5Exception {

        DataSourceConfigManager mgr = (DataSourceConfigManager) ContextFacade
                .getBean(DataSourceConfigManager.class);

        this.dsConfigs  = mgr.getAllDSConfigs();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.context.Cache#reset()
     */
    public void reset() {
        this.dsConfigs = DomUtils.EMPTY_DSCONFIG_ARRAY;
    }

    public DataSourceConfig[] getAllDSConfigs() {
         return this.dsConfigs;
    }

}