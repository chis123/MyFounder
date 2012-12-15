package com.founder.e5.dom;

/**
 * 文档库bean
 * 
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-七月-2005 14:00:25
 */
public class DocLib {

    private int docLibID;

    private String docLibName;

    private int docTypeID;

    private String docLibTable;

    private int keepDay;

    private String description;

    private String attachDevName;

    private int folderID;

    /**
     * 该文档库对应的数据源ID
     */
    private int dsID;

    /**
     * 是否持久库
     */
    private int isPersistent;

    
    /**
     * 缺省构造函数
     */
    public DocLib() {

    }
    
    /**
     * 返回附件设备名
     * @return 附件设备名
     */
    public String getAttachDevName ( ) {
        return attachDevName;
    }

    /**
     * @param attachDevName
     *            The attachDevName to set.
     */
    void setAttachDevName ( String attachDevName ) {
        this.attachDevName = attachDevName;
    }

    /**
     * 返回文档库描述信息
     * @return 文档库描述信息
     */
    public String getDescription ( ) {
        return description;
    }

    /**
     * 设置文档库描述信息
     * @param description 描述信息
     */
    public void setDescription ( String description ) {
        this.description = description;
    }

    /**
     * 返回文档库ID
     * @return 文档库ID
     */
    public int getDocLibID ( ) {
        return docLibID;
    }

    /**
     * @param docLibID
     *            The docLibID to set.
     */
    public void setDocLibID ( int docLibID ) {
        this.docLibID = docLibID;
    }

    /**
     * 返回文档库名称
     * @return 文档库名称
     */
    public String getDocLibName ( ) {
        return docLibName;
    }

    /**
     * 设置文档库名称
     * @param docLibName 文档库名称
     */
    public void setDocLibName ( String docLibName ) {
        this.docLibName = docLibName;
    }

    /**
     * 返回文档库表名
     * @return 文档库表名
     */
    public String getDocLibTable ( ) {
        return docLibTable;
    }

    /**
     * @param docLibTable
     *            The docLibTable to set.
     */
    void setDocLibTable ( String docLibTable ) {
        this.docLibTable = docLibTable;
    }

    /**
     * 返回文档类型ID
     * @return 文档类型ID
     */
    public int getDocTypeID ( ) {
        return docTypeID;
    }

    /**
     * @param docTypeID
     *            The docTypeID to set.
     */
    public void setDocTypeID ( int docTypeID ) {
        this.docTypeID = docTypeID;
    }

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
     * 返回文档库对应的根文件夹ID
     * @return 文件夹ID
     */
    public int getFolderID ( ) {
        return folderID;
    }

    /**
     * @param folderID
     *            The folderID to set.
     */
    void setFolderID ( int folderID ) {
        this.folderID = folderID;
    }

    /**
     * 返回是否持久库
     * @return 1代表是，0代表不是
     */
    public int getIsPersistent ( ) {
        return isPersistent;
    }

    /**
     * @param isPersistent
     *            The isPersistent to set.
     */
    void setIsPersistent ( int isPersistent ) {
        this.isPersistent = isPersistent;
    }

    /**
     * 返回保存天数
     * @return 保存天数
     */
    public int getKeepDay ( ) {
        return keepDay;
    }

    /**
     * @param keepDay
     *            The keepDay to set.
     */
    public void setKeepDay ( int keepDay ) {
        this.keepDay = keepDay;
    }
}