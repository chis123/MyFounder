package com.founder.e5.dom;

/**
 * 调用规则类时传递的参数
 * @created 2006-4-21
 * @author Gong Lijie
 * @version 1.0
 */
public class RuleParam {
	/**当前用户ID*/
    private int userID;
	/**当前用户的角色ID*/
    private int roleID;
	/**当前文档类型ID*/
    private int docTypeID;
	/**当前文档库ID*/
    private int docLibID;
	/**当前文件夹ID*/
    private int fvID;
	/**父的层次,规则中应该+1*/
    private int level;
	/**父的权限码*/
    private int permissionCode;
	/**规则定义时的变参*/
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
