/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-3-21  16:35:23
 *  
 *********************************************************************/
package com.founder.e5.rel.model;


public class RelTableVO{
    
    
    private String name;
    private String tableName;
    private int dsID;
    private int refDocTypeID;    
    
    private int[] filedIds;

    /**
     * @return Returns the dsID.
     */
    public int getDsID() {
        return dsID;
    }

    /**
     * @param dsID The dsID to set.
     */
    public void setDsID(int dsId) {
        this.dsID = dsId;
    }

    /**
     * @return Returns the filedIds.
     */
    public int[] getFiledIds() {
        return filedIds;
    }

    /**
     * @param filedIds The filedIds to set.
     */
    public void setFiledIds(int[] filedIds) {
        this.filedIds = filedIds;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the refDocTypeID.
     */
    public int getRefDocTypeID() {
        return refDocTypeID;
    }

    /**
     * @param refDocTypeID The refDocTypeID to set.
     */
    public void setRefDocTypeID(int refDocTypeId) {
        this.refDocTypeID = refDocTypeId;
    }

    /**
     * @return Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName The tableName to set.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


}
