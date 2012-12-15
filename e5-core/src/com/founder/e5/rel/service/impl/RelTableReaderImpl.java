/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-4-28  17:50:45
 *  
 *********************************************************************/
package com.founder.e5.rel.service.impl;

import com.founder.e5.context.CacheReader;
import com.founder.e5.rel.model.RelTable;
import com.founder.e5.rel.service.RelTableCache;
import com.founder.e5.rel.service.RelTableReader;

public class RelTableReaderImpl implements RelTableReader {
    
    private RelTableCache getCache() {
        return (RelTableCache) CacheReader.find(RelTableCache.class);
    }

    public RelTable getRelTable(int docLibID, int catTypeID) {
       return this.getCache().getRelTable(docLibID,catTypeID);
    }

    public String getRelTableName(int docLibID, int catTypeID) {
        return this.getCache().getRelTableName(docLibID,catTypeID);
    }

}
