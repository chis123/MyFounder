package com.founder.e5.dom;

import java.io.Serializable;

/**
 * 文档类型字段的修改记录表
 * 
 * @author kaifeng zhang
 * @version 1.0
 * @created 11-七月-2005 13:58:25
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
	 * 缺省构造函数
	 */
	public DocTypeFieldApps() {

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
     * 返回文档库ID
	 * @return 文档库ID
	 */
	public int getDocLibID() {
		return this.docLibID;
	}

	/**
	 * 设置文档库ID
	 * @param docLibID 文档库ID
	 */
	public void setDocLibID(int docLibID) {
		this.docLibID = docLibID;
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

	/**
     * 返回字段ID
	 * @return 字段ID
	 */
	public int getFieldID() {
		return this.fieldID;
	}

	/**
	 * 设置字段ID
	 * @param fieldID 字段ID
	 */
	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	/**
     * 判断是否新增字段
	 * @return 1代表新增
	 */
	public int getIsDBAdded() {
		return this.isDBAdded;
	}

	/**
	 * 设置是否新增字段
	 * @param isDBAdded 1代表是，否则为0
	 */
	public void setIsDBAdded(int isDBAdded) {
		this.isDBAdded = isDBAdded;
	}

	/**
     * 判断是否更新字段
	 * @return 1代表更新
	 */
	public int getIsDBUpdated() {
		return this.isDBUpdated;
	}

	/**
	 * 设置是否更新字段
	 * @param isDBUpdated 1代表是，否则为0
	 */
	public void setIsDBUpdated(int isDBUpdated) {
		this.isDBUpdated = isDBUpdated;
	}

	/**
     * 构造函数
	 * @param appid 子系统ID
	 * @param libID 文档库ID
	 * @param typeID 文档类型ID
	 * @param fieldid 字段ID
	 * @param added 是否新增
	 * @param updated 是否更新
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