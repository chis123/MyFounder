package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-����-2005 15:27:13
 */
public interface RuleManager extends RuleReader {

    /**
     * ����Rule����
     * 
     * @param rule
     *            Լ����������һ��ʵ�ʵ�Rule���󣬷�����׳��쳣��id�������ã���������rule���ƣ��������ĵ�����ID��
     * @throws E5Exception
     */
    public void create(Rule rule) throws E5Exception;

    /**
     * ɾ��һ��rule����
     * 
     * @param ruleID
     * @throws E5Exception
     */
    public void delete(int ruleID) throws E5Exception;

    /**
     * ����һ��rule����
     * 
     * @param rule
     *            rule��������ָ���Ѿ����ڵ�id�����������²���
     * @throws E5Exception
     */
    public void update(Rule rule) throws E5Exception;
    
    /**
     * ��ȡָ���Ĺ����������Щ�ļ�����
     * 2006-3-2 13:38:17
     * @author zhang_kaifeng
     * @param ruleID
     * @return
     * @throws E5Exception
     */
    public FolderView[] getFolderViews(int ruleID) throws E5Exception;

}