package com.founder.e5.dom;

import java.io.Serializable;

/**
 * @created 05-七月-2005 9:02:34
 * @author kaifeng zhang
 * @version 1.0
 */
public class DocTypeApps implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8110036900215352507L;
	private int docTypeID;
    private int appID;

    /**
     * 构造函数
     * @param appid 子系统ID
     * @param typeID 文档类型ID
     */
    public DocTypeApps(int appid, int typeID) {
        super();
        appID = appid;
        docTypeID = typeID;
    }

    /**
     * 缺省构造函数
     */
    public DocTypeApps() {

    }

    /**
     * 返回子系统ID
     * @return 子系统ID
     */
    public int getAppID() {
        return this.appID;
    }

    /**
     * 设置子系统ID
     * @param appID 子系统ID
     */
    public void setAppID(int appID) {
        this.appID = appID;
    }

    /**
     * 返回文档类型ID
     * @return 文档类型ID
     */
    public int getDocTypeID() {
        return this.docTypeID;
    }

    /**
     * 设置文档类型ID
     * @param docTypeID 文档类型ID
     */
    public void setDocTypeID(int docTypeID) {
        this.docTypeID = docTypeID;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return super.hashCode();
    }

}