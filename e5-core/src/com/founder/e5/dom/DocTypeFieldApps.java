package com.founder.e5.dom;

import java.io.Serializable;

/**
 * �ĵ������ֶε��޸ļ�¼��
 * 
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-����-2005 13:58:25
 */
public class DocTypeFieldApps implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5422625214607305116L;

	private int docTypeID;

	private int fieldID;

	private int docLibID;

	private int appID;

	private int isDBUpdated;

	private int isDBAdded;

	/**
	 * ȱʡ���캯��
	 */
	public DocTypeFieldApps() {

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
     * �����ĵ���ID
	 * @return �ĵ���ID
	 */
	public int getDocLibID() {
		return this.docLibID;
	}

	/**
	 * �����ĵ���ID
	 * @param docLibID �ĵ���ID
	 */
	public void setDocLibID(int docLibID) {
		this.docLibID = docLibID;
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

	/**
     * �����ֶ�ID
	 * @return �ֶ�ID
	 */
	public int getFieldID() {
		return this.fieldID;
	}

	/**
	 * �����ֶ�ID
	 * @param fieldID �ֶ�ID
	 */
	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	/**
     * �ж��Ƿ������ֶ�
	 * @return 1��������
	 */
	public int getIsDBAdded() {
		return this.isDBAdded;
	}

	/**
	 * �����Ƿ������ֶ�
	 * @param isDBAdded 1�����ǣ�����Ϊ0
	 */
	public void setIsDBAdded(int isDBAdded) {
		this.isDBAdded = isDBAdded;
	}

	/**
     * �ж��Ƿ�����ֶ�
	 * @return 1�������
	 */
	public int getIsDBUpdated() {
		return this.isDBUpdated;
	}

	/**
	 * �����Ƿ�����ֶ�
	 * @param isDBUpdated 1�����ǣ�����Ϊ0
	 */
	public void setIsDBUpdated(int isDBUpdated) {
		this.isDBUpdated = isDBUpdated;
	}

	/**
     * ���캯��
	 * @param appid ��ϵͳID
	 * @param libID �ĵ���ID
	 * @param typeID �ĵ�����ID
	 * @param fieldid �ֶ�ID
	 * @param added �Ƿ�����
	 * @param updated �Ƿ����
	 */
	public DocTypeFieldApps(int appid, int libID, int typeID, int fieldid,
			int added, int updated) {
		super();
		appID = appid;
		docLibID = libID;
		docTypeID = typeID;
		fieldID = fieldid;
		isDBAdded = added;
		isDBUpdated = updated;
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