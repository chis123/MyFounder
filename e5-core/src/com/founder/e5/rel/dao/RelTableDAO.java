package com.founder.e5.rel.dao;

import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.model.RelTable;


public interface RelTableDAO extends DAO {
    
    /**
     * ��ȡָ���ĵ����͵Ĺ������¼
     * 2006-3-21 11:08:28
     * @author zhang_kaifeng
     * @return
     */
    public List getRelTables(int docTypeId);
    
    /**
     * ��ȡָ��id�Ĺ������¼
     * 2006-3-21 11:08:40
     * @author zhang_kaifeng
     * @param tableId
     * @return
     */
    public RelTable getRelTable(Integer tableId);
    
    /**
     * ����������¼�����ڲ�������id
     * 2006-3-21 11:08:56
     * @author zhang_kaifeng
     * @param table
     * @throws E5Exception 
     */
    public void saveRelTable(RelTable table) throws E5Exception;
    
    /**
     * ɾ��ָ�����¼
     * 2006-3-21 11:09:07
     * @author zhang_kaifeng
     * @param tableId
     */
    public void removeReltable(Integer tableId);
    
    /**
     * ���ݷ��������ı�����׺����ȡ�����
     * 2006-4-21 14:46:13 by Zhang Kaifeng
     * @param tableSuffix
     * @return
     */
    public RelTable getRelTable(String tableSuffix);
}
