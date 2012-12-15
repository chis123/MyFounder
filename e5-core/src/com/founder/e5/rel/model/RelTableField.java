/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-3-21  18:05:38
 *  
 *********************************************************************/
package com.founder.e5.rel.model;

public class RelTableField {

    private int dataLength;

    private String dataType;
    private String defaultValue;
    private String fieldName;

    private int isNull;

    private int relTableId;

    private int scale;

    public int getDataLength() {
        return this.dataLength;
    }

    /**
     * @return Returns the dataType.
     */
    public String getDataType() {
        return dataType;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @return Returns the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }

    public int getIsNull() {
        return this.isNull;
    }

    /**
     * @return Returns the relTableId.
     */
    public int getRelTableId() {
        return relTableId;
    }
    
    public int getScale() {
        return this.scale;
    }

    /**
     * @param dataLength The dataLength to set.
     */
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }
    /**
     * @param dataType
     *            The dataType to set.
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @param fieldName
     *            The fieldName to set.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @param isNull
     *            The isNull to set.
     */
    public void setIsNull(int isNull) {
        this.isNull = isNull;
    }

    /**
     * @param relTableId
     *            The relTableId to set.
     */
    public void setRelTableId(int relTableId) {
        this.relTableId = relTableId;
    }

    /**
     * @param scale The scale to set.
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

}
