package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @created 05-����-2005 9:02:23
 * @author kaifeng zhang
 * @version 1.0
 */
public interface DocLibManager extends DocLibReader {

    /**
     * �����ĵ���
     * @param DDL TODO
     * @param docLib
     *            �������ú�dsid��doctypeID,doclibid������
     * 
     * @throws E5Exception
     */
    public void create(String DDL, DocLib docLib) throws E5Exception;

    /**
     * ɾ���ĵ��⣺ɾ�����̱������¼��ɾ���ĵ����Ӧ�ļ��м��������ļ��У�ɾ���ĵ����¼
     * 
     * @param docLibID
     * @throws E5Exception
     */
    public void delete(int docLibID) throws E5Exception;

    /**
     * �����ĵ��⣬����ֻ�����ĵ�������ֻ�������Ϣ
     * 
     * @param docLib
     * @return
     * @throws E5Exception
     */
    public void update(DocLib docLib) throws E5Exception;

    /**
     * ֱ����DDL����DocLib����Ҫ��������������
     * 
     * @param docLibID
     * @param DDLStr
     * @throws E5Exception
     */
    public void alterDocLib(int docLibID, String DDLStr) throws E5Exception;

    /**
     * ���ĵ������ֶα仯�󣬸����ĵ���
     * 
     * ������Ҫ�����������ֶε������
     * 
     * ɾ���ֶκ�����仯�ĵ��⡣ �����ֶε�������迼��
     * 
     * @param docLibID
     * @throws E5Exception
     */
    public void alterDocLib(int docLibID) throws E5Exception;
    
    /**
     * ���ɴ����ĵ����DDL���ص��ͻ���
     * 2006-1-24 10:10:16
     * @author zhang_kaifeng
     * @param doclib ����������ֵ�ģ�typeid��dsid
     * @return TODO
     * @throws E5Exception
     */
    public String generateDDL(DocLib doclib) throws E5Exception;
    
    /**
     * �ж�ĳ������Դ�Ƿ������������ĵ���
     * 2006-3-2 9:31:46
     * @author zhang_kaifeng
     * @param dsID
     * @return
     * @throws E5Exception
     */
    public boolean hasUsedForDocLib(int dsID) throws E5Exception;

}