package com.founder.e5.personality;

import java.io.Serializable;

/**
 * 个性化设置值对象，对应于数据库中fsys_personsetting表中的一条记录
 * 
 * @author liyanhui
 * @version 1.0
 * @created 2006-3-23 13:25:25
 */
public class PersonalSetting implements Serializable {

	private static final long serialVersionUID = 1541271237087199620L;

	private int userID;

	private int roleID;

	private String item;

	private String value;

	private String ext1;

	private String ext2;

	private String ext3;

	private String ext4;

	private String ext5;

	/**
	 * 
	 */
	public PersonalSetting() {
		super();
	}

	public String getItem() {
		return item;
	}

	public void setItem( String item ) {
		this.item = item;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID( int roleID ) {
		this.roleID = roleID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID( int userID ) {
		this.userID = userID;
	}

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1( String ext1 ) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2( String ext2 ) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3( String ext3 ) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4( String ext4 ) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5( String ext5 ) {
		this.ext5 = ext5;
	}

}
