package com.founder.e5.dom;

import org.hibernate.Session;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-����-2005 15:36:35
 */
public interface ViewManager extends ViewReader {

    /**
     * ������ͼ
     * 
     * @param view
     *            id���ڷ���������
     * @throws E5Exception
     */
    public int create(View view,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;

    /**
     * ɾ����ͼ�������ͼ�����ڣ���ʲôҲ����
     * 
     * @param viewID
     * @throws E5Exception
     */
    public void delete(int viewID) throws E5Exception;

    public void create(View view,Session session) throws E5Exception;
    
    void delete(int viewID, Session ss) throws E5Exception;
    
    public void update(View view,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;
    
}