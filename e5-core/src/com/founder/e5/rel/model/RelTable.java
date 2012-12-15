/**********************************************************************
 * Copyright (c) 2002,�������������޹�˾���ӳ�����ҵ������ϵͳ������
 * All rights reserved.
 * 
 * ժҪ��
 * 
 * ��ǰ�汾��1.0
 * ���ߣ� �ſ���   Zhang_KaiFeng@Founder.Com
 * ������ڣ�2006-3-21  10:00:18
 *  
 *********************************************************************/
package com.founder.e5.rel.model;

public class RelTable extends BaseObject {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String tableName;
    private Integer dsID;
    private Integer refDocTypeID;

    /**
     * ��������ԴID
     * 
     * @return ����ԴID
     */
    public Integer getDsID() {
        return dsID;
    }

    /**
     * ��������ԴID
     * 
     * @param dsID
     *            ����ԴID
     */
    public void setDsID(Integer dsID) {
        this.dsID = dsID;
    }

    /**
     * ���ط��������ID
     * 
     * @return ���������ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * ���÷��������ID
     * 
     * @param id
     *            ���������ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ���ط������������
     * 
     * @return �������������
     */
    public String getName() {
        return name;
    }

    /**
     * ���÷������������
     * 
     * @param name
     *            �������������
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ���������ĵ�����ID
     * 
     * @return �����ĵ�����ID
     */
    public Integer getRefDocTypeID() {
        return refDocTypeID;
    }

    /**
     * ���������ĵ�����ID
     * 
     * @param refDocTypeID
     *            �����ĵ�����ID
     */
    public void setRefDocTypeID(Integer refDocTypeID) {
        this.refDocTypeID = refDocTypeID;
    }

    /**
     * ���ط�����������
     * 
     * @return ������������
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * ���÷�����������
     * 
     * @param tableName
     *            ������������
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
