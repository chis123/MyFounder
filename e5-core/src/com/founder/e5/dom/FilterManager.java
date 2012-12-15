package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @version 1.0
 * @created 11-����-2005 15:30:44
 */
public interface FilterManager extends FilterReader {

    /**
     * ����Filter��¼
     * 
     * @param filter
     *            �������´�����Filter���󣬷����׳��쳣
     * @throws E5Exception
     */
    public void create(Filter filter) throws E5Exception;

    /**
     * ɾ��ĳһ������
     * 
     * @param filterID
     *            ����ù����������ڣ���ʲôҲ������
     * @throws E5Exception
     */
    public void delete(int filterID) throws E5Exception;

    /**
     * ����ĳһ������
     * 
     * @param filter
     * @throws E5Exception
     */
    public void update(Filter filter) throws E5Exception;
    
    /**
     * �鿴��������������Щ�ļ�����
     * 2006-3-2 9:52:36
     * @author zhang_kaifeng
     * @param filterID
     * @return
     * @throws E5Exception
     */
    public FolderView[] getFolderViews(int filterID) throws E5Exception;

}