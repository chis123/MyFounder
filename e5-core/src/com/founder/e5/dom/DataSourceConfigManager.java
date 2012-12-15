package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-����-2005 15:41:10
 */
public interface DataSourceConfigManager extends DataSourceConfigReader {

    /**
     * ����DSCONFIG��¼
     * 
     * @param dsConfig
     *            name��dsid�������������ú�
     * @throws E5Exception
     *             ����Ѿ�������ͬ�����֣����׳��쳣
     */
    public void create(DataSourceConfig dsConfig) throws E5Exception;

    /**
     * ɾ��һ��DSCONFIG
     * 
     * @param dsConfigName
     *            ���config�����ڣ���ʲô������
     * @throws E5Exception
     */
    public void delete(String dsConfigName) throws E5Exception;

    /**
     * ����һ��DSCONFIG��ֻ�ɸ���dsid
     * 
     * @param dsConfig
     * @throws E5Exception
     *             ���config�����ڣ����׳��쳣
     */
    public void update(DataSourceConfig dsConfig) throws E5Exception;

}