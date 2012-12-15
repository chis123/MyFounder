package com.founder.e5.dom;

import java.util.List;

import com.founder.e5.context.E5Exception;

/**
 * @created 2005-7-18 16:53:53
 * @author Zhang Kaifeng
 */
public interface FolderReader {
    
    /**
     * �����ĵ���ID����ȡ���Ӧ���ļ��ж���
     * @param docLibID �ĵ���ID
     * @return �ļ��ж���
     * @throws E5Exception
     */
    public Folder getRoot(int docLibID) throws E5Exception;

    /**
     * ��ȡָ��id��FolderView����
     * 
     * @param fvID �ļ�����ͼID
     * @return FolderView ���FolderView���󲻴��ڣ��򷵻�null
     * @throws E5Exception
     */
    public FolderView get(int fvID) throws E5Exception;

    /**
     * ��ȡָ���ĵ����µ������ļ��к���ͼ
     * 
     * @param docLibID �ĵ���ID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     */
    public FolderView[] getChildrenFVsByDocLib(int docLibID) throws E5Exception;

    /**
     * ����ָ���ļ����µ��������ļ��к���ͼ������һ�����Ľڵ�˳�������˵�
     * 
     * @param folderID �ļ���ID
     * @return ���û�У��򷵻ؿ�����
     * @throws E5Exception
     * 
     */
    public FolderView[] getChildrenFVs(int folderID) throws E5Exception;

    /**
     * ����ָ���ļ����µ�������id������һ�����Ľڵ�˳�������˵�
     * 
     * @param folderID �ļ���ID
     * @return ����ļ��������ӣ��򷵻ؿ�����
     * @throws E5Exception
     */
    public int[] getChildrenFVIDs(int folderID) throws E5Exception;

    /**
     * ��ȡָ���ļ����ϵ����й�������˳���
     * 
     * @param folderID �ļ���ID
     * @return ����ļ������޹����򷵻ؿ�����
     * @throws E5Exception
     */
    public Rule[] getRules(int folderID) throws E5Exception;

    /**
     * ��ȡָ���ļ����ϵ����й���id������˳���
     * 
     * @param folderID �ļ���ID
     * @return ����޹����򷵻ؿ�����
     * @throws E5Exception
     */
    public int[] getRuleIDs(int folderID) throws E5Exception;

    /**
     * ��ȡָ���ļ����ϵ����й�������������������飬�洢��List�У�ÿ��Ԫ���Ǹ�Filter�����飬����˳���
     * 
     * @param folderID �ļ���ID
     * @return ����ļ������޹��������򷵻ؿ��б�
     * @throws E5Exception
     */
    public List getFilters(int folderID) throws E5Exception;

    /**
     * ��ȡָ���ļ����ϵ����й�����id��������������飬�洢��List�У�ÿ��Ԫ���Ǹ�int���飬����˳���
     * 
     * @param folderID �ļ���ID
     * @return ����ļ������޹��������򷵻ؿ�����
     * @throws E5Exception
     */
    public List getFilterIDs(int folderID) throws E5Exception;
    
    /**
     * ��ȡָ���ļ��еĶ��ӽڵ㣨�ļ��У�
     * 
     * @param folderID �ļ���
     * @return ����ļ��������ӣ��򷵻ؿ�����
     * @throws E5Exception
     *             ����ļ��в����ڣ����׳��쳣
     */
    public Folder[] getSubFolders(int folderID) throws E5Exception;

    /**
     * ��ȡָ���ļ��еĶ��ӽڵ��id���� 2006-1-16 16:35:04
     * 
     * @param folderID ָ�����ļ���id
     * @return ����ļ��������ӣ��򷵻ؿ�����
     * @throws E5Exception
     */
    public int[] getSubFoldersID(int folderID) throws E5Exception;
    
    /**
     * ��ȡָ���ļ��еĶ��ӽڵ�����飨������ͼ��
     * @param folderID �ļ���ID
     * @return ������ӣ��򷵻ؿ�����
     * @throws E5Exception
     */
    public FolderView[] getSubFVs(int folderID) throws E5Exception;
    
    /**
     * ��ȡָ��id��FolderView��������
     * @param fvIDs ���Ϊnull���򷵻ؿ�����
     * @return ���ĳ��idû�ж�Ӧ��FV���򷵻ؿ�����
     * @throws E5Exception
     */
    public FolderView[] getFVs(int[] fvIDs) throws E5Exception;

}