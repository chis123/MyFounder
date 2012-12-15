package com.founder.e5.rel.service;

import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.dao.RelTableDAO;
import com.founder.e5.rel.model.RelTable;
import com.founder.e5.rel.model.RelTableField;
import com.founder.e5.rel.model.RelTableVO;

public interface RelTableManager {
    
    public void setDao(RelTableDAO dao);
    
    /**
     * ��ȡָ���ĵ����͵Ĺ��������
     * @param docTypeId
     * @return �������Ϊ0���򷵻����еķ�����������
     * @throws E5Exception
     */
    public RelTable[] getRelTables(int docTypeId) throws E5Exception;
    
    /**
     * ��ȡָ���Ĺ��������
     * 2006-3-21 11:13:25
     * @author zhang_kaifeng
     * @param tableId
     * @return
     */
    public RelTable getRelTable(int tableId)throws E5Exception;
    
    /**
     * ɾ��ָ���Ĺ��������
     * 2006-3-21 11:13:48
     * @param talbeId
     * @return 1����ɾ���ɹ���0����ɾ��ʧ�ܣ��÷���������Ѿ��������ĵ���Ķ�Ӧ������ֱ��ɾ�����������
     */
    public int removeRelTable(int talbeId) throws E5Exception;
    
    /**
     * ���ɴ������ķ���������DDL
     * 2006-3-21 16:16:24
     * @author zhang_kaifeng
     * @param table ��������Чֵ��dsid��tablename,fieldIds
     * @return
     */
    public String genCreateDDL(RelTableVO table)throws E5Exception;
    
    /**
     * �����û�ȷ����ddl���������������
     * 2006-3-21 16:18:56
     * @author zhang_kaifeng
     * @param ddl �û�ȷ�ϲ������޸Ĺ���ddl
     * @param table ��������Чֵ��dsid��doctypeid��name��tablename
     * @throws Exception 
     */
    public void createRelTable(String ddl, RelTable table) throws Exception;
    
   
    /**
     * Ϊ�������������������ֶ�
     * 2006-3-21 18:07:17
     * @author zhang_kaifeng
     * @param field
     * @throws Exception
     */
    public void appendRelTableField(RelTableField field) throws Exception;
    
    /**
     * �������������¼
     * 2006-3-22 9:28:36
     * @author zhang_kaifeng
     * @param table
     * @throws Exception
     */
    public void saveRelTable(RelTable table) throws Exception ;
    
    /**
     * ���ݷ��������ı�����׺����ȡ�����
     * 2006-4-21 14:46:13 by Zhang Kaifeng
     * @param tableSuffix
     * @return
     */
    public RelTable getRelTable(String tableSuffix);
}
