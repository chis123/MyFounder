/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-4-28  17:51:05
 *  
 *********************************************************************/
package com.founder.e5.rel.service;

import gnu.trove.TIntObjectHashMap;

import java.util.ArrayList;

import com.founder.e5.context.Cache;
import com.founder.e5.context.Context;
import com.founder.e5.context.E5Exception;
import com.founder.e5.rel.model.RelTable;
import com.founder.e5.rel.model.RelTableDocLib;
import com.founder.e5.rel.model.RelTableDocLibVO;

public class RelTableCache implements Cache {
    
    private TIntObjectHashMap tableID_relTable;
    private RelTableDocLib[] relTableDocLibs;

    public RelTable getRelTable(int docLibID, int catTypeID) {
        for( int i = 0; i < relTableDocLibs.length; i++){
            RelTableDocLib temp = relTableDocLibs[i];
            if(temp.getCatTypeId().intValue() == catTypeID && temp.getDocLibId().intValue() == docLibID){
                int tableID = temp.getRelTableId().intValue();
                return (RelTable) this.tableID_relTable.get(tableID);
            }                
        }
        
        return null;
    }

    public String getRelTableName(int docLibID, int catTypeID) {
        RelTable table = this.getRelTable(docLibID,catTypeID);
        if(table != null)
            return table.getTableName();
        return null;
    }

    public void refresh() throws E5Exception {
        
        this.tableID_relTable = new TIntObjectHashMap();
        RelTableManager tableManager = (RelTableManager) Context.getBean(RelTableManager.class);
        RelTable[] tables = tableManager.getRelTables(0);
        for( int i = 0; i < tables.length; i++){
            RelTable table = tables[i];
            this.tableID_relTable.put(table.getId().intValue(),table);
        }

        RelTableDocLibManager tableLibManager = (RelTableDocLibManager) Context.getBean(RelTableDocLibManager.class);
        RelTableDocLibVO[] vos = tableLibManager.getRelTableDocLibs();
        ArrayList list = new ArrayList();
        for( int i = 0; i < vos.length; i++){
            RelTableDocLibVO vo = vos[i];
            
            RelTableDocLib lib = new RelTableDocLib();
            lib.setDocLibId(new Integer(vo.getDocLibId()));
            lib.setCatTypeId(new Integer(vo.getCatTypeId()));
            lib.setRelTableId(new Integer(vo.getRelTableId()));
            
            list.add(lib);
        }
        this.relTableDocLibs = (RelTableDocLib[]) list.toArray(new RelTableDocLib[0]);
        
    }

    public void reset() {
        this.tableID_relTable.clear();
        this.relTableDocLibs = null;
        
    }
}
