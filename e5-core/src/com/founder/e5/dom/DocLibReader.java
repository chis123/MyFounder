package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * �ĵ������������Ӧ�ò�ʹ��
 * @created 11-����-2005 13:07:29
 * @version 1.0
 */
public interface DocLibReader {

    /**
     * ��ȡָ���ĵ������µ��ĵ���
     * 
     * @param docTypeID �ĵ�����ID
     * @return ����ĵ����Ͳ����ڻ��������ĵ��⣬�򷵻ؿ����飻����������Ϊ0���򷵻����е��ĵ���
     * @throws E5Exception
     * 
     */
    public DocLib[] getByTypeID(int docTypeID) throws E5Exception;

    /**
     * ��ȡָ���ĵ������µ��ĵ���
     * 
     * @param docTypeName �ĵ���������
     * @return ����ĵ����Ͳ����ڻ�����û���ĵ��⣬�򷵻ؿ�����
     * @throws E5Exception
     */
    public DocLib[] getByTypeName(String docTypeName) throws E5Exception;

    /**
     * ��ȡ�ĵ����Ӧ���������Ϣ
     * 
     * @param docLibID �ĵ���ID
     * @return ���û�У��򷵻�null
     * @throws E5Exception
     */
    public DocLibAdditionals getFlowTableInfo(int docLibID) throws E5Exception;

    /**
     * ��ȡָ���ĵ������µ������ĵ���id
     * 
     * @param docTypeID �ĵ�����ID
     * @return ��������ڣ��򷵻ؿ����飻����������Ϊ0���򷵻����е��ĵ���
     * @throws E5Exception
     */
    public int[] getIDsByTypeID(int docTypeID) throws E5Exception;

    /**
     * ����ָ��id���ĵ���
     * 
     * @param docLibID �ĵ���ID
     * @return ��������ڣ��򷵻�null
     * @throws E5Exception
     */
    public DocLib get(int docLibID) throws E5Exception;

    /**
     * ��ȡָ��id�������ĵ���
     * 
     * @param docLibIDs �ĵ���ID����
     * @return ���ĳ�ĵ��ⲻ���ڣ����Ӧ������λ��Ϊnull
     * @throws E5Exception
     */
    public DocLib[] get(int[] docLibIDs) throws E5Exception;
    
    /**
     * �����ĵ���ID����ȡ�ĵ�����ID
     * @param docLibID �ĵ���ID
     * @return ���û�ж�Ӧ���ĵ��⣬�򷵻�0
     * @throws E5Exception
     */
    public int getDocTypeID(int docLibID) throws E5Exception;

}