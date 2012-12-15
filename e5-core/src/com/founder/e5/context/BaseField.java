package com.founder.e5.context;
import java.util.Map;

import com.founder.e5.config.ConfigReader;
import com.founder.e5.config.InfoField;
import com.founder.e5.db.ColumnInfo;
/**
 * 保存文档类型的平台字段的定义，包括名称、类型等
 * @created 11-7-2005 15:46:29
 * @author Gong Lijie
 * @version 1.0
 */
public class BaseField {

	public static final ColumnInfo DOCUMENTID;
	public static final ColumnInfo DOCLIBID;
	public static final ColumnInfo FOLDERID;
	public static final ColumnInfo DELETEFLAG;
	public static final ColumnInfo SYSCREATED;
	public static final ColumnInfo LASTMODIFIED;
	public static final ColumnInfo CURRENTFLOW;
	public static final ColumnInfo ISLOCKED;
	public static final ColumnInfo CURRENTNODE;
	public static final ColumnInfo CURRENTSTATUS;
	public static final ColumnInfo CURRENTUSERID;
	public static final ColumnInfo CURRENTUSERNAME;
	public static final ColumnInfo AUTHORS;
	public static final ColumnInfo TOPIC;
	public static final ColumnInfo HAVERELATION;
	public static final ColumnInfo ISKEEP;
	public static final ColumnInfo HAVEATTACH;

	static {
		ConfigReader reader = ConfigReader.getInstance();
		Map fields = reader.getFields();
		
		InfoField field = (InfoField)fields.get("DOCUMENTID");
		DOCUMENTID = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("DOCLIBID");
		DOCLIBID = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("FOLDERID");
		FOLDERID = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("DELETEFLAG");
		DELETEFLAG = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("CREATED");
		SYSCREATED = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("LASTMODIFIED");
		LASTMODIFIED = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("CURRENTFLOW");
		CURRENTFLOW = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("ISLOCKED");
		ISLOCKED = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("CURRENTNODE");
		CURRENTNODE = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("CURRENTSTATUS");
		CURRENTSTATUS = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("CURRENTUSERID");
		CURRENTUSERID = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("CURRENTUSERNAME");
		CURRENTUSERNAME = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("AUTHORS");
		AUTHORS = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("TOPIC");
		TOPIC = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("HAVERELATION");
		HAVERELATION = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("ISKEEP");
		ISKEEP = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());

		field = (InfoField)fields.get("HAVEATTACH");
		HAVEATTACH = new ColumnInfo(field.getCode(), field.getName(), field.getType(), 
				field.getNullable().equals("true"), false, 
				Integer.parseInt(field.getLength()), 0, field.getDefaultValue());
	}
}