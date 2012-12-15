package com.founder.e5.dom;


import com.founder.e5.context.E5Exception;

/**
 * ����context��
 * 
 * @version 1.0
 * @created 11-����-2005 15:40:59
 */
public interface DataSourceConfigReader {

    /**
     * ��������Դ�������������Դid
     * 
     * @param dsConfigName ����Դ���õ�����
     * @return �������Դ���ò����ڣ��򷵻�-1
     * @throws E5Exception
     */
    public int get(String dsConfigName) throws E5Exception;

    /**
     * ��ȡ���е�����Դ���õ�����
     * 
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public DataSourceConfig[] getAllDSConfigs() throws E5Exception;

}