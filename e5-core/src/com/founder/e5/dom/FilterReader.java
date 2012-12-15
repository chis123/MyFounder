package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-����-2005 15:29:37
 */
public interface FilterReader {

    /**
     * ����ĳ�ĵ������µ����е�Filter����
     * 
     * @param docTypeID �ĵ�����ID
     * @return ����ĵ����Ͳ����ڣ����߸��ĵ������²�����Filter���򷵻ؿ�����
     * @throws E5Exception
     */
    public Filter[] getByTypeID(int docTypeID) throws E5Exception;

    /**
     * ��ȡָ��id�Ĺ�����
     * 
     * @param filterID ������ID
     * @return ���ָ���Ĺ����������ڣ��򷵻�null
     * @throws E5Exception
     */
    public Filter get(int filterID) throws E5Exception;

}