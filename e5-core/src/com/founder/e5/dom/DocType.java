package com.founder.e5.dom;

/**
 * @created 05-����-2005 9:02:25
 * @author ZhangKaifeng
 * @version 1.0
 */
public class DocType {

    /**
     * �ĵ�����ID
     */
    private int docTypeID;

    /**
     * �ĵ���������
     */
    private String docTypeName;

    /**
     * ��Ӧ�ĵ�����ID�����м��ö��Ÿ���
     */
    private String docTypeRelated;

    /**
     * ���ĵ����͵�ȱʡ���� Comment for <code>defaultFlow</code>
     */
    private int defaultFlow;

    /**
     * ����
     */
    private String descInfo;

    /**
     * ��ϵͳid����ʼֵΪ0������hbm����ӳ��
     */
    private int appID;

    /**
     * ���캯��
     * @param docTypeName �ĵ���������
     * @param descInfo ������Ϣ
     */
    public DocType(String docTypeName, String descInfo) {
        super();
        this.docTypeName = docTypeName;
        this.descInfo = descInfo;
    }

    /**
     * ���캯��
     * @param flow ȱʡ����ID
     * @param description ������Ϣ
     * @param name ����
     * @param related �������ĵ�����ID��
     * @param appID ��ϵͳID
     */
    public DocType(int flow, String description, String name, String related,
            int appID) {

        super ( );
        this.defaultFlow = flow;
        this.descInfo = description;
        this.docTypeName = name;
        this.docTypeRelated = related;
        this.appID = appID;
    }

    /**
     * ȱʡ���캯��
     */
    public DocType() {

    }

    /**
     * ����ȱʡ����
     * @return ȱʡ���̵�ID
     */
    public int getDefaultFlow ( ) {
        return this.defaultFlow;
    }

    /**
     * ����ȱʡ����
     * @param defaultFlow ȱʡ����ID
     */
    public void setDefaultFlow ( int defaultFlow ) {
        this.defaultFlow = defaultFlow;

    }

    /**
     * �����ĵ�����������Ϣ
     * @return ������Ϣ
     */
    public String getDescInfo ( ) {
        return this.descInfo;
    }

    /**
     * ����������Ϣ
     * @param description ������Ϣ
     */
    public void setDescInfo ( String description ) {
        this.descInfo = description;
    }

    /**
     * �����ĵ�����ID
     * @return �ĵ�����ID
     */
    public int getDocTypeID ( ) {
        return this.docTypeID;
    }

    /**
     * �����ĵ���������
     * @return �ĵ���������
     */
    public String getDocTypeName ( ) {
        return this.docTypeName;
    }

    /**
     * ������ϵͳID
     * @param appID ��ϵͳID
     */
    public void setAppID ( int appID ) {
        this.appID = appID;
    }

    void setDocTypeName ( String docTypeName ) {
        this.docTypeName = docTypeName;
    }

    /**
     * ���ع������ĵ�����ID
     * @return ���硰2,3,4��
     */
    public String getDocTypeRelated ( ) {
        return this.docTypeRelated;
    }

    /**
     * ������ϵͳID
     * @return ��ϵͳID
     */
    public int getAppID ( ) {
        return appID;
    }

    void setDocTypeID ( int docTypeID ) {
        this.docTypeID = docTypeID;
    }

    /**
     * ���ù������ĵ�����
     * @param doctypeRelated �������ĵ�����ID�����硰2,3,4��
     */
    public void setDocTypeRelated ( String doctypeRelated ) {
        this.docTypeRelated = doctypeRelated;
    }

}