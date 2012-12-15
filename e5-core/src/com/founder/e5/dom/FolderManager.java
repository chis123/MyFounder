package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

import org.hibernate.Session;

/**
 * @version 1.0
 * @created 11-����-2005 15:23:47
 */
public interface FolderManager extends FolderReader {

    /**
     * ����һ���ļ���
     * 
     * @param folder
     *            ��������new�����Ķ��󣬳���folderID���������Ա���
     * @throws E5Exception
     */
    public int create(Folder folder,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;

    /**
     * ����һ���ļ��У������紴���ĵ���ĳ�����ʹ��
     * 
     * @param session
     * @param folder
     * @throws E5Exception
     */
    public void create(Folder folder,Session session) throws E5Exception;

    /**
     * ɾ��ĳһ�ļ��У�����ļ��в����ڣ���ʲôҲ������ �÷�����ɾ�����ļ��м����е������ļ��У������ļ����ϵĹ���͹�����
     * 
     * @param folderID
     * @throws E5Exception
     */
    public void delete(int folderID) throws E5Exception;

    /**
     * �����ļ���     * 
     * 2006-1-17 10:01:02
     * @author zhang_kaifeng
     * @param folder �Ǹ��������ֵ�folder
     * @param fvRules �¸���Ĺ���
     * @param fvFilters �¸���Ĺ�����
     * @throws E5Exception
     */
    public void update(Folder folder,FVRules[] fvRules,FVFilters[] fvFilters) throws E5Exception;

    /**
     * ���Ѿ����ڵ�session��ɾ��folder��¼�������ڲ��ر�session
     * 
     * @param docLibID
     * @param ss
     * @throws E5Exception
     */
    void delete(int folderID, Session ss) throws E5Exception;

  
    
    /**
     * �ļ���������ק�ļ���ʱ���ı����ĸ��ڵ�
     * 2006-1-17 12:26:10
     * @author zhang_kaifeng
     * @param folder
     * @throws E5Exception
     */
    public void drag(Folder folder) throws E5Exception;
    
    /**
     * �����ĵ�������ʱ��ͬ����������Ϊ���ļ��е�����
     * 2006-3-9 10:27:34
     * @author zhang_kaifeng
     * @param docLibID
     * @param newName
     * @throws E5Exception
     */
    public void updateFolderName(int docLibID, String newName) throws E5Exception;
    
    /**
     * ��������ָ�����ļ����µ����ж����ļ�����ͼ
     * 2006-3-17 16:12:12
     * @author zhang_kaifeng
     * @param parentID
     * @throws E5Exception
     */
    public void reArrangeSubFVs(int parentID) throws E5Exception;
    
    /**
     * �����Ѿ��źõ�˳�򣬸�ĳ�ļ����������ӽ������
     * 2006-4-12 13:59:04 by Zhang Kaifeng
     * @param parentID
     * @param fvids
     * @throws E5Exception
     */
    public void reArrangeSubFVs(int[] fvids) throws E5Exception;

}