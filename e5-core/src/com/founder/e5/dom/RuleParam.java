package com.founder.e5.dom;

/**
 * ���ù�����ʱ���ݵĲ���
 * @created 2006-4-21
 * @author Gong Lijie
 * @version 1.0
 */
public class RuleParam {
	/**��ǰ�û�ID*/
    private int userID;
	/**��ǰ�û��Ľ�ɫID*/
    private int roleID;
	/**��ǰ�ĵ�����ID*/
    private int docTypeID;
	/**��ǰ�ĵ���ID*/
    private int docLibID;
	/**��ǰ�ļ���ID*/
    private int fvID;
	/**���Ĳ��,������Ӧ��+1*/
    private int level;
	/**����Ȩ����*/
    private int permissionCode;
	/**������ʱ�ı��*/
    private String varParam;
    
	public int getDocLibID() {
		return docLibID;
	}
	public void setDocLibID(int docLibID) {
		this.docLibID = docLibID;
	}
	public int getDocTypeID() {
		return docTypeID;
	}
	public void setDocTypeID(int docTypeID) {
		this.docTypeID = docTypeID;
	}
	public int getFvID() {
		return fvID;
	}
	public void setFvID(int fvID) {
		this.fvID = fvID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPermissionCode() {
		return permissionCode;
	}
	public void setPermissionCode(int permissionCode) {
		this.permissionCode = permissionCode;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getVarParam() {
		return varParam;
	}
	public void setVarParam(String varParam) {
		this.varParam = varParam;
	}
    
}
