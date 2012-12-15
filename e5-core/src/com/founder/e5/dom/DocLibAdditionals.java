package com.founder.e5.dom;

/**
 * 文档库与其流程表的对应关系表
 * 
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-七月-2005 14:00:31
 */
public class DocLibAdditionals {

    private int id;

    private int libTypes;

    private String libServer;

    private String libDB;

    private String libTable;

    private int docLibID;

    public DocLibAdditionals() {

    }

    /**
     * 返回文档库ID
     * @return 文档库ID
     */
    public int getDocLibID() {
        return docLibID;
    }

    /**
     * 设置文档库ID
     * @param docLibID 文档库ID
     */
    public void setDocLibID(int docLibID) {
        this.docLibID = docLibID;
    }

    /**
     * 返回文档库对应流程表ID
     * @return 文档库对应流程表ID
     */
    public int getId() {
        return id;
    }

    /**
     * 设置流水号
     * @param id 流水号
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 返回文档库数据库名称
     * @return 文档库数据库名称
     */
    public String getLibDB() {
        return libDB;
    }

    /**
     * 设置文档库数据库名称
     * @param libDB 文档库数据库名称 
     */
    public void setLibDB(String libDB) {
        this.libDB = libDB;
    }

    /**
     * 返回文档库服务器名称
     * @return 文档库服务器名称
     */
    public String getLibServer() {
        return libServer;
    }

    /**
     * 设置文档库服务器名称
     * @param libServer 文档库服务器名称
     */
    public void setLibServer(String libServer) {
        this.libServer = libServer;
    }

    /**
     * 返回文档库对应表名称
     * @return 文档库对应表名称
     */
    public String getLibTable() {
        return libTable;
    }

    /**
     * 设置文档库对应表名称
     * @param libTable 文档库对应表名称
     */
    public void setLibTable(String libTable) {
        this.libTable = libTable;
    }

    /**
     * 返回文档库类型
     * @return 文档库类型
     */
    public int getLibTypes() {
        return libTypes;
    }

    /**
     * 设置文档库类型
     * @param libTypes 文档库类型
     */
    public void setLibTypes(int libTypes) {
        this.libTypes = libTypes;
    }
}