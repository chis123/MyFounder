package com.founder.e5.dom;

import java.io.Serializable;

/**
 * @created 05-����-2005 9:02:34
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
     * ���캯��
     * @param appid ��ϵͳID
     * @param typeID �ĵ�����ID
     */
    public DocTypeApps(int appid, int typeID) {
        super();
        appID = appid;
        docTypeID = typeID;
    }

    /**
     * ȱʡ���캯��
     */
    public DocTypeApps() {

    }

    /**
     * ������ϵͳID
     * @return ��ϵͳID
     */
    public int getAppID() {
        return this.appID;
    }

    /**
     * ������ϵͳID
     * @param appID ��ϵͳID
     */
    public void setAppID(int appID) {
        this.appID = appID;
    }

    /**
     * �����ĵ�����ID
     * @return �ĵ�����ID
     */
    public int getDocTypeID() {
        return this.docTypeID;
    }

    /**
     * �����ĵ�����ID
     * @param docTypeID �ĵ�����ID
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