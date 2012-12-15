package com.founder.e5.rel.dao;

import java.util.List;

import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.model.RelTable;


public interface RelTableDAO extends DAO {
    
    /**
     * 获取指定文档类型的关联表记录
     * 2006-3-21 11:08:28
     * @author zhang_kaifeng
     * @return
     */
    public List getRelTables(int docTypeId);
    
    /**
     * 获取指定id的关联表记录
     * 2006-3-21 11:08:40
     * @author zhang_kaifeng
     * @param tableId
     * @return
     */
    public RelTable getRelTable(Integer tableId);
    
    /**
     * 保存关联表记录，在内部给对象赋id
     * 2006-3-21 11:08:56
     * @author zhang_kaifeng
     * @param table
     * @throws E5Exception 
     */
    public void saveRelTable(RelTable table) throws E5Exception;
    
    /**
     * 删除指定表记录
     * 2006-3-21 11:09:07
     * @author zhang_kaifeng
     * @param tableId
     */
    public void removeReltable(Integer tableId);
    
    /**
     * 根据分类关联表的表名后缀，获取表对象
     * 2006-4-21 14:46:13 by Zhang Kaifeng
     * @param tableSuffix
     * @return
     */
    public RelTable getRelTable(String tableSuffix);
}
