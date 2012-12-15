package com.founder.e5.dom;

import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 2005-7-21 14:19:15
 * @author Zhang Kaifeng
 * @version
 */
public interface ViewReader {

    /**
     * ��ȡָ��ID����ͼ
     * 
     * @param viewID ��ͼID
     * @return �����ͼ�����ڣ��򷵻�null
     * @throws E5Exception
     */
    public View get(int viewID) throws E5Exception;

    /**
     * ��ȡָ����ͼ�µ����й�������˳���
     * 
     * @param viewID ��ͼID
     * @return ����޹����򷵻ؿ�����
     * @throws E5Exception
     */
    public Rule[] getRules(int viewID) throws E5Exception;

    /**
     * ��ȡָ����ͼ�µĹ�����������˳���
     * 
     * @param viewID ��ͼID
     * @return �����ͼ���޹��������򷵿տ�����
     * @throws E5Exception
     */
    public List getFilters(int viewID) throws E5Exception;

    /**
     * ��ȡָ����ͼ�µ����й�����id������˳���
     * 
     * @param viewID����ͼID
     * @return �����ͼ���޹��������򷵻ؿ�����
     * @throws E5Exception
     */
    public List getFilterIDs(int viewID) throws E5Exception;

    /**
     * ��ȡָ����ͼ�µ����й���id������˳���
     * 
     * @param viewID ��ͼID
     * @return �����ͼ���޹����򷵻ؿ�����
     * @throws E5Exception
     */
    public int[] getRuleIDs(int viewID) throws E5Exception;

}