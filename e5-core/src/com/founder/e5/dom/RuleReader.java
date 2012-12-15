package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-����-2005 15:27:01
 */
public interface RuleReader {

    /**
     * ����ruleid��ȡRule����
     * 
     * @param ruleID ����ID
     * @return ���û�ж�Ӧ��Rule���򷵻�null
     * @throws E5Exception
     */
    public Rule get(int ruleID) throws E5Exception;

    /**
     * �����ĵ�����id����ȡ�������е�rule����
     * 
     * @param docTypeID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public Rule[] getByDoctypeID(int docTypeID) throws E5Exception;

}