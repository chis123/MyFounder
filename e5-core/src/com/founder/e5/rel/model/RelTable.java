/**********************************************************************
 * Copyright (c) 2002,北大方正电子有限公司电子出版事业部集成系统开发部
 * All rights reserved.
 * 
 * 摘要：
 * 
 * 当前版本：1.0
 * 作者： 张凯峰   Zhang_KaiFeng@Founder.Com
 * 完成日期：2006-3-21  10:00:18
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
     * 返回数据源ID
     * 
     * @return 数据源ID
     */
    public Integer getDsID() {
        return dsID;
    }

    /**
     * 设置数据源ID
     * 
     * @param dsID
     *            数据源ID
     */
    public void setDsID(Integer dsID) {
        this.dsID = dsID;
    }

    /**
     * 返回分类关联表ID
     * 
     * @return 分类关联表ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置分类关联表ID
     * 
     * @param id
     *            分类关联表ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 返回分类关联表名称
     * 
     * @return 分类关联表名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类关联表名称
     * 
     * @param name
     *            分类关联表名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 返回引用文档类型ID
     * 
     * @return 引用文档类型ID
     */
    public Integer getRefDocTypeID() {
        return refDocTypeID;
    }

    /**
     * 设置引用文档类型ID
     * 
     * @param refDocTypeID
     *            引用文档类型ID
     */
    public void setRefDocTypeID(Integer refDocTypeID) {
        this.refDocTypeID = refDocTypeID;
    }

    /**
     * 返回分类关联表表名
     * 
     * @return 分类关联表表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置分类关联表表名
     * 
     * @param tableName
     *            分类关联表表名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
