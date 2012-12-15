/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-4-28  17:48:46
 *  
 *********************************************************************/
package com.founder.e5.rel.service;

import com.founder.e5.rel.model.RelTable;

public interface RelTableReader {
    
    /**
     * 根据文档库ID，分类类型ID，获取对应的分类关联表对象
     * @param docLibID 文档库ID
     * @param catTypeID 分类类型ID
     * @return 如果没有，则返回null
     */
    public RelTable getRelTable(int docLibID,int catTypeID);
    
    /**
     * 根据文档库ID，分类类型ID，获取对应的分类关联表名称
     * @param docLibID 文档库ID
     * @param catTypeID 分类类型ID
     * @return 如果没有，则返回null
     */
    public String getRelTableName(int docLibID,int catTypeID);
}
