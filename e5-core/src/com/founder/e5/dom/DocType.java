package com.founder.e5.dom;

/**
 * @created 05-七月-2005 9:02:25
 * @author ZhangKaifeng
 * @version 1.0
 */
public class DocType {

    /**
     * 文档类型ID
     */
    private int docTypeID;

    /**
     * 文档类型名称
     */
    private String docTypeName;

    /**
     * 对应文档类型ID串，中间用逗号隔开
     */
    private String docTypeRelated;

    /**
     * 该文档类型的缺省流程 Comment for <code>defaultFlow</code>
     */
    private int defaultFlow;

    /**
     * 描述
     */
    private String descInfo;

    /**
     * 子系统id，初始值为0，不在hbm中做映射
     */
    private int appID;

    /**
     * 构造函数
     * @param docTypeName 文档类型名称
     * @param descInfo 描述信息
     */
    public DocType(String docTypeName, String descInfo) {
        super();
        this.docTypeName = docTypeName;
        this.descInfo = descInfo;
    }

    /**
     * 构造函数
     * @param flow 缺省流程ID
     * @param description 描述信息
     * @param name 名称
     * @param related 关联的文档类型ID串
     * @param appID 子系统ID
     */
    public DocType(int flow, String description, String name, String related,
            int appID) {

        super ( );
        this.defaultFlow = flow;
        this.descInfo = description;
        this.docTypeName = name;
        this.docTypeRelated = related;
        this.appID = appID;
    }

    /**
     * 缺省构造函数
     */
    public DocType() {

    }

    /**
     * 返回缺省流程
     * @return 缺省流程的ID
     */
    public int getDefaultFlow ( ) {
        return this.defaultFlow;
    }

    /**
     * 设置缺省流程
     * @param defaultFlow 缺省流程ID
     */
    public void setDefaultFlow ( int defaultFlow ) {
        this.defaultFlow = defaultFlow;

    }

    /**
     * 返回文档类型描述信息
     * @return 描述信息
     */
    public String getDescInfo ( ) {
        return this.descInfo;
    }

    /**
     * 设置描述信息
     * @param description 描述信息
     */
    public void setDescInfo ( String description ) {
        this.descInfo = description;
    }

    /**
     * 返回文档类型ID
     * @return 文档类型ID
     */
    public int getDocTypeID ( ) {
        return this.docTypeID;
    }

    /**
     * 返回文档类型名称
     * @return 文档类型名称
     */
    public String getDocTypeName ( ) {
        return this.docTypeName;
    }

    /**
     * 设置子系统ID
     * @param appID 子系统ID
     */
    public void setAppID ( int appID ) {
        this.appID = appID;
    }

    void setDocTypeName ( String docTypeName ) {
        this.docTypeName = docTypeName;
    }

    /**
     * 返回关联的文档类型ID
     * @return 形如“2,3,4”
     */
    public String getDocTypeRelated ( ) {
        return this.docTypeRelated;
    }

    /**
     * 返回子系统ID
     * @return 子系统ID
     */
    public int getAppID ( ) {
        return appID;
    }

    void setDocTypeID ( int docTypeID ) {
        this.docTypeID = docTypeID;
    }

    /**
     * 设置关联的文档类型
     * @param doctypeRelated 关联的文档类型ID，形如“2,3,4”
     */
    public void setDocTypeRelated ( String doctypeRelated ) {
        this.docTypeRelated = doctypeRelated;
    }

}