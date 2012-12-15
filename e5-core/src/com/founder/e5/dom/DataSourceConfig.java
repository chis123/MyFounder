package com.founder.e5.dom;

/**
 * 管理某模块需要的单独数据源。用名称做区分。如关联表需单独数据源，则记录可以是：(‘DOM_RELATIONS’,2)
 * 
 * @version 1.0
 * @created 11-七月-2005 15:40:53
 */
public class DataSourceConfig {

    /**
     * 名称。唯一，用这个名字找到其对应的数据源ID
     */
    private String dsName;

    /**
     * 返回数据源ID
     * @return 数据源ID
     */
    public int getDsID ( ) {
        return dsID;
    }

    /**
     * 设置数据源ID
     * @param dsID 数据源ID
     */
    public void setDsID ( int dsID ) {
        this.dsID = dsID;
    }

    /**
     * 返回数据源名称
     * @return 数据源名称
     */
    public String getDsName ( ) {
        return dsName;
    }

    /**
     * 设置数据源名称
     * @param name 数据源名称
     */
    public void setDsName ( String name ) {
        this.dsName = name;
    }

    private int dsID;

    public DataSourceConfig() {

    }

    public DataSourceConfig(String name, int dsid) {
        super ( );
        this.dsID = dsid;
        this.dsName = name;
    }

}