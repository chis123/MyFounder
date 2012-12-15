package com.founder.e5.dom;

import java.io.Serializable;

import com.founder.e5.db.ColumnInfo;

/**
 * depends on db.jar
 * 
 * @created 05-����-2005 9:02:25
 * @author kaifeng zhang
 * @version 1.0
 */
public class DocTypeField implements Serializable, Cloneable {
	/**�ֶ���д��ʽ��������д*/
	public static final int EDITTYPE_FREE = 0;
	/**�ֶ���д��ʽ��ö��ֵ*/
	public static final int EDITTYPE_ENUM = 1;
	/**�ֶ���д��ʽ������ֵ*/
	public static final int EDITTYPE_BOOLEAN = 2;
	/**�ֶ���д��ʽ�����ӿؼ���ʽ*/
	public static final int EDITTYPE_COMPLEX = 3;

	/**�ֶ���д��ʽ��APPLET.�������ͣ�ûʹ��*/
	public static final int EDITTYPE_APPLET = 4;
	/**�ֶ���д��ʽ��������ѡ��*/
	public static final int EDITTYPE_SELECT = 5;
	/**�ֶ���д��ʽ������ѡ��*/
	public static final int EDITTYPE_TREE = 6;
	
	/**�ֶμ���1��ƽ̨���ֶΡ���ƽ̨�ṩ���ֶ�*/
	public static final int FIELD_SYSTEM = 1;
	/**�ֶμ���2��Ӧ�ü��ֶΡ�����ϵͳ������ֶ�*/
	public static final int FIELD_APPLICATION = 2;
	/**�ֶμ���3���û����ֶΡ����û��ֹ���չ���ֶ�*/
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

    private int readonly;//�Ƿ�ֻ��
    private int editType;//�ֶ���д��ʽ:0-������д;1-ö��ֵ;2-����ֵ;3-���ӿؼ�
						//4-Applet���ͣ��������ͣ�ûʹ�ã�;
						//5-�����򣬶�̬��ö��ֵ;
    					//6-ѡ����;
    private String options;//ö��ֵ
    
    private int isIndexed;	//�Ƿ�ɽ�������������ûʹ�ã�
    private String classID;	//�ؼ���ʽʱ��CLSID
    private String codeBase;//�ؼ���codeBase
    private String beanName;//�ؼ���ʽʱ�ĳ�ʼ��ֵ��ȡ��
    private String url;		//������дʱurl��ַ
    
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
     * �����ֶ�����
     * @return �ֶ����� 1��ϵͳƽ̨�ֶ� 2��Ӧ��ϵͳ�ֶ� 3���û���չ�ֶ�
     */
    public int getAttribute ( ) {
        return this.attribute;
    }

    /**
     * �����ֶ�Ӣ����
     * @return �ֶ�Ӣ����
     */
    public String getColumnCode ( ) {
        return columnCode;
    }

    /**
     * �����ֶ���ʾ��
     * @return �ֶ���ʾ��
     */
    public String getColumnName ( ) {
        return columnName;
    }

    /**
     * �����ֶ���������
     * @return �ֶ���������
     */
    public String getDataType ( ) {
        return this.dataType;
    }

    /**
     * �����ֶ�ȱʡֵ
     * @return �ֶ�ȱʡֵ
     */
    public String getDefaultValue ( ) {
        return this.defaultValue;
    }

    /**
     * �����ĵ�����ID
     * @return �ĵ�����ID
     */
    public int getDocTypeID ( ) {
        return this.docTypeID;
    }

    /**
     * �����ֶ�ID
     * @return �ֶ�ID
     */
    public int getFieldID ( ) {
        return this.fieldID;
    }

    /**
     * �����ֶ��Ƿ�Ϊ��
     * @return 1�����ǣ�0�����
     */
    public int getIsNull ( ) {
        return this.isNull;
    }

    /**
     * �������ݳ���
     * @return ���ݳ���
     */
    public int getDataLength ( ) {
        return this.dataLength;
    }

    /**
     * �����ֶξ���
     * @return �ֶξ���
     */
    public int getScale ( ) {
        return this.scale;
    }

    /**
     * �����ֶ�״̬
     * @return �ֶ�״̬��D��ɾ�� P����Ч

     */
    public String getStatus ( ) {
        return this.status;
    }

    /**
     * �����ֶ�����
     * @param �ֶ�����
     */
    public void setAttribute ( int attribute ) {
        this.attribute = attribute;
    }

    /**
     * �����ֶ�Ӣ����
     * @param �ֶ�Ӣ����
     */
    public void setColumnCode ( String columnCode ) {
        this.columnCode = columnCode;
    }

    /**
     * �����ֶ���ʾ��
     * @param �ֶ���ʾ��
     */
    public void setColumnName ( String columnName ) {
        this.columnName = columnName;
    }

    /**
     * �����ֶ���������
     * @param dataType �ֶ���������
     */
    public void setDataType ( String dataType ) {
        this.dataType = dataType;
    }

    /**
     * �����ֶ�ȱʡֵ
     * @param defaultValue �ֶ�ȱʡֵ
     */
    public void setDefaultValue ( String defaultValue ) {
        this.defaultValue = defaultValue;
    }

    /**
     * �����ĵ�����ID
     * @param docTypeID �ĵ�����ID
     */
    public void setDocTypeID ( int doctypeID ) {
        this.docTypeID = doctypeID;
    }

    /**
     * �����ֶ�ID
     * @param fieldID �ֶ�ID 
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
     * �����ֶ����ݳ���
     * @param dataLength �ֶ����ݳ���
     */
    public void setDataLength ( int length ) {
        this.dataLength = length;
    }

    /**
     * �����ֶξ���
     * @param scale �ֶξ���
     */
    public void setScale ( int scale ) {
        this.scale = scale;
    }

    /**
     * �����ֶ�״̬
     * @param status ״̬
     */
    public void setStatus ( String status ) {
        this.status = status;
    }

    /**
     * ȱʡ���캯��
     */
    public DocTypeField() {

    }

    /**
     * ������캯���ڴ����ĵ����ͣ�����ƽ̨�ֶ�ʱʹ�á�
     * 
     * @param ci
     *            Ԥ�ȶ���õ�ColumnInfo������context.BaseField��
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
	 * Clone�������
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