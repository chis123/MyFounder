package com.founder.e5.dom;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.founder.e5.context.BaseDAO;
import com.founder.e5.context.DAOHelper;
import com.founder.e5.context.E5Exception;
import com.founder.e5.dom.util.DomUtils;

/**
 * ±æ¿‡“¿¿µcontext∞¸
 * 
 * @created 2005-7-20 9:34:40
 * @author Zhang Kaifeng
 * @version
 */
class DataSourceConfigManagerImpl implements DataSourceConfigManager {

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DataSourceConfigManager#create(com.founder.e5.dom.DataSourceConfig)
     */
    public void create(DataSourceConfig dsConfig) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        try{
            dao.save(dsConfig);
        } catch (HibernateException e){
            throw new E5Exception(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DataSourceConfigManager#delete(java.lang.String)
     */
    public void delete(String dsConfigName) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        try{

            dao.delete("delete from DataSourceConfig as c where c.dsName = :dsname",
                       dsConfigName, Hibernate.STRING);

        } catch (HibernateException e){
            throw new E5Exception(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DataSourceConfigReader#get(java.lang.String)
     */
    public int get(String dsConfigName) throws E5Exception {

        DataSourceConfig config = this.getConfig(dsConfigName);
        if (null != config)
            return config.getDsID();
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DataSourceConfigReader#getAll()
     */
    public DataSourceConfig[] getAllDSConfigs() throws E5Exception {
        
        List configList = DAOHelper.find("from DataSourceConfig as c");
        
        return (DataSourceConfig[]) configList.toArray(DomUtils.EMPTY_DSCONFIG_ARRAY);
        
    }

    private DataSourceConfig getConfig(String dsConfigName) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        DataSourceConfig config = null;

        try{

            config = (DataSourceConfig) dao.get(DataSourceConfig.class,
                                                dsConfigName);

        } catch (HibernateException e){
            throw new E5Exception(e);
        }

        return config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.founder.e5.dom.DataSourceConfigManager#update(com.founder.e5.dom.DataSourceConfig)
     */
    public void update(DataSourceConfig dsConfig) throws E5Exception {

        BaseDAO dao = new BaseDAO();
        Session session = null;
        
        try{
            session = dao.getSession();
            
            String dsName = dsConfig.getDsName();
            DataSourceConfig oriConfig = (DataSourceConfig) session.get(DataSourceConfig.class, dsName);
            
            oriConfig.setDsID(dsConfig.getDsID());
            session.save(oriConfig);
            
            session.flush();
            
        } catch (HibernateException e){
            
            throw new E5Exception(e);
            
        }finally{
            
            dao.closeSession(session);
        }

    }
}