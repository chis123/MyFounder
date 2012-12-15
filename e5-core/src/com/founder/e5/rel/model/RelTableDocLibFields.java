/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-4-29  16:45:08
 *  
 *********************************************************************/
package com.founder.e5.rel.model;

public class RelTableDocLibFields extends BaseObject {
    
	private static final long serialVersionUID = -7669605347342321834L;

	private Integer docLibID;
    private Integer catTypeID;
    private String tableField;
    private String libField;
    public RelTableDocLibFields() {
        super();
        // TODO Auto-generated constructor stub
    }
    public RelTableDocLibFields(Integer docLibID, Integer catTypeID, String tableField, String libField) {
        super();
        // TODO Auto-generated constructor stub
        this.docLibID = docLibID;
        this.catTypeID = catTypeID;
        this.tableField = tableField;
        this.libField = libField;
    }
    /**
     * @return Returns the catTypeID.
     */
    public Integer getCatTypeID() {
        return catTypeID;
    }
    /**
     * @param catTypeID The catTypeID to set.
     */
    public void setCatTypeID(Integer catTypeID) {
        this.catTypeID = catTypeID;
    }
    /**
     * @return Returns the docLibID.
     */
    public Integer getDocLibID() {
        return docLibID;
    }
    /**
     * @param docLibID The docLibID to set.
     */
    public void setDocLibID(Integer docLibID) {
        this.docLibID = docLibID;
    }
    /**
     * @return Returns the libField.
     */
    public String getLibField() {
        return libField;
    }
    /**
     * @param libField The libField to set.
     */
    public void setLibField(String libField) {
        this.libField = libField;
    }
    /**
     * @return Returns the tableField.
     */
    public String getTableField() {
        return tableField;
    }
    /**
     * @param tableField The tableField to set.
     */
    public void setTableField(String tableField) {
        this.tableField = tableField;
    }
    

}
