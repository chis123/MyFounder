package com.founder.e5.sys.note;



public class SimpleUser {
	protected int userID;

	protected String userCode;

	protected String userName;
	public int getuserID() {
		return userID;
	}

	public void setuserID(int NoteId) {
		this.userID = NoteId;
	}
	public String getuserCode() {
		return userCode;
	}

	public void setuserCode(String UserCode) {
		this.userCode = UserCode;
	}
	public String getuserName() {
		return userName;
	}

	public void setuserName(String UserName) {
		this.userName = UserName;
	}
	
}
