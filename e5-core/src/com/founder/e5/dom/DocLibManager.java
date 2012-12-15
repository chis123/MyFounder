package com.founder.e5.dom;

import com.founder.e5.context.E5Exception;

/**
 * @created 05-七月-2005 9:02:23
 * @author kaifeng zhang
 * @version 1.0
 */
public interface DocLibManager extends DocLibReader {

    /**
     * 创建文档库
     * @param DDL TODO
     * @param docLib
     *            必须设置好dsid，doctypeID,doclibid等属性
     * 
     * @throws E5Exception
     */
    public void create(String DDL, DocLib docLib) throws E5Exception;

    /**
     * 删除文档库：删除流程表关联记录，删除文档库对应文件夹及所有子文件夹，删除文档库记录
     * 
     * @param docLibID
     * @throws E5Exception
     */
    public void delete(int docLibID) throws E5Exception;

    /**
     * 更新文档库，这里只更新文档库的名字或描述信息
     * 
     * @param docLib
     * @return
     * @throws E5Exception
     */
    public void update(DocLib docLib) throws E5Exception;

    /**
     * 直接用DDL更新DocLib表，主要用来建立索引等
     * 
     * @param docLibID
     * @param DDLStr
     * @throws E5Exception
     */
    public void alterDocLib(int docLibID, String DDLStr) throws E5Exception;

    /**
     * 当文档类型字段变化后，更新文档库
     * 
     * 这里主要考虑增加了字段的情况。
     * 
     * 删除字段后，无需变化文档库。 更新字段的情况不予考虑
     * 
     * @param docLibID
     * @throws E5Exception
     */
    public void alterDocLib(int docLibID) throws E5Exception;
    
    /**
     * 生成创建文档库的DDL返回到客户端
     * 2006-1-24 10:10:16
     * @author zhang_kaifeng
     * @param doclib 包含有属性值的：typeid，dsid
     * @return TODO
     * @throws E5Exception
     */
    public String generateDDL(DocLib doclib) throws E5Exception;
    
    /**
     * 判断某个数据源是否用来创建了文档库
     * 2006-3-2 9:31:46
     * @author zhang_kaifeng
     * @param dsID
     * @return
     * @throws E5Exception
     */
    public boolean hasUsedForDocLib(int dsID) throws E5Exception;

}