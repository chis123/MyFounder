package com.founder.e5.dom;

import java.io.Serializable;

import com.founder.e5.db.ColumnInfo;

/**
 * depends on db.jar
 * 
 * @created 05-七月-2005 9:02:25
 * @author kaifeng zhang
 * @version 1.0
 */
public class DocTypeField implements Serializable, Cloneable {
	/**字段填写方式：任意填写*/
	public static final int EDITTYPE_FREE = 0;
	/**字段填写方式：枚举值*/
	public static final int EDITTYPE_ENUM = 1;
	/**字段填写方式：布尔值*/
	public static final int EDITTYPE_BOOLEAN = 2;
	/**字段填写方式：复杂控件方式*/
	public static final int EDITTYPE_COMPLEX = 3;

	/**字段填写方式：APPLET.保留类型，没使用*/
	public static final int EDITTYPE_APPLET = 4;
	/**字段填写方式：下拉框选择*/
	public static final int EDITTYPE_SELECT = 5;
	/**字段填写方式：树型选择*/
	public static final int EDITTYPE_TREE = 6;
	
	/**字段级别1：平台级字段。由平台提供的字段*/
	public static final int FIELD_SYSTEM = 1;
	/**字段级别2：应用级字段。由子系统加入的字段*/
	public static final int FIELD_APPLICATION = 2;
	/**字段级别3：用户级字段。由用户手工扩展的字段*/
	public static final int FIELD_USER = 3;
	
	
	private static final long serialVersionUID = -1850463587169640830L;

	private int attribute;
    private String columnCode;
    private String columnName;
    private String dataType;
    private int dataLength;
    private String defaultValue;
    private int docTypeID;
    private int fieldID;

    private int isNull;

    
    private int scale;

    private String status;

    private int readonly;//是否只读
    private int editType;//字段填写方式:0-任意填写;1-枚举值;2-布尔值;3-复杂控件
						//4-Applet类型（保留类型，没使用）;
						//5-下拉框，动态的枚举值;
    					//6-选择树;
    private String options;//枚举值
    
    private int isIndexed;	//是否可建索引（此属性没使用）
    private String classID;	//控件方式时的CLSID
    private String codeBase;//控件的codeBase
    private String beanName;//控件方式时的初始化值获取类
    private String url;		//复杂填写时url地址
    
    public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getClassID() {
		return classID;
	}

	public void setClassID(String classid) {
		this.classID = classid;
	}

	public String getCodeBase() {
		return codeBase;
	}

	public void setCodeBase(String codebase) {
		this.codeBase = codebase;
	}

	public int getIsIndexed() {
		return isIndexed;
	}

	public void setIsIndexed(int isIndexed) {
		this.isIndexed = isIndexed;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getEditType()
	{
		return editType;
	}

	public void setEditType(int editType)
	{
		this.editType = editType;
	}

	public String getOptions()
	{
		return options;
	}

	public void setOptions(String options)
	{
		this.options = options;
	}

	public int getReadonly()
	{
		return readonly;
	}

	public void setReadonly(int readonly)
	{
		this.readonly = readonly;
	}
	
	/**
     * 返回字段类型
     * @return 字段类型 1—系统平台字段 2—应用系统字段 3－用户扩展字段
     */
    public int getAttribute ( ) {
        return this.attribute;
    }

    /**
     * 返回字段英文码
     * @return 字段英文码
     */
    public String getColumnCode ( ) {
        return columnCode;
    }

    /**
     * 返回字段显示码
     * @return 字段显示码
     */
    public String getColumnName ( ) {
        return columnName;
    }

    /**
     * 返回字段数据类型
     * @return 字段数据类型
     */
    public String getDataType ( ) {
        return this.dataType;
    }

    /**
     * 返回字段缺省值
     * @return 字段缺省值
     */
    public String getDefaultValue ( ) {
        return this.defaultValue;
    }

    /**
     * 返回文档类型ID
     * @return 文档类型ID
     */
    public int getDocTypeID ( ) {
        return this.docTypeID;
    }

    /**
     * 返回字段ID
     * @return 字段ID
     */
    public int getFieldID ( ) {
        return this.fieldID;
    }

    /**
     * 返回字段是否为空
     * @return 1代表是，0代表否
     */
    public int getIsNull ( ) {
        return this.isNull;
    }

    /**
     * 返回数据长度
     * @return 数据长度
     */
    public int getDataLength ( ) {
        return this.dataLength;
    }

    /**
     * 返回字段精度
     * @return 字段精度
     */
    public int getScale ( ) {
        return this.scale;
    }

    /**
     * 返回字段状态
     * @return 字段状态，D－删除 P－有效

     */
    public String getStatus ( ) {
        return this.status;
    }

    /**
     * 设置字段类型
     * @param 字段类型
     */
    public void setAttribute ( int attribute ) {
        this.attribute = attribute;
    }

    /**
     * 设置字段英文码
     * @param 字段英文码
     */
    public void setColumnCode ( String columnCode ) {
        this.columnCode = columnCode;
    }

    /**
     * 设置字段显示码
     * @param 字段显示码
     */
    public void setColumnName ( String columnName ) {
        this.columnName = columnName;
    }

    /**
     * 设置字段数据类型
     * @param dataType 字段数据类型
     */
    public void setDataType ( String dataType ) {
        this.dataType = dataType;
    }

    /**
     * 设置字段缺省值
     * @param defaultValue 字段缺省值
     */
    public void setDefaultValue ( String defaultValue ) {
        this.defaultValue = defaultValue;
    }

    /**
     * 设置文档类型ID
     * @param docTypeID 文档类型ID
     */
    public void setDocTypeID ( int doctypeID ) {
        this.docTypeID = doctypeID;
    }

    /**
     * 设置字段ID
     * @param fieldID 字段ID 
     */
    public void setFieldID ( int fieldID ) {
        this.fieldID = fieldID;
    }

    /**
     * 
     * @param isNull
     */
    public void setIsNull ( int isNull ) {
        this.isNull = isNull;
    }

    /**
     * 返回字段数据长度
     * @param dataLength 字段数据长度
     */
    public void setDataLength ( int length ) {
        this.dataLength = length;
    }

    /**
     * 设置字段精度
     * @param scale 字段精度
     */
    public void setScale ( int scale ) {
        this.scale = scale;
    }

    /**
     * 设置字段状态
     * @param status 状态
     */
    public void setStatus ( String status ) {
        this.status = status;
    }

    /**
     * 缺省构造函数
     */
    public DocTypeField() {

    }

    /**
     * 这个构造函数在创建文档类型，生成平台字段时使用。
     * 
     * @param ci
     *            预先定义好的ColumnInfo对象，在context.BaseField中
     */
    DocTypeField(ColumnInfo ci) {
        this.columnCode = ci.getName ( );
        this.columnName = ci.getDisplayName ( );
        this.defaultValue = ci.getDefaultValue ( );
        this.dataLength = ci.getDataLength ( );
        this.scale = ci.getDataPrecision ( );
        this.dataType = ci.getE5TypeName ( );
        this.isNull = ci.isNullable ( ) ? 1 : 0;

    }
	/**
	 * Clone分类对象
	 */
	public Object clone()
	{
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}