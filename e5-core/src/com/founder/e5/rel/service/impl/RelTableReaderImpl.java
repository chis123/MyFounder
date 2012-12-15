/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-4-28  17:50:45
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
