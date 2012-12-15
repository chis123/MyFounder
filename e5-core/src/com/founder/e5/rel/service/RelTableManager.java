package com.founder.e5.rel.service;

import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.dao.RelTableDAO;
import com.founder.e5.rel.model.RelTable;
import com.founder.e5.rel.model.RelTableField;
import com.founder.e5.rel.model.RelTableVO;

public interface RelTableManager {
    
    public void setDao(RelTableDAO dao);
    
    /**
     * 获取指定文档类型的关联表对象
     * @param docTypeId
     * @return 如果参数为0，则返回所有的分类关联表对象
     * @throws E5Exception
     */
    public RelTable[] getRelTables(int docTypeId) throws E5Exception;
    
    /**
     * 获取指定的关联表对象
     * 2006-3-21 11:13:25
     * @author zhang_kaifeng
     * @param tableId
     * @return
     */
    public RelTable getRelTable(int tableId)throws E5Exception;
    
    /**
     * 删除指定的关联表对象
     * 2006-3-21 11:13:48
     * @param talbeId
     * @return 1代表删除成功，0代表删除失败（该分类关联表已经建立与文档库的对应，不得直接删除分类关联表）
     */
    public int removeRelTable(int talbeId) throws E5Exception;
    
    /**
     * 生成待创建的分类关联表的DDL
     * 2006-3-21 16:16:24
     * @author zhang_kaifeng
     * @param table 必需有有效值：dsid，tablename,fieldIds
     * @return
     */
    public String genCreateDDL(RelTableVO table)throws E5Exception;
    
    /**
     * 根据用户确定的ddl，创建分类关联表
     * 2006-3-21 16:18:56
     * @author zhang_kaifeng
     * @param ddl 用户确认并可能修改过的ddl
     * @param table 必需有有效值：dsid，doctypeid，name，tablename
     * @throws Exception 
     */
    public void createRelTable(String ddl, RelTable table) throws Exception;
    
   
    /**
     * 为分类关联表添加其他的字段
     * 2006-3-21 18:07:17
     * @author zhang_kaifeng
     * @param field
     * @throws Exception
     */
    public void appendRelTableField(RelTableField field) throws Exception;
    
    /**
     * 保存分类关联表记录
     * 2006-3-22 9:28:36
     * @author zhang_kaifeng
     * @param table
     * @throws Exception
     */
    public void saveRelTable(RelTable table) throws Exception ;
    
    /**
     * 根据分类关联表的表名后缀，获取表对象
     * 2006-4-21 14:46:13 by Zhang Kaifeng
     * @param tableSuffix
     * @return
     */
    public RelTable getRelTable(String tableSuffix);
}
